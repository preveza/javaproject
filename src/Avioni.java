public class Avioni {
    private String kodiAvionit;
    private String modeli;
    private int kapacitetiEconomy;
    private int kapacitetiBusiness;

    public Avioni(String kodiAvionit, String modeli, int kapacitetiEconomy, int kapacitetiBusiness) {
        this.kodiAvionit = kodiAvionit;
        this.modeli = modeli;
        this.kapacitetiEconomy = kapacitetiEconomy;
        this.kapacitetiBusiness = kapacitetiBusiness;
    }

    public String getModeli() { return modeli; }
    public String getKodiAvionit() { return kodiAvionit; }
    public int getKapacitetiEconomy() { return kapacitetiEconomy; }
    public int getKapacitetiBusiness() { return kapacitetiBusiness; }
}