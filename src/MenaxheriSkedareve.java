import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MenaxheriSkedareve {
    private static final String EMRI_SKEDARIT = "rezervimet.txt";

    public void ruajBiletenNeSkedar(Bileta b) {
        try (PrintWriter out = new PrintWriter(new FileWriter(EMRI_SKEDARIT, true))) {
            out.println("PNR: " + b.getKodiRezervimit() +
                        " | Pasagjeri: " + b.getPasagjeri().getEmriPlote() +
                        " | Fluturimi: " + b.getFluturimi().getNumriFluturimit() +
                        " | Ulesja: " + b.getUlesja().getNumriUleses() +
                        " | Paguar: " + b.getPagesa().getShumaTotale() + " EUR");
        } catch (IOException e) {
            System.out.println("Gabim gjatë ruajtjes në skedar: " + e.getMessage());
        }
    }

    public void eksportoTeGjithaFluturimet(ArrayList<Fluturimi> fluturimet) {
        try (PrintWriter out = new PrintWriter(new FileWriter("fluturimet_raport.txt"))) {
            for (Fluturimi f : fluturimet) {
                out.println(f.getNumriFluturimit() + ": " + f.getNisja().getKodiIATA() +
                            " -> " + f.getMberritja().getKodiIATA() + " | Data: " + f.getDataOraNisjes());
            }
        } catch (IOException e) {
            System.out.println("Gabim gjatë eksportimit: " + e.getMessage());
        }
    }
}