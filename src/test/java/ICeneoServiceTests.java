import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ICeneoServiceTests {

    @Test
    public final void urlToObject(){

        String psychologia = "https://www.ceneo.pl/Filmy_Blu-ray/Gatunek:Psychologiczne.htm";
        ArrayList<Produkt> lista = new ArrayList<>();
        lista.add(new Produkt("Podziemny Krąg (Fight Club) (Blu-ray)", "39,99", "https://image.ceneostatic.pl/data/products/6718843/f-podziemny-krag-fight-club-blu-ray.jpg"));

        assertEquals(lista.get(0).getNazwa(), ICeneoService.stringToObject(psychologia).get(0).getNazwa());
    }

}
