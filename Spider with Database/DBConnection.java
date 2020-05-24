package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DBConnection {

    private Connection conn;
    private Statement statement;
    private PreparedStatement preper;
    private ResultSet result;

    public DBConnection(){
        conn = null;
        statement = null;
        preper = null;
        result = null;
    }


    public void connect(){
        try{
            String url = "jdbc:postgresql://localhost:5432/Test";
            String sqliteUrl = "jdbc:sqlite:C:/Projekty/baza_test.db";

            System.setProperty("jdbc.Drivers", "org.postgresql.Drivers");

            Properties properties = new Properties();
            properties.setProperty("user", "student");
            properties.setProperty("password", "student");
            conn = DriverManager.getConnection(url, properties);

            if(conn != null){
                System.out.println("Nawiązano połączenie z bazą danych");
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
                System.out.println("Połączenie zamknięte ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable(){
        try {
            statement = conn.createStatement();
            String sql = "create table if not exists links_temp " +
                    "(id serial, " +
                    "name text, " +
                    "day timestamp default now()," +
                    "primary key(id))";
            statement.execute(sql);

            sql = "create table if not exists links_visited " +
                    "(id serial, " +
                    "name text, " +
                    "day timestamp default now()," +
                    "primary key(id))";
            int number = statement.executeUpdate(sql);
            System.out.println("Wykonano operacji " + number);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    public void addLinkToTempTab(String link){
        try {
            preper = conn.prepareStatement("insert into links_temp values (default, ?)");
            preper.setString(1, link);
            int number = preper.executeUpdate();
            if (number > 0 ){
                System.out.println("dodano " + number + " rekordów do bazy danych ");
            }else{
                System.out.println("nie dodano rekordu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addLinkToVisitiedTab(String link){
        try {
            preper = conn.prepareStatement("insert into links_visited values (default, ?)");
            preper.setString(1, link);
            int number = preper.executeUpdate();
            if (number > 0 ){
                System.out.println("dodano " + number + " rekordów do bazy danych ");
            }else{
                System.out.println("nie dodano rekordu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getSizeTemTab(){
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
            preper = conn.prepareStatement("select * from links_temp limit 3");
            result = preper.executeQuery();

            ArrayList<String> temlList = new ArrayList<>();

            while (result.next()){
                temlList.add(result.getString("name"));
            }

            for(int i = 0; i<temlList.size();i++){
                preper = conn.prepareStatement("select name from links_visited " +
                        "where name = ?");
                preper.setString(1, temlList.get(i));

                result = preper.executeQuery();
                int number = 0;
                while (result.next()){
                    number ++;
                }
                System.out.println(number + " " + temlList.get(i));
                if ( number > 0 ){
                    // link był odwiedzony usuń go z tabeli links_temp
                }else{
                    return temlList.get(i);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}






