public abstract class Personi {
    protected String emri;
    protected String mbiemri;
    protected String numriPersonal;

    public Personi(String emri, String mbiemri, String numriPersonal) {
        this.emri = emri;
        this.mbiemri = mbiemri;
        this.numriPersonal = numriPersonal;
    }

    public String getEmriPlote() {
        return emri + " " + mbiemri;
    }

    public String getNumriPersonal() {
        return numriPersonal;
    }
}