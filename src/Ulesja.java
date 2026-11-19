public class Ulesja {
    private String numriUleses;
    private String klasa;
    private boolean eshteEZene;

    public Ulesja(String numriUleses, String klasa) {
        this.numriUleses = numriUleses;
        this.klasa = klasa;
        this.eshteEZene = false;
    }

    public String getNumriUleses() { return numriUleses; }
    public String getKlasa() { return klasa; }
    public boolean isEshteEZene() { return eshteEZene; }
    public void setEshteEZene(boolean eshteEZene) { this.eshteEZene = eshteEZene; }
}