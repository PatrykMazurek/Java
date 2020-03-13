package com.company;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class XMLFile {

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

            // formatowanie danych przygotowanych danych do pliku
            Transformer t = TransformerFactory.newInstance().newTransformer();
            // ndawanie odpowiednich atrybutów
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            // zapisywanie danych do pliku
            t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(fileName)));
            System.out.println("Plik xml został stworzony");
        } catch (ParserConfigurationException | TransformerException | FileNotFoundException e) {
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

            for (int i=0; i< osobyNodes.getLength(); i++){
                if(osobyNodes.item(i) instanceof Element){
                    Node tempNode = osobyNodes.item(i);
                    System.out.println("Nazwa węzła: " + tempNode.getNodeName());
                    // sprawdzenie i pobranie atrybutu węzła
                    if (tempNode.hasAttributes()){
                        System.out.println("Węzeł posiada atrybut jego wartość:");
                        NamedNodeMap attrMap = tempNode.getAttributes();
                        for (int z=0; z<attrMap.getLength(); z++){
                            System.out.print(attrMap.item(z).getNodeName() + ": ");
                            System.out.println(attrMap.item(z).getNodeValue());
                        }
                        System.out.println(tempNode.getAttributes());
                    }
                    // sprawdzenie i pobranie wartości węzła
                    if(!tempNode.getTextContent().isEmpty() ){
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
            NodeList xOsobaList = (NodeList)expPath.evaluate(doc, XPathConstants.NODESET);
            for(int t = 0; t<xOsobaList.getLength(); t++){
                if(xOsobaList.item(t) instanceof Element){
                    Element tempElement = (Element)xOsobaList.item(t);
                    System.out.println(tempElement.getTagName());
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}
