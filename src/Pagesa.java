public class Pagesa {
    private double shumaTotale;
    private String menyraPageses;
    private boolean ePaguar;

    public Pagesa(double cmimiBaze, String klasaUleses, Bagazhi bagazhi, boolean perdorZbritjePikesh) {
        double cmimiKlases = klasaUleses.equalsIgnoreCase("Business") ? cmimiBaze * 1.8 : cmimiBaze;
        double kostoBagazhit = bagazhi.llogaritKostonShtese();
        double zbritja = perdorZbritjePikesh ? 20.0 : 0.0;

        this.shumaTotale = Math.max(10.0, cmimiKlases + kostoBagazhit - zbritja);
        this.ePaguar = false;
    }

    public void kryejPagesen(String menyraPageses) {
        this.menyraPageses = menyraPageses;
        this.ePaguar = true;
    }

    public double getShumaTotale() { return shumaTotale; }
    public boolean isEPaguar() { return ePaguar; }
    public String getMenyraPageses() { return menyraPageses; }
}