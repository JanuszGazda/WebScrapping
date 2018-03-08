
public class Produkt {

    private String nazwa;
    private String cena;
    private String zdjecie;

    protected Produkt(String nazwa, String cena, String zdjecie){
        this.nazwa=nazwa;
        this.cena=cena;
        this.zdjecie = zdjecie;
    }

    protected Produkt(){}

    protected String getNazwa() {
        return nazwa;
    }

    protected void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    protected String getCena() {
        return cena;
    }

    protected void setCena(String cena) {
        this.cena = cena;
    }

    protected String getZdjecie() {
        return zdjecie;
    }

    protected void setZdjecie(String zdjecie) {
        this.zdjecie = zdjecie;
    }

}
