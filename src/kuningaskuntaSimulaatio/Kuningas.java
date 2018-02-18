package kuningaskuntaSimulaatio;

import java.util.*;

public class Kuningas {
	@SuppressWarnings("unused")
	private String nimi;
	private int raha;
	private int ruoka;
	private int rahaTuotto;
	private int ruokaTuotto;
	private int vuorot;
	private int sukujenLKM;
	private boolean havitty = false;
	@SuppressWarnings("unused")
	private Scanner vastaus = new Scanner(System.in);
	public ArrayList<Ongelma> ongelmat = new ArrayList<Ongelma>();
	public ArrayList<Suku> suvut = new ArrayList<Suku>();
	public ArrayList<String> maalaisnimet = new ArrayList<>(
			Arrays.asList("Kuokka", "Vilja", "Vihre�", "Lihava", "Pelto", "Aura", "Terve", "Jussi"));
	public ArrayList<String> sotilasnimet = new ArrayList<>(Arrays.asList("Miekka", "Kilpi", "Rohkea", "Keih�s",
			"Soturi", "Tappava", "Nuoli", "L�vist�v�", "Verinen", "Lalli", "Punainen"));
	public ArrayList<String> uskontonimet = new ArrayList<>(Arrays.asList("Risti", "Pyh�", "Loistava", "Siunattu",
			"Jeesus", "Jumalan", "Hymni", "Synagoga", "Protestantti", "Sutra", "Valkoinen"));
	public ArrayList<String> kauppiasnimet = new ArrayList<>(Arrays.asList("Kultainen", "Varakas", "Kolikko", "�veri�s",
			"Roope", "Tuote", "Lent�v�", "K�rryt", "Lompakko"));
	public ArrayList<String> magianimet = new ArrayList<>(Arrays.asList("Lohik��rme", "Salamoiva", "Liekehtiv�",
			"Feenix", "Potter", "Milla", "Sauva", "Alkemisti", "Haltija"));

	public Kuningas(String nimi, int vuorot) {
		Random r = new Random();
		this.nimi = nimi;
		this.raha = r.nextInt(10) + 90;
		this.ruoka = r.nextInt(10) + 90;
		this.rahaTuotto = 2;
		this.ruokaTuotto = 2;
		this.vuorot = vuorot;
		this.sukujenLKM = 25;

		//luodaan suvut
		for (int i = 0; i < sukujenLKM - 2; i++) {
			lisaaSuku();
		}
		lisaaAatelisSuku();
		lisaaAatelisSuku();
		
		generoiSukuSuhteet();
		
	}



	public int annaSukujenLKM() {
		return sukujenLKM;
	}

	public void vuorokierto() { // Koko peli t��ll�
		laskePisteet();
		for (int i = 0; i < vuorot; i++) {
			Random r = new Random();
			int x = r.nextInt(ongelmat.size());
			Ongelma vuoronOngelma = ongelmat.get(x);
			vuoronOngelma.tulosta(this);
			vuoronOngelma.valitsePaatos(vastaus.nextInt(),this);
			laskePisteet();
			if (havitty)
				break;
		}
		tulostaPisteet();
	}

	/*
	 * private Paatos kysyPaatos(){ return new Paatos(); }
	 */

	private void laskePisteet() {
		this.raha += this.rahaTuotto;
		this.ruoka += this.ruokaTuotto;
		if (ruoka < 1 || raha < 1) {
			System.out.println("Resurssisi loppuivat ja kuningaskuntasi vajosi anarkiaan.");
			havitty = true;
		} else
			System.out.println("Sinulla on " + this.raha + " kulta(a) ((+)" + this.rahaTuotto + ") ja " + this.ruoka
					+ " ruoka(a) per kk ((+)" + this.ruokaTuotto + ").");
	}

	// pelin lopetus
	private void tulostaPisteet() {
		System.out.println("Valtakautesi on p��ttynyt.");
	}

	@SuppressWarnings("unused")
	private void generoiNimi(Suku nimettava) {
		nimettava.asetaNimi("randomnimi");
	}
	
	
	private void generoiSukuSuhteet() {
		Random r = new Random();
		for ( Suku muokattava : suvut ) {
			for ( Suku suhdeKohde : suvut) {
				if ( !muokattava.equals(suhdeKohde) ) {
					muokattava.asetaSuhdeSukuun(r.nextInt(200)-100, suhdeKohde);
				}
			}
		}
	}
	
	private void lisaaSuku() {
		Random r = new Random();
		int[] tyyppi = new int[5];
		tyyppi[r.nextInt(4)] += r.nextInt(1) + 1;
		tyyppi[r.nextInt(4)] += r.nextInt(1) + 1;
		// haetaan tyyppiin kuuluva nimi
		String lisattavaNimi = "";
		for (int j = 0; j < tyyppi.length; j++) {
			if (tyyppi[j] > 0) {
				if (j==0) {
					lisattavaNimi += magianimet.get(r.nextInt(magianimet.size()));
				}
				if (j==1) {
					lisattavaNimi += sotilasnimet.get(r.nextInt(sotilasnimet.size()));
				}
				if (j==2) {
					lisattavaNimi += uskontonimet.get(r.nextInt(uskontonimet.size()));
				}
				if (j==3) {
					lisattavaNimi += kauppiasnimet.get(r.nextInt(kauppiasnimet.size()));
				}
				if (j==4) {
					lisattavaNimi += maalaisnimet.get(r.nextInt(maalaisnimet.size()));
				}
			}
		}
		suvut.add(new Suku());
		Suku lisattava = suvut.get(suvut.size()-1);
		lisattava.asetaNimi(lisattavaNimi);
		lisattava.asetaPopulaatio(r.nextInt(30) + 20);
		lisattava.asetaAatelisuus(0);
		
		lisattava.asetaMagia(tyyppi[0]);	
		lisattava.asetaSotilaallinen(tyyppi[1]);
		lisattava.asetaUskonnollinen(tyyppi[2]);	
		lisattava.asetaKauppias(tyyppi[3]);
		lisattava.asetaMaalainen(tyyppi[4]);	
	}
	private void lisaaAatelisSuku() {
		Random r = new Random();
		suvut.add(new Suku());
		Suku lisattava = suvut.get(suvut.size()-1);
		lisattava.asetaAatelisuus(r.nextInt(2) + 2);
		
		lisattava.asetaMagia(0);	
		lisattava.asetaSotilaallinen(0);
		lisattava.asetaUskonnollinen(0);	
		lisattava.asetaKauppias(0);
		lisattava.asetaMaalainen(0);	
	}
	public int annaRaha() {
		return raha;
	}
	public int annaRuoka() {
		return ruoka;
	}
	public void asetaRaha(int asetettavaRahamaara) {
		this.raha = asetettavaRahamaara;
	}
	public void asetaRuoka(int asetettavaRuoka) {
		this.ruoka = asetettavaRuoka;
	}
	public void tulostaSuvut() {
		for (Suku tulostettava : suvut) {
			System.out.println(tulostettava);
		}
	}

	public int annaRahaTuotto() {
		return this.rahaTuotto;
	}

	public void asetaRahaTuotto(int rahaTuotto) {
		this.rahaTuotto = rahaTuotto;
	}
	
	public int annaRuokaTuotto() {
		return this.ruokaTuotto;
	}
	
	public void asetaRuokaTuotto(int ruokaTuotto) {
		this.ruokaTuotto = ruokaTuotto;
	}
}