
public class Produkt {

    String nazwa;
    String cena;
    String zdjecie;

    public Produkt(String nazwa, String cena, String zdjecie){
        this.nazwa=nazwa;
        this.cena=cena;
        this.zdjecie = zdjecie;
    }

    public Produkt(){}

    @Override
    public String toString() {
        return ("Nazwa: "+this.getNazwa()+
        " Cena:"+this.getCena()+
        " URL:"+this.getZdjecie());
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getCena() {
        return cena;
    }

    public void setCena(String cena) {
        this.cena = cena;
    }

    public String getZdjecie() {
        return zdjecie;
    }

    public void setZdjecie(String zdjecie) {
        this.zdjecie = zdjecie;
    }

}
