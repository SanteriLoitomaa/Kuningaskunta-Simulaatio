package kuningaskuntaSimulaatio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Luokka, joka toimii Ongelma-olioiden runkona.
 * @author Santeri Loitomaa
 */
public class Ongelma implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nimi;
	private String selitys;
	private Suku esittelijaSuku;
	private ArrayList<Paatos> paatokset;
	private ArrayList<Paatos> sallitut;

	// Ongelman luonnin jälkeen pitää ne lisätä arraylistiin.
	/**
	 * Konstruktori, joka luo Ongelma-olion runkona.
	 * @param nimi
	 * @param selitys (Ongelman esittelyteksti)
	 * @param esittelijaSuku
	 * @param paatokset (lista Ongelman mahdollisista päätöksistä)
	 */
	public Ongelma(String nimi, String selitys, Suku esittelijaSuku, ArrayList<Paatos> paatokset) {
		this.nimi = nimi;
		this.selitys = selitys;
		this.esittelijaSuku = esittelijaSuku;
		this.paatokset = paatokset;
		this.sallitut = new ArrayList<Paatos>(paatokset);
	}

	/**
	 * Metodi joka tulostaa vuoron Ongelma-olion ja sen mahdolliset päätökset pelaajalle näkyväksi.
	 * @param kunkku (Kuninkaan tämänhetkinen instanssi)
	 */
	public void tulosta(Kuningas kunkku, int vuoro) {
		System.out.println();
		System.out.println("Turn " + vuoro + ", " + esittelijaSuku.annaNimi() + "family" + ":\n\n" + this.nimi + "!\n" + selitys);
		for (Paatos p : paatokset) {
			try {
				p.tulostaPaatosrivi(kunkku, this, paatokset.indexOf(p));
			}catch(NullPointerException e) {
				
			}
		}
	}

	/**
	 * Pelaajan valitsema päätös lähtee liikkeelle
	 * @param valinta (pelaajan valitseman päätöksen numero
	 * @param kunkku (Kuninkaan tämänhetkinen instanssi)
	 */
	public void valitsePaatos(int valinta, Kuningas kunkku) {
		this.paatokset.get(valinta - 1).toteutaSeuraukset(kunkku);
		this.sallitut = new ArrayList<Paatos>(paatokset);
	}
	
	/**
	 * Onko päätös laillista tehdä?
	 * @param paatos (Pelaajan päätöksen numero)
	 * @return true/false
	 */
	public boolean onSallittu(String paatos) {
		try {
			int valinta = Integer.parseInt(paatos);
			if(this.sallitut.get(valinta - 1) == null) return false;
			return true;
		}catch(NullPointerException | InputMismatchException | NumberFormatException | IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	/**
	 * Palauttaa listan sallituista Paatos-olioista
	 * @return
	 */
	public ArrayList<Paatos> annaSallitut() {
		return sallitut;
	}
	
	/**
	 * Asettaa listan sallituista Paatos-olioista
	 * @param sallitut (lista sallituista Paatos-olioista)
	 */
	public void asetaSallitut(ArrayList<Paatos> sallitut) {
		this.sallitut = sallitut;
	}
}

/**
 * Luokka, joka toimii Paatos-olioiden runkona.
 * @author Santeri Loitomaa
 */
class Paatos implements Serializable {
	private static final long serialVersionUID = 1L;
	private Vaatimus[] vaatimukset;
	private Seuraus[] seuraukset;
	private String viesti;
	private String lopputulos;
	
	/**
	 * Luo päätöksen Ongelmalle
	 * @param v (Array päätöksen Vaatimuksista)
	 * @param s (Array päätöksen Seurauksista)
	 * @param viesti (Selitys päätöksen Vaatimuksista)
	 * @param lopputulos (Selitys päätöksen Seurauksista)
	 */
	public Paatos(Vaatimus[] v, Seuraus[] s, String viesti, String lopputulos) {
		this.vaatimukset = v;
		this.seuraukset = s;
		this.viesti = viesti;
		this.lopputulos = lopputulos;
	}

	/**
	 * Tulostaa päätöksen jos vaatimukset täyttyvät
	 * @param kunkku (Kuninkaan tämänhetkinen instanssi)
	 * @param o (Tutkittavan Ongelman instanssi)
	 * @param paatosIndex (Tutkittavan päätöksen indeksi Ongelman paatokset-listassa)
	 */
	public void tulostaPaatosrivi(Kuningas kunkku, Ongelma o, int paatosIndex) {
		boolean b = false;
		for (Vaatimus v : vaatimukset) {
			if (v.tarkistaVaatimus(kunkku, paatosIndex, o)) {
				b = true;
			}
			else {
				b = false;
				break;
			}
		}
		if(b)
			System.out.println((paatosIndex + 1) + ". " + this.viesti);
	}

	/**
	 * Toteuta paatos
	 * @param kunkku (Kuninkaan tämänhetkinen instanssi)
	 */
	public void toteutaSeuraukset(Kuningas kunkku) {
		for (Seuraus s : seuraukset) {
			s.toteuta(kunkku);
		}
		System.out.println(lopputulos);
	}
}

/**
 * Enum Tyyppi, jolla voidaan pitää kirjaa Vaatimusten ja Seurausten tyypistä
 * @author Santeri Loitomaa
 */
enum Tyyppi implements Serializable{
	RAHA, RAHA_T, RUOKA, RUOKA_T, SUKUSUHDE, SUKUVALIT, SUKUVALIT_NEG, SUKUPOPULAATIO, SUKUPOPULAATIO_NEG, NULL
}

/**
 * Luokka, joka toimii Vaatimus-olioiden runkona.
 * @author Santeri Loitomaa
 */
class Vaatimus implements Serializable{
	private static final long serialVersionUID = 1L;
	private Tyyppi tyyppi;
	private int arvo;
	private Suku kohde;
	private Suku kohde2;
	
	/**
	 * Luo Vaatimuksen ongelmalle
	 * @param tyyppi (Vaatimuksen tyyppi)
	 * @param arvo (Muutos joka tyypin määrittämälle arvolle tapahtuu)
	 * @param kohde (Jos Vaatimuksen tyyppi vaatii kohdesuvun)
	 * @param kohde2 (Jos Vaatimuksen tyyppi vaatii toisen kohdesuvun)
	 */
	public Vaatimus(Tyyppi tyyppi, int arvo, Suku kohde, Suku kohde2) {
		this.tyyppi = tyyppi;
		this.arvo = arvo;
		if (kohde != null)
			this.kohde = kohde;
		if (kohde2 != null)
			this.kohde2 = kohde2;
	}
	public Vaatimus(Tyyppi tyyppi, int arvo, Suku kohde) {
		this.tyyppi = tyyppi;
		this.arvo = arvo;
		if (kohde != null)
			this.kohde = kohde;
	}
	public Vaatimus(Tyyppi tyyppi, int arvo) {
		this.tyyppi = tyyppi;
		this.arvo = arvo;
	}

	/**
	 * Tarkista päätöksen mahdollisuuden. Jos ei mahdollista, päätös poistetaan mahdollisten päätösten listasta.
	 * @param kunkku (Kuninkaan tämänhetkinen instanssi)
	 * @param paatosIndex (Tutkittavan päätöksen indeksi Ongelman paatokset-listassa)
	 * @param o (Läpikäytävä Ongelma)
	 * @return true (jos ei poistettu)
	 */
	public boolean tarkistaVaatimus(Kuningas kunkku, int paatosIndex, Ongelma o) {
		if (tyyppi == Tyyppi.RAHA) {
			if (kunkku.annaRaha() >= this.arvo)
				return true;
			else {
				ArrayList<Paatos> sallitut = o.annaSallitut();
				sallitut.set(paatosIndex, null);
				o.asetaSallitut(sallitut);
			}
		}
		if (tyyppi == Tyyppi.RAHA_T) {
			if (kunkku.annaRahaTuotto() >= this.arvo)
				return true;
			else {
				ArrayList<Paatos> sallitut = o.annaSallitut();
				sallitut.set(paatosIndex, null);
				o.asetaSallitut(sallitut);
			}
		}
		if (tyyppi == Tyyppi.RUOKA) {
			if (kunkku.annaRuoka() >= this.arvo)
				return true;
			else {
				ArrayList<Paatos> sallitut = o.annaSallitut();
				sallitut.set(paatosIndex, null);
				o.asetaSallitut(sallitut);
			}
		}
		if (tyyppi == Tyyppi.RUOKA_T) {
			if (kunkku.annaRuokaTuotto() >= this.arvo)
				return true;
			else {
				ArrayList<Paatos> sallitut = o.annaSallitut();
				sallitut.set(paatosIndex, null);
				o.asetaSallitut(sallitut);
			}
		}
		if (tyyppi == Tyyppi.SUKUSUHDE) {
			if (kohde.annaSuhdeKuninkaaseen() >= arvo)
				return true;
			else {
				ArrayList<Paatos> sallitut = o.annaSallitut();
				sallitut.set(paatosIndex, null);
				o.asetaSallitut(sallitut);
			}
		}
		if (tyyppi == Tyyppi.SUKUVALIT) {
			if (kohde.annaSuhdeSukuun(kohde2) >= arvo)
				return true;
			else {
				ArrayList<Paatos> sallitut = o.annaSallitut();
				sallitut.set(paatosIndex, null);
				o.asetaSallitut(sallitut);
			}
		}
		if (tyyppi == Tyyppi.SUKUVALIT_NEG) {
			if (kohde.annaSuhdeSukuun(kohde2) <= arvo)
				return true;
			else {
				ArrayList<Paatos> sallitut = o.annaSallitut();
				sallitut.set(paatosIndex, null);
				o.asetaSallitut(sallitut);
			}
		}
		if (tyyppi == Tyyppi.SUKUPOPULAATIO) {
			if (kohde.annaPopulaatio() >= arvo)
				return true;
			else {
				ArrayList<Paatos> sallitut = o.annaSallitut();
				sallitut.set(paatosIndex, null);
				o.asetaSallitut(sallitut);
			}
		}
		if (tyyppi == Tyyppi.SUKUPOPULAATIO_NEG) {
			if (kohde.annaPopulaatio() <= arvo)
				return true;
			else {
				ArrayList<Paatos> sallitut = o.annaSallitut();
				sallitut.set(paatosIndex, null);
				o.asetaSallitut(sallitut);
			}
		}
		if(tyyppi == Tyyppi.NULL) {
			return true;
		}
		return false;
	}
}

/**
 * Luokka, joka toimii Seuraus-olioiden runkona.
 * @author Santeri Loitomaa
 */
class Seuraus implements Serializable {
	private static final long serialVersionUID = 1L;
	private Tyyppi tyyppi;
	private int arvo;
	private ArrayList<Suku> kohde;
	private ArrayList<Suku> uhri;

	/**
	 * Luo Seurauksen ongelmalle
	 * @param tyyppi (Vaatimuksen tyyppi)
	 * @param arvo (Muutos joka tyypin määrittämälle arvolle tapahtuu)
	 * @param kohde (Jos Vaatimuksen tyyppi vaatii kohdesuvun [lista])
	 * @param uhri (Jos Vaatimuksen tyyppi vaatii toisen kohdesuvun [lista])
	 */
	public Seuraus(Tyyppi tyyppi, int arvo, ArrayList<Suku> kohde, ArrayList<Suku> uhri) {
		this.tyyppi = tyyppi;
		this.arvo = arvo;
		if (kohde != null)
			this.kohde = kohde;
		if(uhri != null)
			this.uhri = uhri;
	}
	public Seuraus(Tyyppi tyyppi, int arvo, ArrayList<Suku> kohde) {
		this.tyyppi = tyyppi;
		this.arvo = arvo;
		if (kohde != null)
			this.kohde = kohde;
	}
	public Seuraus(Tyyppi tyyppi, int arvo) {
		this.tyyppi = tyyppi;
		this.arvo = arvo;
	}

	/**
	 * Toteuta Seuraus
	 * @param kunkku (Kuninkaan tämänhetkinen instanssi)
	 */
	public void toteuta(Kuningas kunkku) {
		if (tyyppi == Tyyppi.RAHA) {
			kunkku.asetaRaha(kunkku.annaRaha() + this.arvo);
		}
		if (tyyppi == Tyyppi.RAHA_T) {
			kunkku.asetaRahaTuotto(kunkku.annaRahaTuotto() + this.arvo);
		}
		if (tyyppi == Tyyppi.RUOKA) {
			kunkku.asetaRuoka(kunkku.annaRuoka() + this.arvo);
		}
		if (tyyppi == Tyyppi.RUOKA_T) {
			kunkku.asetaRuokaTuotto(kunkku.annaRuokaTuotto() + this.arvo);
		}
		if (tyyppi == Tyyppi.SUKUSUHDE) {
			for (Suku s : kohde) {
				s.asetaSuhdeKuninkaaseen(s.annaSuhdeKuninkaaseen() + arvo);
			}
		}
		if (tyyppi == Tyyppi.SUKUVALIT) {
			for(Suku s : kohde) {
				for(Suku u : uhri) {
					s.asetaSuhdeSukuun(arvo + s.annaSuhdeSukuun(u), u);
				}
			}
		}
		if (tyyppi == Tyyppi.SUKUPOPULAATIO) {
			for (Suku s : kohde) {
				s.asetaPopulaatio(s.annaPopulaatio() + arvo);
			}
		}
	}
}
