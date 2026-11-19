import java.util.ArrayList;

public class Pasagjeri extends Personi {
    private String numriPasaportes;
    private int piketBesnikerise;
    private ArrayList<Bileta> biletat;

    public Pasagjeri(String emri, String mbiemri, String numriPersonal, String numriPasaportes) {
        super(emri, mbiemri, numriPersonal);
        this.numriPasaportes = numriPasaportes;
        this.piketBesnikerise = 0;
        this.biletat = new ArrayList<>();
    }

    public void shtoBilete(Bileta bileta) {
        biletat.add(bileta);
        piketBesnikerise += 50;
    }

    public void hiqBilete(Bileta bileta) {
        biletat.remove(bileta);
    }

    public ArrayList<Bileta> getBiletat() {
        return biletat;
    }

    public String getNumriPasaportes() {
        return numriPasaportes;
    }

    public int getPiketBesnikerise() {
        return piketBesnikerise;
    }

    public void perdorPiket(int sasia) {
        if (piketBesnikerise >= sasia) {
            piketBesnikerise -= sasia;
        }
    }
}