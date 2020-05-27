package com.company;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DBConnection {

    private Connection conn;
    private Statement statement;
    private PreparedStatement preper;
    private ResultSet result;
	private String database = "sqlite";
    //private String database = "postgresql";

    public DBConnection(){
        conn = null;
        statement = null;
        preper = null;
        result = null;
    }

    public void connect(){
        String url = "";
        try{
            if (database.equals("sqlite")){
                url = "jdbc:sqlite:"; // uzupełnić swoją loklizację bazy danych 
                conn = DriverManager.getConnection(url);
                if(conn != null){
                    System.out.println("Nawiązano połączenie");
                }
            }else{
                url = "jdbc:postgresql://localhost:5432/Test";
                System.setProperty("jdbc.Drivers", "org.postgresql.Drivers");
                conn = DriverManager.getConnection(url, "student", "student");
                if(conn != null){
                    System.out.println("Nawiązano połączenie");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try{
            if(!conn.isClosed()){
                conn.close();
                System.out.println("Połączenie zakończone");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void createTable(){
        try {
            statement = conn.createStatement();
            String sql = "";
            if (database.equals("sqlite")){
                //uzupełnićtworzenie bazy pod SQLite;
                sql = "create table if not exists links_temp " +
                        "(id integer primary key autoincrement, " +
                        "name text, " +
                        "day timestamp default CURRENT_TIMESTAMP)";
                if(!statement.execute(sql)){
                    System.out.println("Tabela dodana do bazy");
                }
                sql = "create table if not exists links_visited " +
                        "(id integer primary key autoincrement, " +
                        "name text, " +
                        "day timestamp default CURRENT_TIMESTAMP)";
                if(!statement.execute(sql)){
                    System.out.println("Tabela dodana do bazy");
                }
                sql = "create table if not exists links_unsupported " +
                        "(id integer primary key autoincrement, " +
                        "name text, " +
                        "day timestamp default CURRENT_TIMESTAMP)";
                if(!statement.execute(sql)){
                    System.out.println("Tabela dodana do bazy");
                }
            }else{
                sql = "create table if not exists links_temp " +
                        "(id serial, " +
                        "name text, " +
                        "day timestamp default now()," +
                        "primary key(id))";
                if(!statement.execute(sql)){
                    System.out.println("Tabela dodana do bazy");
                }
                sql = "create table if not exists links_visited " +
                        "(id serial, " +
                        "name text, " +
                        "day timestamp default now()," +
                        "primary key(id))";
                if(!statement.execute(sql)){
                    System.out.println("Tabela dodana do bazy");
                }
                sql = "create table if not exists links_unsupported " +
                        "(id serial, " +
                        "name text, " +
                        "day timestamp default now()," +
                        "primary key(id))";
                if(!statement.execute(sql)){
                    System.out.println("Tabela dodana do bazy");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void addLinkToTempTab(String link){
        try {
			if(database.equals("sqlite")){
                preper = conn.prepareStatement("insert into links_temp (name) values (?)");
            }else{
                preper = conn.prepareStatement("insert into links_temp values (default, ?)");
            }
            preper.setString(1, link);
            int number = preper.executeUpdate();
            //System.out.println("Wstawiono " + number + " rekoed");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void addLinkToVisitedTab(String link){
        try {
            if(database.equals("sqlite")){
                preper = conn.prepareStatement("insert into links_visited (name) values (?)");
            }else{
                preper = conn.prepareStatement("insert into links_visited values (default, ?)");
            }
            preper.setString(1, link);
            int number = preper.executeUpdate();
            System.out.println("Wstawiono " + number + " rekoed");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public int getTempTabSize(){
        try {
            statement = conn.createStatement();
            String sql = "select count(*) from links_temp";
            result = statement.executeQuery(sql);
            int number = 0;
            while (result.next()){
                number = result.getInt(1);
            }
            return number;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public String getLinkToVisit(){
        try {

            preper = conn.prepareStatement("select name from links_temp limit 3");
            result = preper.executeQuery();

            ArrayList<String> tempLink = new ArrayList<>();
            while (result.next()){
                tempLink.add(result.getString("name"));
            }

            for (int i = 0; i < tempLink.size(); i++){
                preper = conn.prepareStatement("select name from links_visited where name = ?",
                        ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                preper.setString(1, tempLink.get(i));
                result = preper.executeQuery();

                result.next();
                int number = result.getRow();
                //System.out.println(number);
                if(number > 0 ){
                    preper = conn.prepareStatement("delete from links_temp where name = ?");
                    preper.setString(1, tempLink.get(i));
                    int delRow = preper.executeUpdate();
                    System.out.println("Z tabeli links_temp usunięto podobnych rekordów: "+ delRow);
                }else{
                    return tempLink.get(i);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return "";
    }

}






