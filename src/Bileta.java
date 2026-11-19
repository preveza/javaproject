public class Bileta {
    private String kodiRezervimit;
    private Pasagjeri pasagjeri;
    private Fluturimi fluturimi;
    private Ulesja ulesja;
    private Bagazhi bagazhi;
    private Pagesa pagesa;
    private String paketa;
    private String sherbimet;
    private boolean eshteAktiv;
    private boolean checkInKryer;

    public Bileta(String kodiRezervimit, Pasagjeri pasagjeri, Fluturimi fluturimi,
                  Ulesja ulesja, Bagazhi bagazhi, Pagesa pagesa, String paketa, String sherbimet) {
        this.kodiRezervimit = kodiRezervimit;
        this.pasagjeri = pasagjeri;
        this.fluturimi = fluturimi;
        this.ulesja = ulesja;
        this.bagazhi = bagazhi;
        this.pagesa = pagesa;
        this.paketa = paketa;
        this.sherbimet = sherbimet;
        this.eshteAktiv = true;
        this.checkInKryer = false;
        this.ulesja.setEshteEZene(true);
    }

    public void anuloBileten() {
        this.eshteAktiv = false;
        this.ulesja.setEshteEZene(false);
    }

    public void kryejCheckIn() { this.checkInKryer = true; }
    public boolean isCheckInKryer() { return checkInKryer; }
    public String getKodiRezervimit() { return kodiRezervimit; }
    public Pasagjeri getPasagjeri() { return pasagjeri; }
    public Fluturimi getFluturimi() { return fluturimi; }
    public Ulesja getUlesja() { return ulesja; }
    public Bagazhi getBagazhi() { return bagazhi; }
    public Pagesa getPagesa() { return pagesa; }
    public String getPaketa() { return paketa; }
    public String getSherbimet() { return sherbimet; }
    public boolean isEshteAktiv() { return eshteAktiv; }
}