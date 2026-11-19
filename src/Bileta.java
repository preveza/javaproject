import java.util.UUID;

public class Bileta {
    private String kodiRezervimit;
    private Pasagjeri pasagjeri;
    private Fluturimi fluturimi;
    private Ulesja ulesja;
    private Bagazhi bagazhi;
    private Pagesa pagesa;

    public Bileta(Pasagjeri pasagjeri, Fluturimi fluturimi, Ulesja ulesja, Bagazhi bagazhi, Pagesa pagesa) {
        this.kodiRezervimit = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.pasagjeri = pasagjeri;
        this.fluturimi = fluturimi;
        this.ulesja = ulesja;
        this.bagazhi = bagazhi;
        this.pagesa = pagesa;
    }

    public String getKodiRezervimit() { return kodiRezervimit; }
    public Ulesja getUlesja() { return ulesja; }
    public Pasagjeri getPasagjeri() { return pasagjeri; }
    public Fluturimi getFluturimi() { return fluturimi; }
    public Pagesa getPagesa() { return pagesa; }
    
    
    private boolean checkInKryer = false;

    public void kryejCheckIn() {
        this.checkInKryer = true;
    }

    public boolean isCheckInKryer() {
        return checkInKryer;
    }

	public boolean isEshteAktiv() {
		// TODO Auto-generated method stub
		return false;
	}

	public void anuloBileten() {
		// TODO Auto-generated method stub
		
	}
}
