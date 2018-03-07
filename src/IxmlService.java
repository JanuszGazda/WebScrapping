import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.IOException;
import java.util.ArrayList;

public interface IxmlService {

    //metoda zapisuje String'a do pliku XML
    static void stringToXML(String xmlSource)
            throws IOException {
        java.io.FileWriter fw = new java.io.FileWriter("produkty.xml");
        fw.write(xmlSource);
        fw.close();
    }


    static String objectToXMLString(ArrayList<Produkt> lista) {
        XStream xstream = new XStream(new DomDriver());
        String xml = "";
        for (int i = 0; i <= lista.size() - 1; i++) {
            xml += xstream.toXML(lista.get(i));
            xml += '\n';
        }
        return xml;
    }

}
