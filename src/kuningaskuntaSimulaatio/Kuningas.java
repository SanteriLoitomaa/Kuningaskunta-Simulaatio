package kuningaskuntaSimulaatio;

import java.io.Serializable;
import java.util.*;

public class Kuningas implements Serializable{
	private static final long serialVersionUID = 1L;
	private String nimi;
	private int raha;
	private int ruoka;
	private int rahaTuotto;
	private int ruokaTuotto;
	private int vuorot;
	private int sukujenLKM;
	private boolean havitty = false;
	transient private Scanner vastaus;
	public ArrayList<Ongelma> ongelmat = new ArrayList<Ongelma>();
	public ArrayList<Suku> suvut = new ArrayList<Suku>();
	public ArrayList<String> maalaisnimet = new ArrayList<>(Arrays.asList("Kuokka", "Vilja", "Vihre�", "Lihava",
			"Pelto", "Aura", "Terve", "Jussi"));
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
		this.vuorot = vuorot;
		this.raha = r.nextInt(10) + 90;
		this.ruoka = r.nextInt(10) + 90;
		this.rahaTuotto = 2;
		this.ruokaTuotto = 2;
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
	
	public String annaNimi() {
		return nimi;
	}

	public void vuorokierto() { // Koko peli t��ll�
		laskePisteet(false);
		for (int i = 0; i < vuorot; i++) {
			Random r = new Random();
			int x = r.nextInt(ongelmat.size());
			Ongelma vuoronOngelma = ongelmat.get(x);
			vuoronOngelma.tulosta(this);
			System.out.print("P��t�ksesi numero on: ");
			String s = "";
			while(true) {
				s = vastaus.next();
				if(vuoronOngelma.onSallittu(s)) {
					vuoronOngelma.valitsePaatos(Integer.parseInt(s),this);
					break;
				}
			}
			laskePisteet(true);
			if (havitty)
				break;
			else {
				TallennaLataaPisteet.tallenna(this);
				System.out.println("Haluatko Jatkaa?");
				System.out.println("1. Kyll�");
				System.out.println("2. En");
				while (!vastaus.hasNextInt()) {
					vastaus.next();
				}
				int vast = vastaus.nextInt();
				if(vast == 2) {
					System.exit(1);
				}
			}
		}
		tulostaPisteet();
	}
	// Laskee vallankumouksen mahdin kun se iskee
	public int coupDeTat() { 
		int vihamiehet = 0;
		for (int i=0; i<suvut.size();i++) {
			if(suvut.get(i).annaSuhdeKuninkaaseen() < -50) {
				if(suvut.get(i).annaSotilaallinen() >0) {
					vihamiehet += suvut.get(i).annaSotilaallinen();
				}else {
					vihamiehet++;
				}
			}
		}
		vihamiehet += -5;
		return vihamiehet;
	}
	//Laskee sinua puolustavat joukot
	public int sotilasmahti() {
		int joukot = 0;
		for (int i=0; i<suvut.size();i++) {
			joukot += suvut.get(i).annaSotilaallinen();
		}
		return joukot;
	}

	private void laskePisteet(Boolean lisaaPisteet) {
		if(lisaaPisteet) {
			this.raha += this.rahaTuotto;
			this.ruoka += this.ruokaTuotto;
		}
		if (ruoka < 1 || raha < 1 || coupDeTat() > sotilasmahti()) { //Vallankaappaush�vi� pit�� ottaa edelliseen ifiin.
			System.out.println("Resurssisi loppuivat ja kuningaskuntasi vajosi anarkiaan.");
			havitty = true;
		} else
			System.out.println("Sinulla on " + this.raha + " kulta(a) ((+)" + this.rahaTuotto + ") ja " + this.ruoka
					+ " ruoka(a) per kk ((+)" + this.ruokaTuotto + ").");
	}

