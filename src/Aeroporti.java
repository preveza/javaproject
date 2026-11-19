public class Aeroporti {
    private String kodiIATA;
    private String emri;
    private String qyteti;
    private String shteti;

    public Aeroporti(String kodiIATA, String emri, String qyteti, String shteti) {
        this.kodiIATA = kodiIATA.toUpperCase();
        this.emri = emri;
        this.qyteti = qyteti;
        this.shteti = shteti;
    }

    public String getKodiIATA() { return kodiIATA; }
    public String getQyteti() { return qyteti; }

    @Override
    public String toString() {
        return qyteti + " (" + kodiIATA + ") - " + emri;
    }
}