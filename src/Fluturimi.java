import java.util.ArrayList;

public class Fluturimi {
    private String numriFluturimit;
    private Aeroporti nisja;
    private Aeroporti mberritja;
    private String dataOraNisjes;
    private Avioni avioni;
    private Piloti piloti;
    private double cmimiBaze;
    private String portaNisjes;
    private ArrayList<Ulesja> uleset;

    public Fluturimi(String numriFluturimit, Aeroporti nisja, Aeroporti mberritja,
                     String dataOraNisjes, Avioni avioni, Piloti piloti,
                     double cmimiBaze, String portaNisjes) {
        this.numriFluturimit = numriFluturimit;
        this.nisja = nisja;
        this.mberritja = mberritja;
        this.dataOraNisjes = dataOraNisjes;
        this.avioni = avioni;
        this.piloti = piloti;
        this.cmimiBaze = cmimiBaze;
        this.portaNisjes = portaNisjes;
        this.uleset = new ArrayList<>();
        gjeneroUleset();
    }

    // Konstruktor rezervë nëse në ndonjë vend thirret pa portaNisjes
    public Fluturimi(String numriFluturimit, Aeroporti nisja, Aeroporti mberritja,
                     String dataOraNisjes, Avioni avioni, Piloti piloti, double cmimiBaze) {
        this(numriFluturimit, nisja, mberritja, dataOraNisjes, avioni, piloti, cmimiBaze, "Gate A1");
    }

    // Gjeneron kabinën e plotë me 30 rreshta (A-B-C | D-E-F = 180 ulëse)
    private void gjeneroUleset() {
        uleset.clear();
        String[] kolonat = {"A", "B", "C", "D", "E", "F"};
        for (int rreshti = 1; rreshti <= 30; rreshti++) {
            String klasa = (rreshti <= 3) ? "Business" : "Economy";
            for (String kol : kolonat) {
                uleset.add(new Ulesja(rreshti + kol, klasa));
            }
        }
    }

    public int numeroUlesetELira(String klasa) {
        int count = 0;
        for (Ulesja u : uleset) {
            if (!u.isEshteEZene() && u.getKlasa().equalsIgnoreCase(klasa)) {
                count++;
            }
        }
        return count;
    }

    public double getCmimiDinamik() {
        int teZena = 0;
        for (Ulesja u : uleset) {
            if (u.isEshteEZene()) teZena++;
        }
        double perqindja = (double) teZena / uleset.size();
        if (perqindja > 0.75) return cmimiBaze * 1.25;
        if (perqindja > 0.50) return cmimiBaze * 1.10;
        return cmimiBaze;
    }

    public Ulesja gjejUlesen(String numriUleses) {
        for (Ulesja u : uleset) {
            if (u.getNumriUleses().equalsIgnoreCase(numriUleses)) {
                return u;
            }
        }
        return null;
    }

    public String getNumriFluturimit() { return numriFluturimit; }
    public Aeroporti getNisja() { return nisja; }
    public Aeroporti getMberritja() { return mberritja; }
    public String getDataOraNisjes() { return dataOraNisjes; }
    public Avioni getAvioni() { return avioni; }
    public Piloti getPiloti() { return piloti; }
    public double getCmimiBaze() { return cmimiBaze; }
    public String getPortaNisjes() { return portaNisjes; }
    public ArrayList<Ulesja> getUleset() { return uleset; }

    @Override
    public String toString() {
        return numriFluturimit + ": " + nisja.getQyteti() + " -> " + mberritja.getQyteti() + " (" + dataOraNisjes + ")";
    }
}