	// pelin lopetus
	private void tulostaPisteet() {
		System.out.println("Valtakautesi on p��ttynyt.");
		double pisteet = 0;
		pisteet += annaRaha();
		pisteet += annaRuoka();
		pisteet += annaRahaTuotto()*10;
		pisteet += annaRuokaTuotto()*10;
		for (int i=0;i<suvut.size();i++) {
			if (suvut.get(i).annaAatelisuus() > 0) {
				pisteet += suvut.get(i).annaSuhdeKuninkaaseen()*10;
			}else if (suvut.get(i).annaSuhdeKuninkaaseen() > 0) {
				pisteet += suvut.get(i).annaSuhdeKuninkaaseen()*3;
			}else {
				pisteet += suvut.get(i).annaSuhdeKuninkaaseen();
			}
		}
		if (havitty) {
			pisteet = pisteet*0.2;
		}
		int truPisteet = (int)pisteet;
		TallennaLataaPisteet.lisaaPisteet(truPisteet, nimi);
		TallennaLataaPisteet.tulostaPisteet();
		/*
		 jos valtakunta hajosi pisteist� pois 80%
		 Sukujen v�liset tyytyv�isyyssuhteet summataan kesken��n, positiivisten tulee painaa negatiivisia
		 enemm�n.
		 Aatelien tyytyv�isyys faktoroidaan*10
		 Kokonais ruokasi ja rahasi lasketaan mukaan pisteisiin. Tuotot*10 pisteisiin my�s.
		 */
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
	
	//Luodaan suku
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
		lisattava.asetaSuhdeKuninkaaseen(r.nextInt(20)-10);
		
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
		lisattava.asetaSuhdeKuninkaaseen(r.nextInt(5)+5);
		
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
	
	public Suku annaAatelisin() {
		int aatelisuus = 0;
		Suku aatelisin = this.suvut.get(0);
		for(Suku s : this.suvut) {
			if(s.annaAatelisuus() > 0) {
				aatelisuus = s.annaAatelisuus();
				aatelisin = s;
				break;
			}
		}
		for(Suku s : this.suvut) {
			if(s.annaAatelisuus() > aatelisuus) {
				aatelisin = s;
			}
		}
		return aatelisin;
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

	public void scan(Scanner vastaus) {
		this.vastaus = vastaus;
	}
	
	/*
	 * Palauttaa kaikki suvut, jotka on merkitty boolean parametreihin ja järjestää ne
	 * suosituimmuusjärjestykseen
	 */
	ArrayList<Suku> etsiSukuTyypit(boolean magia, boolean sotilas, boolean uskonnollinen, boolean kauppias, boolean maalainen){
		ArrayList<Suku> palautettava = new ArrayList<Suku>();
		
		//Lisätään suvut joita kysytään
		for ( Suku tarkasteltava : suvut ) {
			if ( magia && tarkasteltava.annaMagia() > 0 ) {
				palautettava.add(tarkasteltava);
			} else if (sotilas && tarkasteltava.annaSotilaallinen() > 0) {
				palautettava.add(tarkasteltava);
			} else if (uskonnollinen && tarkasteltava.annaUskonnollinen() > 0) {
				palautettava.add(tarkasteltava);
			} else if (kauppias && tarkasteltava.annaKauppias() > 0) {
				palautettava.add(tarkasteltava);
			} else if (maalainen && tarkasteltava.annaMaalainen() > 0) {
				palautettava.add(tarkasteltava);
			} 
		}
		
		//Järjestetään kun comparatori on päivitetty sukuun
		Collections.sort(palautettava); 
		
		return palautettava;
	}
	
	ArrayList<Suku> etsiAateliset() {
		ArrayList<Suku> palautettava = new ArrayList<Suku>();
		
		for ( Suku tarkasteltava : suvut) {
			if ( tarkasteltava.annaAatelisuus() > 0 ) {
				palautettava.add(tarkasteltava);
			}
		}
		
		Collections.sort(palautettava);
		
		return palautettava;
	}
}