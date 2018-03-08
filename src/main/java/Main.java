import java.io.IOException;
import java.util.ArrayList;

public class Main implements IxmlService, ICeneoService {

    public static void main(String[] args) {

        //Strona z której pobierane będą produkty
        String url = "https://www.ceneo.pl/Filmy_Blu-ray/Gatunek:Melodramaty.htm";

        ArrayList<Produkt> lista = ICeneoService.stringToObject(url);

        try {
            IxmlService.stringToXML(IxmlService.objectToXMLString(lista));
            System.out.println("\nPlik XML stworzony z "+lista.size()+" produktami");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
