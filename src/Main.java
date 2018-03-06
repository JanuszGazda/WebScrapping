import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.misc.BASE64Encoder;

public class Main {

    public static void main(String[] args) {

        //Strona z której pobierane będą produkty
        String url = "https://www.ceneo.pl/Filmy_Blu-ray/Gatunek:Melodramaty.htm";

        ArrayList<Produkt> lista = new ArrayList<>();
        Elements strony;
        Elements produkty;
        Boolean wyjscie = false;

        //pętla do-while jest odpowiedzialna za iteracje po kolejnych stronach
        do{
            System.out.println("Strona: "+ url);
            Document doc =  Jsoup.parse(readURL(url));
            produkty = doc.select("div[data-pid]");

        //pętla for jest odpowiedzialna za iteracje po kolejnych produktach na stronie
        for(Element p : produkty)  {

            Produkt pr = new Produkt();

            int indeksURLbegin, indeksURLend, cenaBegin,  cenaEnd, groszeBegin, groszeEnd;
            String nazwa = null;
            String cena = null;
            String urlZdjecia = null;

            //Wyszukanie cech produktu
            Scanner scanner = new Scanner(p.toString());
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                //nazwa
                if (line.contains("list-prod-name")) {
                    nazwa = scanner.nextLine().substring(4);
                    System.out.println("Produkt: " + nazwa);
                }//zdjęcie
                else if(line.contains("data-src=")){
                    indeksURLbegin = line.indexOf("data-src=");
                    indeksURLend   = line.indexOf("data-preloader");
                    urlZdjecia     = line.substring(indeksURLbegin+10, indeksURLend-2);
                }//cena
                else if(line.contains("price-int")){
                    cenaBegin   = line.indexOf("price-int")+11;
                    cenaEnd     = line.indexOf("</span><span class=\"price-fraction\"");
                    groszeBegin = line.indexOf("price-fraction")+16;
                    groszeEnd   = groszeBegin+3;
                    cena        = line.substring(cenaBegin, cenaEnd)+line.substring(groszeBegin, groszeEnd);
                }
            }

            //warunek if zabezpiecza przed wstawieniem proponowanych pozycji do listy
            //takie pozycje posiadają nazwę o wartości null
            if(nazwa!=null){
                pr.setNazwa(nazwa);
                try {
                    pr.setZdjecie(encodeToString(new URL("https:" + urlZdjecia)));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                pr.setCena(cena);
                lista.add(pr);
            }
            scanner.close();
        }

        //Ile znaleziono produktów na stronie
        System.out.println("Ile zczytano: "+produkty.size());
        //Ile produktów pochodzi z wyszukiwarki(pomijamy proponowane pozycje)
        System.out.println("Ile w liście: "+lista.size());



            //Sprawdzanie czy strona posiada link do kolejnych stron
            strony = doc.select("div.pagination");
            if(strony.size()!=0) {
                Element link = strony.select("span").last();
                String koniec = link.attr("class");

                if (koniec.equals("last-page")) {
                    wyjscie = true;
                } else {
                    Element alink = link.select("a").first();
                    url = "https://www.ceneo.pl" + alink.attr("href");
                }
            }

        }while(strony.size()!=0 && wyjscie==false);


        //XSTREAM
        //biblioteka wykorzystywana do zapisu obiektów
        // do Stringa w formacie XML
        XStream xstream = new XStream(new DomDriver());
        String xml = "";
        for(int i=0; i<=lista.size()-1; i++){
            xml += xstream.toXML(lista.get(i));
            xml += '\n';
        }

        try {
            stringToXML(xml);
            System.out.println("Plik XML stworzony z "+lista.size()+" produktami");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //metoda przeznaczona do zapisu strony www do Stringa
    private static String readURL(String url) {

        String fileContents = "";
        String currentLine = "";

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            fileContents = reader.readLine();
            while (currentLine != null) {
                currentLine = reader.readLine();
                fileContents += "\n" + currentLine;
            }
            reader.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage()+" nie odpowiada", "Error Message", JOptionPane.OK_OPTION);
            e.printStackTrace();

        }
        return fileContents;
    }

    //metoda przyjmuje url zdjęcia jako parametr
    // i zwraca zdjęcie w postaci Base64 (String)
    private static String encodeToString(URL url) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            Image image = ImageIO.read(url.openStream());
            BufferedImage buff = (BufferedImage) image;
            ImageIO.write(buff, "jpg", bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    //metoda zapisuje String'a do pliku XML
    private static void stringToXML(String xmlSource)
            throws IOException {
        java.io.FileWriter fw = new java.io.FileWriter("produkty.xml");
        fw.write(xmlSource);
        fw.close();
    }
}
