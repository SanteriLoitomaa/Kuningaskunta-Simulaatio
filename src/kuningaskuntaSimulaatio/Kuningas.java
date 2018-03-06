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
	public ArrayList<String> maalaisnimet = new ArrayList<>(Arrays.asList("Kuokka", "Vilja", "Vihreä", "Lihava",
			"Pelto", "Aura", "Terve", "Jussi"));
	public ArrayList<String> sotilasnimet = new ArrayList<>(Arrays.asList("Miekka", "Kilpi", "Rohkea", "Keihäs",
			"Soturi", "Tappava", "Nuoli", "Lävistävä", "Verinen", "Lalli", "Punainen"));
	public ArrayList<String> uskontonimet = new ArrayList<>(Arrays.asList("Risti", "Pyhä", "Loistava", "Siunattu",
			"Jeesus", "Jumalan", "Hymni", "Synagoga", "Protestantti", "Sutra", "Valkoinen"));
	public ArrayList<String> kauppiasnimet = new ArrayList<>(Arrays.asList("Kultainen", "Varakas", "Kolikko", "Äveriäs",
			"Roope", "Tuote", "Lentävä", "Kärryt", "Lompakko"));
	public ArrayList<String> magianimet = new ArrayList<>(Arrays.asList("Lohikäärme", "Salamoiva", "Liekehtivä",
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

	public void vuorokierto() { // Koko peli täällä
		laskePisteet(false);
		for (int i = 0; i < vuorot; i++) {
			if (ongelmat.size() <1) {
				Kuningaskunta.meillaOnOngelmia(this);
			}
			Random r = new Random();
			int x = r.nextInt(ongelmat.size());
			Ongelma vuoronOngelma = ongelmat.get(x);
			vuoronOngelma.tulosta(this);
			String s = "";
			while(true) {
				System.out.print("Päätöksesi numero: ");
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
				System.out.println("1. Kyllä");
				System.out.println("2. En");
				while (!vastaus.hasNextInt()) {
					vastaus.next();
				}
				int vast = vastaus.nextInt();
				if(vast == 2) {
					System.exit(1);
				}
				ongelmat.remove(x);
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
					vihamiehet += 1 + suvut.get(i).annaSotilaallinen();
				}else {
					vihamiehet++;
				}
			}
		}
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
	//Palauttaa true jos kana haluaa vallankumouksen
	public boolean kumotaanko() {
		ArrayList<Suku> halukkaat = new ArrayList<Suku>();
		for (int i=0; i<suvut.size();i++) {
			if(suvut.get(i).annaSuhdeKuninkaaseen() < -50) {
				halukkaat.add(suvut.get(i));
			}
		}
		int maara = 0;
		for (int j=0; j<halukkaat.size();j++) {
			maara += halukkaat.get(j).annaPopulaatio();
		}
		if (halukkaat.size()>4 && maara >180) {
			return true;
		} else {
			return false;
		}
	}
	public ArrayList<Suku> kapinalliset(){ //Palauttaa listan kapinoineista suvuista
		ArrayList<Suku> kapinat = new ArrayList<Suku>();
		for (int i=0; i<suvut.size();i++) {
			if(suvut.get(i).annaSuhdeKuninkaaseen() < -50) {
				kapinat.add(suvut.get(i));
			}
		}
		return kapinat;
	}

	private void laskePisteet(Boolean lisaaPisteet) {
		if(lisaaPisteet) {
			this.raha += this.rahaTuotto;
			this.ruoka += this.ruokaTuotto;
		}
		if (ruoka < 1 || raha < 1) { //Resurssipulaloppu
			System.out.println("Resurssisi loppuivat ja kuningaskuntasi vajosi anarkiaan.");
			havitty = true;
		} else if (kumotaanko()) { //Vallankaappausloppu
			if (coupDeTat() > sotilasmahti()) {
				System.out.println("Kapiset kapinalliset ovat kylästyneet valtaasi, sinut karkotettiin maasta ja koko perheesi teloitettiin!");
				havitty = true;
			}else {
				System.out.println("Kansan tyytymättömyys nosti osan heistä kapinoimaan, onnistuimem kuitenkin kukistamaan heidät omalla armeijallamme. Osa kapinoitsijista toki kuoli taistelussa, eiköhän se opeta heille vallan merkityksen!");
				ArrayList<Suku> rankaistavat = kapinalliset();
				for (int uhri =0;uhri<rankaistavat.size();uhri++) {
					rankaistavat.get(uhri).asetaPopulaatio((int)(rankaistavat.get(uhri).annaPopulaatio()*0.8));
				}
			}
		}else System.out.println("Sinulla on " + this.raha + " kulta(a) ((+)" + this.rahaTuotto + ") ja " + this.ruoka
					+ " ruoka(a) per kk ((+)" + this.ruokaTuotto + ")."+
				"\n" + "Tämänhetkiset pisteesi ovat: " + annaPisteet());
	}

	public int annaPisteet() { //Laskee pelissä tämänhetkiset pisteet
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
		pisteet += pisteet*suhteellinenVakimaara();
		if (havitty) {
			pisteet = pisteet*0.2;
		}
		int truPisteet = (int)pisteet;
		return truPisteet;
	}
	
	// pelin lopetus
	private void tulostaPisteet() {
		System.out.println("Valtakautesi on päättynyt.");
		int truPisteet = annaPisteet();
		TallennaLataaPisteet.lisaaPisteet(truPisteet, nimi);
		TallennaLataaPisteet.tulostaPisteet();
		/*
		 jos valtakunta hajosi pisteistä pois 80%
		 Sukujen väliset tyytyväisyyssuhteet summataan keskenään, positiivisten tulee painaa negatiivisia
		 enemmän.
		 Aatelien tyytyväisyys faktoroidaan*10
		 Kokonais ruokasi ja rahasi lasketaan mukaan pisteisiin. Tuotot*10 pisteisiin myäs.
		 */
	}
	public double suhteellinenVakimaara() { //palauttaa doublen, joka on yli 1 jos väkilukua on oletusta enemmän ja alle 1 jos väkiluku on oletusta pienempi
		int perus = 25*35;
		int oikea = 0;
		for (int i=0; i<suvut.size();i++) {
			oikea += suvut.get(i).annaPopulaatio();
		}
		double kerroin = oikea/perus;
		return kerroin;
	}
	
	/**
	 * Asetetaan sukujenväliset suhteet satunnaiseksi
	 * 
	 * @author Pasi Toivanen
	 */
  
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
	
	/**
	 * Metodi joka luo uuden satunnaisen suvun kuninkaalle. Aatelissuvuille on oma metodi alempana.
	 * 
	 * @author Pasi Toivanen
	 */
	private void lisaaSuku() {
		Random r = new Random();
		int[] tyyppi = new int[5];
		tyyppi[r.nextInt(5)] += r.nextInt(1) + 1;
		tyyppi[r.nextInt(5)] += r.nextInt(1) + 1;
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
	
	/**
	 * Metodi joka luo uuden aatelissuvun kuninkaalle
	 * 
	 * @author Pasi Toivanen
	 */
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
	
	/**
	 * @author Pasi Toivanen
	 * @return palauttaa kaikista aatelisimman suvun (huom eri asia kuin suosituin aatelisin suku)
	 */
	
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
	
	/**
	 * Palauttaa ne suvut, joiden tyypistä löytyy jotain merkattuja true-arvoisia tyyppejä
	 * 
	 * @param magia
	 * @param sotilas
	 * @param uskonnollinen
	 * @param kauppias
	 * @param maalainen
	 * @return Palauttaa ArrayListana kaikki löydetyt suvut suosituimmuusjärjestyksessä
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
	
	/**
	 * @author Pasi Toivanen
	 * 
	 * Syötetään kahteen parametriin arvo true (muihin false) jonka jälkeen palautetaan leikkaus
	 * hauista: esimerkiksi (true,false,false,true,false)
	 * etsiSukuTyypit(true,false,false,false,false)
	 * etsiSukuTyypit(false,false,false,true,false)
	 * eli siis suvut joilta löytyy kumpikin haettava tyyppi
	 * 
	 * Huom! tämä metodi helposti palauttaa tyhjän listan! Etenkin jos true arvoa esiintyy parametreissä
	 * useammin kuin kahdesti
	 * 
	 * @param magia Haetaanko maagista sukua
	 * @param sotilas Haetaanko sotilaallista sukua
	 * @param uskonnollinen Haetaanko uskonnollista sukua
	 * @param kauppias Haetaanko kauppiassukua
	 * @param maalainen Haetaanko maalaissukua
	 * @return Palauttaa ArrayListana kaikki kombinaation toteuttavat suvut
	 */
	
	ArrayList<Suku> etsiSukuKombo(boolean magia, boolean sotilas, boolean uskonnollinen, boolean kauppias, boolean maalainen) {
		ArrayList<Suku> palautettava = new ArrayList<Suku>();
		boolean[] tyypit = {magia,sotilas,uskonnollinen,kauppias,maalainen};
		
		int summa = 0; //tarkastetaan että kyse on kombosta
		for (boolean tyyppi : tyypit) {
			if (tyyppi == true) {
				summa++;
			}
		}
		if ( summa > 2) {
			return palautettava;
		} else if ( summa == 1 ) { // tilanne jossa haetaan uniikkia tyyppiä
			ArrayList<Suku> kaikki = etsiSukuTyypit(tyypit[0],tyypit[1],tyypit[2], tyypit[3], tyypit[4]);
			for ( Suku tarkasteltava : kaikki ) {
				if (etsiSukuTyypit(!tyypit[0],!tyypit[1],!tyypit[2],!tyypit[3],!tyypit[4]).contains(tarkasteltava)) {
					kaikki.remove(tarkasteltava);
				}
			}
			return kaikki;
		} else if ( summa == 0 ) { // jos etsitään komboa, jossa ei rajoiteta mitään, niin silloin palautetaan kaikki
			return etsiSukuTyypit(true,true,true,true,true);
		}
		
		//normaalitilanne, jossa true-arvoja on tasan kaksi
		boolean[] ensimmainen = new boolean[5];
		boolean[] toinen      = new boolean[5];
		for ( int i = 0; i < 5; i++ ) {
			for ( int j = i+1; j < 5; j++) {
				if ( tyypit[i] == true && tyypit[j] == true) {
					ensimmainen[i] = true;
					toinen[j] = true;
					break;
				}
			}
		}
		ArrayList<Suku> ensimmainenLista = etsiSukuTyypit(ensimmainen[0],ensimmainen[1],ensimmainen[2],ensimmainen[3],ensimmainen[4]);
		
		for ( Suku eka : ensimmainenLista) {
			if ( etsiSukuTyypit(toinen[0],toinen[1],toinen[2],toinen[3],toinen[4]).contains(eka) ) {
				palautettava.add(eka);
			}
		}
		
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