import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SistemiRezervimeve {
    private ArrayList<Aeroporti> aeroportet;
    private ArrayList<Avioni> flota;
    private ArrayList<Piloti> pilotet;
    private ArrayList<Fluturimi> fluturimet;
    private ArrayList<Pasagjeri> pasagjeret;
    private ArrayList<Bileta> teGjithaBiletat;

    public SistemiRezervimeve() {
        aeroportet = new ArrayList<>();
        flota = new ArrayList<>();
        pilotet = new ArrayList<>();
        fluturimet = new ArrayList<>();
        pasagjeret = new ArrayList<>();
        teGjithaBiletat = new ArrayList<>();
        ngarkoTeDhenatFillestare();
    }

    private void ngarkoTeDhenatFillestare() {
        Aeroporti prn = new Aeroporti("PRN", "Adem Jashari", "Prishtinë", "Kosovë");
        Aeroporti tia = new Aeroporti("TIA", "Nënë Tereza", "Tiranë", "Shqipëri");
        Aeroporti ist = new Aeroporti("IST", "Istanbul Airport", "Stamboll", "Turqi");
        Aeroporti vie = new Aeroporti("VIE", "Vienna Intl", "Vjenë", "Austri");
        Aeroporti lhr = new Aeroporti("LHR", "Heathrow", "Londër", "Britani");
        Aeroporti zrh = new Aeroporti("ZRH", "Zurich Airport", "Cyrih", "Zvicër");
        Aeroporti jfk = new Aeroporti("JFK", "John F. Kennedy", "Nju Jork", "SHBA");
        Aeroporti cdg = new Aeroporti("CDG", "Charles de Gaulle", "Paris", "Francë");
        Aeroporti fco = new Aeroporti("FCO", "Fiumicino", "Romë", "Itali");
        Aeroporti mxp = new Aeroporti("MXP", "Malpensa", "Milano", "Itali");
        Aeroporti mad = new Aeroporti("MAD", "Barajas", "Madrid", "Spanjë");
        Aeroporti bcn = new Aeroporti("BCN", "El Prat", "Barcelonë", "Spanjë");
        Aeroporti fra = new Aeroporti("FRA", "Frankfurt Main", "Frankfurt", "Gjermani");
        Aeroporti muc = new Aeroporti("MUC", "Franz Josef Strauss", "Mynih", "Gjermani");
        Aeroporti ber = new Aeroporti("BER", "Brandenburg", "Berlin", "Gjermani");
        Aeroporti ams = new Aeroporti("AMS", "Schiphol", "Amsterdam", "Holandë");
        Aeroporti bru = new Aeroporti("BRU", "Brussels Airport", "Bruksel", "Belgjikë");
        Aeroporti dxb = new Aeroporti("DXB", "Dubai Intl", "Dubai", "EBA");
        Aeroporti doh = new Aeroporti("DOH", "Hamad Intl", "Doha", "Katar");
        Aeroporti ath = new Aeroporti("ATH", "Eleftherios Venizelos", "Athinë", "Greqi");
        Aeroporti skg = new Aeroporti("SKG", "Makedonia", "Selanik", "Greqi");
        Aeroporti dbv = new Aeroporti("DBV", "Dubrovnik Airport", "Dubrovnik", "Kroaci");
        Aeroporti zag = new Aeroporti("ZAG", "Franjo Tuđman", "Zagreb", "Kroaci");
        Aeroporti lju = new Aeroporti("LJU", "Jože Pučnik", "Lubjanë", "Slloveni");
        Aeroporti bud = new Aeroporti("BUD", "Ferenc Liszt", "Budapest", "Hungari");
        Aeroporti prg = new Aeroporti("PRG", "Václav Havel", "Pragë", "Çeki");
        Aeroporti waw = new Aeroporti("WAW", "Chopin Airport", "Varshavë", "Poloni");
        Aeroporti arn = new Aeroporti("ARN", "Arlanda", "Stokholm", "Suedi");
        Aeroporti osl = new Aeroporti("OSL", "Gardermoen", "Oslo", "Norvegji");
        Aeroporti cph = new Aeroporti("CPH", "Kastrup", "Kopenhagë", "Danimarkë");
        Aeroporti lis = new Aeroporti("LIS", "Humberto Delgado", "Lisbonë", "Portugali");
        Aeroporti sin = new Aeroporti("SIN", "Changi Airport", "Singapor", "Singapor");
        Aeroporti hnd = new Aeroporti("HND", "Haneda Airport", "Tokio", "Japoni");

        Aeroporti[] listaAeroporteve = {
            prn, tia, ist, vie, lhr, zrh, jfk, cdg, fco, mxp, mad, bcn,
            fra, muc, ber, ams, bru, dxb, doh, ath, skg, dbv, zag, lju,
            bud, prg, waw, arn, osl, cph, lis, sin, hnd
        };
        for (Aeroporti a : listaAeroporteve) aeroportet.add(a);

        Avioni b737 = new Avioni("B738", "Boeing 737-800 Pink Edition", 162, 18);
        Avioni a320 = new Avioni("A320", "Airbus A320neo Rose", 162, 18);
        Avioni b787 = new Avioni("B789", "Boeing 787 Dreamliner VIP", 162, 18);
        flota.add(b737); flota.add(a320); flota.add(b787);

        Piloti p1 = new Piloti("Kapt. Arben Krasniqi", "LIC-901", null, null, 0, null);
        Piloti p2 = new Piloti("Kapt. Elena Berisha", "LIC-902", null, null, 0, null);
        pilotet.add(p1); pilotet.add(p2);

        fluturimet.add(new Fluturimi("CM-101", prn, ist, "2026-12-15 | 08:30 → 11:15 (2h 45m)", a320, p1, 69.0, "Gate A1"));
        fluturimet.add(new Fluturimi("CM-102", prn, vie, "2026-12-15 | 09:45 → 11:25 (1h 40m)", b737, p2, 59.0, "Gate A2"));
        fluturimet.add(new Fluturimi("CM-103", prn, zrh, "2026-12-15 | 11:20 → 13:30 (2h 10m)", a320, p1, 89.0, "Gate A3"));
        fluturimet.add(new Fluturimi("CM-104", prn, fra, "2026-12-15 | 13:10 → 15:35 (2h 25m)", b737, p2, 95.0, "Gate A4"));
        fluturimet.add(new Fluturimi("CM-105", prn, muc, "2026-12-15 | 15:00 → 16:55 (1h 55m)", a320, p1, 82.0, "Gate B1"));
        fluturimet.add(new Fluturimi("CM-106", prn, ber, "2026-12-16 | 07:15 → 09:40 (2h 25m)", b737, p2, 74.0, "Gate B2"));
        fluturimet.add(new Fluturimi("CM-107", prn, lhr, "2026-12-16 | 10:30 → 12:45 (3h 15m)", a320, p1, 119.0, "Gate B3"));
        fluturimet.add(new Fluturimi("CM-108", prn, cdg, "2026-12-16 | 14:15 → 16:50 (2h 35m)", b737, p2, 109.0, "Gate C1"));
        fluturimet.add(new Fluturimi("CM-109", prn, mxp, "2026-12-16 | 17:40 → 19:25 (1h 45m)", a320, p1, 54.0, "Gate C2"));
        fluturimet.add(new Fluturimi("CM-110", prn, fco, "2026-12-17 | 08:00 → 09:40 (1h 40m)", b737, p2, 64.0, "Gate A1"));
        fluturimet.add(new Fluturimi("CM-111", prn, ams, "2026-12-17 | 12:10 → 14:50 (2h 40m)", a320, p1, 112.0, "Gate A5"));
        fluturimet.add(new Fluturimi("CM-112", prn, bru, "2026-12-17 | 16:25 → 19:00 (2h 35m)", b737, p2, 98.0, "Gate B4"));
        fluturimet.add(new Fluturimi("CM-113", prn, arn, "2026-12-18 | 09:10 → 12:20 (3h 10m)", a320, p1, 105.0, "Gate C3"));
        fluturimet.add(new Fluturimi("CM-114", prn, osl, "2026-12-18 | 13:45 → 17:05 (3h 20m)", b737, p2, 118.0, "Gate C4"));
        fluturimet.add(new Fluturimi("CM-115", prn, cph, "2026-12-18 | 18:20 → 21:00 (2h 40m)", a320, p1, 92.0, "Gate A2"));
        fluturimet.add(new Fluturimi("CM-201", tia, fco, "2026-12-15 | 07:50 → 09:15 (1h 25m)", b737, p1, 49.0, "Gate D1"));
        fluturimet.add(new Fluturimi("CM-202", tia, mxp, "2026-12-15 | 10:15 → 12:00 (1h 45m)", a320, p2, 45.0, "Gate D2"));
        fluturimet.add(new Fluturimi("CM-203", tia, bcn, "2026-12-16 | 11:40 → 14:15 (2h 35m)", b737, p1, 88.0, "Gate D3"));
        fluturimet.add(new Fluturimi("CM-204", tia, mad, "2026-12-16 | 15:30 → 18:45 (3h 15m)", a320, p2, 115.0, "Gate D4"));
        fluturimet.add(new Fluturimi("CM-205", tia, ath, "2026-12-17 | 08:40 → 10:55 (1h 15m)", b737, p1, 52.0, "Gate E1"));
        fluturimet.add(new Fluturimi("CM-206", tia, skg, "2026-12-17 | 14:00 → 15:50 (50m)", a320, p2, 48.0, "Gate E2"));
        fluturimet.add(new Fluturimi("CM-207", tia, dbv, "2026-12-18 | 09:30 → 10:25 (55m)", b737, p1, 44.0, "Gate E3"));
        fluturimet.add(new Fluturimi("CM-208", tia, zag, "2026-12-18 | 12:50 → 14:10 (1h 20m)", a320, p2, 62.0, "Gate E4"));
        fluturimet.add(new Fluturimi("CM-209", tia, lju, "2026-12-19 | 10:00 → 11:25 (1h 25m)", b737, p1, 66.0, "Gate F1"));
        fluturimet.add(new Fluturimi("CM-210", tia, bud, "2026-12-19 | 13:20 → 14:55 (1h 35m)", a320, p2, 58.0, "Gate F2"));
        fluturimet.add(new Fluturimi("CM-211", tia, prg, "2026-12-19 | 16:45 → 18:40 (1h 55m)", b737, p1, 77.0, "Gate F3"));
        fluturimet.add(new Fluturimi("CM-212", tia, waw, "2026-12-20 | 08:15 → 10:25 (2h 10m)", a320, p2, 79.0, "Gate F4"));
        fluturimet.add(new Fluturimi("CM-301", ist, dxb, "2026-12-20 | 21:30 → 02:55 (4h 25m)", b787, p1, 210.0, "Gate G1"));
        fluturimet.add(new Fluturimi("CM-302", ist, doh, "2026-12-20 | 22:15 → 02:35 (4h 20m)", b787, p2, 195.0, "Gate G2"));
        fluturimet.add(new Fluturimi("CM-303", cdg, lis, "2026-12-21 | 11:00 → 12:35 (2h 35m)", a320, p1, 84.0, "Gate H1"));
        fluturimet.add(new Fluturimi("CM-304", zrh, jfk, "2026-12-21 | 13:00 → 16:15 (9h 15m)", b787, p2, 390.0, "Gate H2"));
        fluturimet.add(new Fluturimi("CM-305", dxb, sin, "2026-12-22 | 03:15 → 14:40 (7h 25m)", b787, p1, 440.0, "Gate H3"));
        fluturimet.add(new Fluturimi("CM-306", dxb, hnd, "2026-12-22 | 02:50 → 17:20 (9h 30m)", b787, p2, 520.0, "Gate H4"));
    }

    public Fluturimi gjejFluturim(String numri) {
        for (Fluturimi f : fluturimet) {
            if (f.getNumriFluturimit().equalsIgnoreCase(numri.trim())) return f;
        }
        return null;
    }

    public double merrCmiminFillestarPerQytet(String kodiIATA) {
        double min = Double.MAX_VALUE;
        for (Fluturimi f : fluturimet) {
            if (f.getMberritja().getKodiIATA().equalsIgnoreCase(kodiIATA) || f.getNisja().getKodiIATA().equalsIgnoreCase(kodiIATA)) {
                if (f.getCmimiDinamik() < min) min = f.getCmimiDinamik();
            }
        }
        return (min == Double.MAX_VALUE) ? 65.0 : min;
    }

    public Pasagjeri gjejOseKrijoPasagjer(String emri, String mbiemri, String nrPersonal, String nrPasaportes) {
        for (Pasagjeri p : pasagjeret) {
            if (p.getNumriPersonal().equalsIgnoreCase(nrPersonal.trim())) return p;
        }
        Pasagjeri iRi = new Pasagjeri(emri, mbiemri, nrPersonal, nrPasaportes);
        pasagjeret.add(iRi);
        return iRi;
    }

    public Bileta rezervoBileteKomplete(Pasagjeri p, Fluturimi f, Ulesja u, double peshaBagazhit,
                                        String paketa, String sherbimet, double kostoShtese,
                                        String menyraPageses, boolean kaPromo) {
        if (u == null || u.isEshteEZene()) return null;

        double cmimiBaze = f.getCmimiDinamik();
        if (u.getKlasa().equalsIgnoreCase("Business")) cmimiBaze += 45.0;

        double totali = cmimiBaze + kostoShtese;
        if (kaPromo) totali = totali * 0.90;

        String pnr = "CM" + (1000 + new Random().nextInt(9000));
        Bagazhi bagazhi = new Bagazhi(peshaBagazhit);
        Pagesa pagesa = new Pagesa(totali, menyraPageses, bagazhi, kaPromo);

        Bileta bileta = new Bileta(pnr, p, f, u, bagazhi, pagesa, paketa, sherbimet);
        teGjithaBiletat.add(bileta);
        return bileta;
    }

    public Bileta gjejBileteMePNR(String pnr) {
        if (pnr == null) return null;
        String kerkuar = pnr.trim().replaceAll("\\s+", "").toUpperCase();
        for (Bileta b : teGjithaBiletat) {
            if (b.isEshteAktiv()) {
                String kodiBiletes = b.getKodiRezervimit().trim().replaceAll("\\s+", "").toUpperCase();
                if (kodiBiletes.equals(kerkuar)) {
                    return b;
                }
            }
        }
        return null;
    }

    public Bileta gjejBiletePerCheckIn(String pnr, String mbiemri) {
        Bileta b = gjejBileteMePNR(pnr);
        if (b == null) return null;
        if (mbiemri == null || mbiemri.trim().isEmpty()) return b;
        String emriPlote = b.getPasagjeri().getEmriPlote().toLowerCase();
        if (emriPlote.contains(mbiemri.trim().toLowerCase())) {
            return b;
        }
        return null;
    }

    public Bileta merrBiletenEFundit() {
        for (int i = teGjithaBiletat.size() - 1; i >= 0; i--) {
            if (teGjithaBiletat.get(i).isEshteAktiv()) {
                return teGjithaBiletat.get(i);
            }
        }
        return null;
    }

    public boolean anuloRezervimin(String pnr) {
        Bileta b = gjejBileteMePNR(pnr);
        if (b != null) {
            b.anuloBileten();
            return true;
        }
        return false;
    }

    public boolean shtoFluturimTeRi(String nr, Aeroporti nisja, Aeroporti mberritja, String dataOra,
                                    Avioni avioni, Piloti piloti, double cmimi, String porta) {
        if (gjejFluturim(nr) != null) return false;
        fluturimet.add(new Fluturimi(nr, nisja, mberritja, dataOra, avioni, piloti, cmimi, porta));
        return true;
    }

    public double llogaritTeArdhuratTotale() {
        double totali = 0;
        for (Bileta b : teGjithaBiletat) {
            if (b.isEshteAktiv()) totali += b.getPagesa().getShumaTotale();
        }
        return totali;
    }

    public void eksportoRaportin() {
        try (FileWriter fw = new FileWriter("raporti_kompanise.txt")) {
            fw.write("=== RAPORTI ZYRTAR COPA AIR PINK OMBRÉ LUXURY EDITION ===\n");
            fw.write("Destinacione: " + aeroportet.size() + " | Fluturime: " + fluturimet.size() + "\n");
            fw.write("Të ardhurat totale: " + String.format("%.2f", llogaritTeArdhuratTotale()) + " EUR\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Aeroporti> getAeroportet() { return aeroportet; }
    public ArrayList<Avioni> getFlota() { return flota; }
    public ArrayList<Piloti> getPilotet() { return pilotet; }
    public ArrayList<Fluturimi> getFluturimet() { return fluturimet; }
    public ArrayList<Bileta> getTeGjithaBiletat() { return teGjithaBiletat; }
}