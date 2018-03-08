import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class IxmlServiceTests {


    @Test
    public final void testObjToXMLString(){
        ArrayList<Produkt> lista = new ArrayList<>();
        lista.add(new Produkt("Krzesło", "22,22", "urlZdjecia"));
        assertEquals("<Produkt>\n" +
                "  <nazwa>Krzesło</nazwa>\n" +
                "  <cena>22,22</cena>\n" +
                "  <zdjecie>urlZdjecia</zdjecie>\n" +
                "</Produkt>", IxmlService.objectToXMLString(lista));
    }


}
