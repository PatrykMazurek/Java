package com.company;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class XMLFile {

    public void SaveDataAndShow(Document document, String filename)  {
        try{
            // konwetowanie struktury dokumentu i zapisanie go w pliku
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.METHOD, "XML");
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            t.transform(new DOMSource(document), new StreamResult(new FileOutputStream(filename)));

            // wyświetlanie w czytelny sposób
            StringWriter sw = new StringWriter();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            // parametr odpowiadający za wcięcia w dokumęcie
            t.transform(new DOMSource(document), new StreamResult(sw));
            System.out.println(sw);
        } catch (TransformerException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void SaveToXML(String fileName) {
        // fileName - nazwapliku lub lokalizacja pliku + nazwa pliku
        try {
            // Tworzenie nowego obiektu typu Document do obsługi np. formatu XML
            DocumentBuilder builder;
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();

            // tworzenie węzła głównego
            Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);

            // tworzenie węzłów potomnych
            // Dla przykładu stworzony zostanie dokument XML zawierający osoby
            // Stworzenie węzła osaba
            Element osobaE = doc.createElement("Osoba");
            osobaE.setAttribute("nr", "1");
            rootElement.appendChild(osobaE);

            // każda osoba posiada imię, nazwisko, adres
            Element imieE = doc.createElement("imie");
            imieE.setTextContent("Jan");
            osobaE.appendChild(imieE);
            Element nazwiskoE = doc.createElement("nazwisko");
            nazwiskoE.setTextContent("Kowalski");
            osobaE.appendChild(nazwiskoE);

            // węzeł adres zawiera informacje o uliczy, numerze budynku i lokalu kodzie pocztowym oraz mieście
            Element adresE = doc.createElement("adres");
            osobaE.appendChild(adresE);
            Element ulicaE = doc.createElement("ulica");
            ulicaE.setTextContent("Kwiatowa");
            adresE.appendChild(ulicaE);
            Element budynekE = doc.createElement("budynek");
            budynekE.setAttribute("nr", "12A");
            budynekE.setAttribute("lok", "10");
            adresE.appendChild(budynekE);
            Element miastoE = doc.createElement("miasto");
            miastoE.setAttribute("poczta", "30-234");
            miastoE.setTextContent("Kraków");
            adresE.appendChild(miastoE);

            // tworzenie węzłów osoba należy powtarzać dla każdej osoby która ma się znaleśćw
            // dokumencie

            SaveDataAndShow(doc, fileName);
//            // formatowanie danych przygotowanych danych do pliku
//            Transformer t = TransformerFactory.newInstance().newTransformer();
//            // ndawanie odpowiednich atrybutów
//            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
//            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//            // należy dodawać tylko w przypadku gdzy chceny czytaćdokument w np. notatniku
//            //t.setOutputProperty(OutputKeys.INDENT, "yes");
//            t.setOutputProperty(OutputKeys.METHOD, "xml");
//            // zapisywanie danych do pliku
//            t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(fileName)));
//            System.out.println("Plik xml został stworzony");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void ReadFromXML(String fileName) {
        // fileName - nazwapliku lub lokalizacja pliku + nazwa pliku
        try {
            // Tworzenie nowego obiektu typu Document do obsługi np. formatu XML
            DocumentBuilder builder;
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            File f = new File(fileName);
            // parsowanie dokumentu do formatu XML
            Document doc = builder.parse(f);

            // pobieranie głównego elementu dokumentu
            Element root = doc.getDocumentElement();

            // przedstawione zostaną dwa sposoby odczytwania danych z
            // dokumentu XML

            // ----- sposób 1 -----

            // pobieranie konkretnych węzłów pi nazwie
            NodeList osobyNodes = root.getElementsByTagName("Osoba");

            for (int i = 0; i < osobyNodes.getLength(); i++) {
                if (osobyNodes.item(i) instanceof Element) {
                    Node tempNode = osobyNodes.item(i);
                    System.out.println("Nazwa węzła: " + tempNode.getNodeName());
                    // sprawdzenie i pobranie atrybutu węzła
                    if (tempNode.hasAttributes()) {
                        System.out.println("Węzeł posiada atrybut jego wartość:");
                        NamedNodeMap attrMap = tempNode.getAttributes();
                        for (int z = 0; z < attrMap.getLength(); z++) {
                            System.out.print(attrMap.item(z).getNodeName() + ": ");
                            System.out.println(attrMap.item(z).getNodeValue());
                        }
                        System.out.println(tempNode.getAttributes());
                    }
                    // sprawdzenie i pobranie wartości węzła
                    if (!tempNode.getTextContent().isEmpty()) {
                        System.out.println("wartość węzla:");
                        System.out.println(tempNode.getTextContent());
                    }
                }
            }

            // ----- sposób 2 -----
            System.out.println("Wyszukiwanie za pomocą XPath");
            XPath xPath = XPathFactory.newInstance().newXPath();
            // wyszukwianie węzła pprzy pomocy wyrażenia XPath
            //XPathExpression expPath = xPath.compile("//Osoba");
            // wyszukanie wżełów adres
            XPathExpression expPath = xPath.compile("//Osoba/adres");
            NodeList xOsobaList = (NodeList) expPath.evaluate(doc, XPathConstants.NODESET);
            for (int t = 0; t < xOsobaList.getLength(); t++) {
                if (xOsobaList.item(t) instanceof Element) {
                    Element tempElement = (Element) xOsobaList.item(t);
                    System.out.println(tempElement.getTagName());
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    public void AddNewNodeToXML(String fileName) {
        try {
            // stworzenie lokalizacji pliku
            Path locationXML = Paths.get(fileName);
            FileInputStream f;
            // sptawdzenie czy podana lokalizacja istnieje
            if (Files.notExists(locationXML)) {
                System.out.println("Plik nie istniej");
            } else {
                // pobieranie danych od użytkownika
                System.out.println("Pliki istnieje i można dodać do nowe węzły ");
                System.out.println("Podaj nowe dane do wstawienia:");
                Scanner input = new Scanner(System.in);
                System.out.print("Imię: ");
                String imie = input.nextLine();
                System.out.print("Nazwisko: ");
                String nazwisko = input.nextLine();
                System.out.println("Dane adresowe ");
                System.out.print("Ulica: ");
                String ulica = input.nextLine();
                System.out.print("Numer budynku: ");
                String nrBud = input.nextLine();
                System.out.print("Numer lokalu: ");
                String nrLok = input.nextLine();
                System.out.print("Miasto: ");
                String Miasto = input.nextLine();
                System.out.print("Kod pocztowy: ");
                String Poczta = input.nextLine();

                //wyświetlenie danych pobranych od użytkownika
//                System.out.println("wprowadzone dane:");
//                System.out.println("Imię Nazwisko: " + imie + " " + nazwisko);
//                System.out.println("Adres:");
//                System.out.println("Ulica: " + ulica + " bud / lok " + nrBud + " / " + nrLok);
//                System.out.println("Miasto: " + Miasto + " kod pocztowy: " + Poczta);

                // odwołanie od pliku
                f = new FileInputStream(fileName);
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.parse(f);
                // odwołanie do głównego węzła dokumentu
                Element root = doc.getDocumentElement();
                // sprawdzenie liczby węzłów
                int nr = root.getChildNodes().getLength() + 1;
                System.out.println("Liczba elementów: " + nr);
                // tworzenie nowego węzła Osoba i uzupełnianie go danymi
                Element osobaE = doc.createElement("Osoba");
                osobaE.setAttribute("nr", String.valueOf(nr));
                Element imieE = doc.createElement("Imie");
                imieE.setTextContent(imie);
                Element nazwiskoE = doc.createElement("Nazwisko");
                nazwiskoE.setTextContent(nazwisko);
                osobaE.appendChild(imieE);
                osobaE.appendChild(nazwiskoE);
                Element adresE = doc.createElement("Adres");
                Element ulicaE = doc.createElement("Ulica");
                ulicaE.setTextContent(ulica);
                Element budynekE = doc.createElement("Budynek");
                budynekE.setAttribute("nr", nrBud);
                budynekE.setAttribute("lok", nrLok);
                Element miastoE = doc.createElement("Miasto");
                miastoE.setTextContent(Miasto);
                miastoE.setAttribute("poczta", Poczta);
                adresE.appendChild(ulicaE);
                adresE.appendChild(budynekE);
                adresE.appendChild(miastoE);

                osobaE.appendChild(adresE);
                System.out.println("Czy chcesz wybrać miejsce w którym wstawisz nowy węzeł? t/n");
                String odp = input.nextLine();
                if (odp.toLowerCase().equals("t")){
                    // wstawienie węzła w wybrane miejscu
                    System.out.println("Wstawienie węzła w wybranym miejscu");
                    NodeList listaOsob = root.getChildNodes();
                    for (int i = 0; i< listaOsob.getLength(); i++){
                        Element tempOsoba = (Element) listaOsob.item(i);
                        System.out.print("nr: " + tempOsoba.getAttribute("nr") + ") ");
                        NodeList tempDane = tempOsoba.getChildNodes();
                        System.out.println(tempDane.item(0).getTextContent() + " " + tempDane.item(1).getTextContent());
                    }
                    System.out.println("Proszę o wybranie węzeł przed którym zostanie wstawiony nowy węzeł");
                    String elementPo = input.nextLine();
                    for (int i = 0; i< listaOsob.getLength(); i++){
                        Element tempNode = (Element) listaOsob.item(i);
                        if(elementPo.equals(tempNode.getAttribute("nr")) ){
                            System.out.println("wstawiono węzeł");
                            root.insertBefore(osobaE, listaOsob.item(i));
                        }
                    }
                }else{
                    // wstawienie węzła na końcu dokumentu
                    System.out.println("węzeł został wstawiony na końcu dokumentu");
                    root.appendChild(osobaE);
                }

                SaveDataAndShow(doc, fileName);
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    public void RemoveNodeFromXML(String fileName){
        try {
            // stworzenie lokalizacji pliku
            Path locationXML = Paths.get(fileName);
            FileInputStream f;
            // sptawdzenie czy podana lokalizacja istnieje
            if (Files.notExists(locationXML)) {
                System.out.println("Plik nie istniej");
            }else{
                System.out.println("Plik istnieje proszę wybraćrekord do usunięcia:");
                // odwołanie od pliku
                f = new FileInputStream(fileName);
                // tworzenie obiektu typu DocumentBuilder do przetwarzania pliku XML
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.parse(f);
                // Odwołanie do głównego elementu dokumentu
                Element root = doc.getDocumentElement();
                // pobranie wszystkich węzłów Osoba
                NodeList listaOsob = root.getChildNodes();
                // pętla odpowiadająca za sterowanie usuwaniem węzłów
                while(listaOsob.getLength()>0){
                    // wyświetlanie informacji o dostępnych elementów
                    for (int i = 0; i< listaOsob.getLength(); i++){
                        Element tempOsoba = (Element) listaOsob.item(i);
                        System.out.print("nr: " + tempOsoba.getAttribute("nr") + ") ");
                        NodeList tempDane = tempOsoba.getChildNodes();
                        System.out.println(tempDane.item(0).getTextContent() + " " + tempDane.item(1).getTextContent());
                    }
                    // pobieranie numeru węzła który ma być usunięty
                    Scanner input = new Scanner(System.in);
                    System.out.print("Proszę o podanie numeru: ");
                    String znak = input.nextLine();
                    if (!znak.isEmpty()){
                        if(znak.equals("n")){
                            System.out.println("Program zapisuej pozostałe dane w pliku");
                            break;
                        }else{
                            // wyszukanie i usuwanie elementu z listy węzłów
                            for (int i = 0; i< listaOsob.getLength(); i++){
                                Element tempNode = (Element) listaOsob.item(i);
                                if(znak.equals(tempNode.getAttribute("nr")) ){
                                    System.out.println("Usuwam element");
                                    root.removeChild(listaOsob.item(i));
                                }
                            }
                            listaOsob = root.getElementsByTagName("Osoba");
                        }
                    }else{
                        System.out.println("Nie podano numeru");
                    }
                }
                SaveDataAndShow(doc, fileName);
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
}
