public class Piloti extends Personi {
    private String licenca;
    private int oretEFluturimit;
    private String grada;

    public Piloti(String emri, String mbiemri, String numriPersonal, String licenca, int oretEFluturimit, String grada) {
        super(emri, mbiemri, numriPersonal);
        this.licenca = licenca;
        this.oretEFluturimit = oretEFluturimit;
        this.grada = grada;
    }

    public String getGradaDheEmri() {
        return grada + " " + getEmriPlote() + " (Licenca: " + licenca + ")";
    }

    public int getOretEFluturimit() {
        return oretEFluturimit;
    }
}