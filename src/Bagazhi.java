public class Bagazhi {
    private double peshaKg;
    private static final double LIMITI_FALAS = 20.0;
    private static final double TARIFA_PER_KG_SHTESE = 5.0;

    public Bagazhi(double peshaKg) {
        this.peshaKg = peshaKg;
    }

    public double llogaritKostonShtese() {
        if (peshaKg <= LIMITI_FALAS) {
            return 0.0;
        }
        return (peshaKg - LIMITI_FALAS) * TARIFA_PER_KG_SHTESE;
    }

    public double getPeshaKg() {
        return peshaKg;
    }
}