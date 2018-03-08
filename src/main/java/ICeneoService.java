import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.misc.BASE64Encoder;

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

public interface ICeneoService {


    static ArrayList<Produkt> stringToObject(String url) {

        ArrayList<Produkt> lista = new ArrayList<>();
        Elements strony;
        Elements produkty;
        Boolean wyjscie = false;

        //pętla do-while jest odpowiedzialna za iteracje po kolejnych stronach
        do {
            System.out.println("Strona: " + url);
            Document doc = Jsoup.parse(readURL(url));
            produkty = doc.select("div[data-pid]");

            iterateProducts(produkty, lista);

            //Sprawdzenie czy strona posiada link do kolejnych stron
            strony = doc.select("div.pagination");
            if (strony.size() != 0) {
                Element link = strony.select("span").last();
                String koniec = link.attr("class");

                if (koniec.equals("last-page")) {
                    wyjscie = true;
                } else {
                    Element alink = link.select("a").first();
                    url = "https://www.ceneo.pl" + alink.attr("href");
                }
            }

        } while (strony.size() != 0 && wyjscie == false);
        return lista;
    }

    static void iterateProducts(Elements produkty, ArrayList<Produkt> lista) {

        //iteracja po kolejnych produktach na stronie
        produkty.stream()
                .filter(p -> extractInfo(p, false).getNazwa()!=null)
                .forEach(p -> lista.add(extractInfo(p, true)));
    }


    static Produkt extractInfo(Element p, boolean info){

        Produkt pr = new Produkt();

        String nazwa = null;
        String cena = null;
        String urlZdjecia = null;
        int indeksURLbegin, indeksURLend, cenaBegin, cenaEnd, groszeBegin, groszeEnd;

        //Wyszukanie cech produktu
        Scanner scanner = new Scanner(p.toString());
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            //nazwa
            if (line.contains("list-prod-name")) {
                nazwa = scanner.nextLine().substring(4);
                if(info){System.out.println("Produkt: " + nazwa);}
            }//zdjęcie
            else if (line.contains("data-src=")) {
                indeksURLbegin = line.indexOf("data-src=");
                indeksURLend = line.indexOf("data-preloader");
                urlZdjecia = line.substring(indeksURLbegin + 10, indeksURLend - 2);
            }//cena
            else if (line.contains("price-int")) {
                cenaBegin = line.indexOf("price-int") + 11;
                cenaEnd = line.indexOf("</span><span class=\"price-fraction\"");
                groszeBegin = line.indexOf("price-fraction") + 16;
                groszeEnd = groszeBegin + 3;
                cena = line.substring(cenaBegin, cenaEnd) + line.substring(groszeBegin, groszeEnd);
            }
        }
        pr.setNazwa(nazwa);
        pr.setCena(cena);
        try {
            pr.setZdjecie(encodeToString(new URL("https:" + urlZdjecia)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        scanner.close();
        return pr;
    }

    //zapis strony www do Stringa
    static String readURL(String url) {

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
    static String encodeToString(URL url) {
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

}
