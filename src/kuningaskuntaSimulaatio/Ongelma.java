package kuningaskuntaSimulaatio;

import java.io.Serializable;
import java.util.*;

public class Ongelma implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nimi;
	private String selitys;
	private Suku esittelijaSuku;
	private ArrayList<Paatos> paatokset;
	private ArrayList<Paatos> sallitut;

	// Ongelman luonnin jälkeen pitää ne lisätä arraylistiin.
	public Ongelma(String nimi, String selitys, Suku esittelijaSuku, ArrayList<Paatos> paatokset) {
		this.nimi = nimi;
		this.selitys = selitys;
		this.esittelijaSuku = esittelijaSuku;
		this.paatokset = paatokset;
		this.sallitut = new ArrayList<Paatos>(paatokset);
	}

	// Mahdollisesti vaikeusastelisäys näille ajan myätä
	public void tulosta(Kuningas kunkku) {
		System.out.println(esittelijaSuku.annaNimi() + ":\n" + this.nimi + "!\n" + selitys);
		for (Paatos p : paatokset) {
			p.tulostaPaatosrivi(kunkku, this, paatokset.indexOf(p));
		}
	}

	// Pelaajan valitsema päätäs lähtee liikkeelle
	public void valitsePaatos(int valinta, Kuningas kunkku) {
		this.paatokset.get(valinta - 1).toteutaSeuraukset(kunkku);
		this.sallitut = new ArrayList<Paatos>(paatokset);
	}
	
	//Onko päätäs laillista tehdä?
	public boolean onSallittu(String paatos) {
		try {
			System.out.print("Päätäksesi numero on: ");
			int valinta = Integer.parseInt(paatos);
			if(this.sallitut.get(valinta - 1) == null) return false;
			return true;
		}catch(NullPointerException | InputMismatchException | NumberFormatException | IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public ArrayList<Paatos> annaSallitut() {
		return sallitut;
	}
	
	public void asetaSallitut(ArrayList<Paatos> sallitut) {
		this.sallitut = sallitut;
	}
}

class Paatos implements Serializable {
	private static final long serialVersionUID = 1L;
	private Vaatimus[] vaatimukset;
	private Seuraus[] seuraukset;
	private String viesti;

	public Paatos(Vaatimus[] v, Seuraus[] s, String viesti) {
		this.vaatimukset = v;
		this.seuraukset = s;
		this.viesti = viesti;
	}

	// Tulostaa mahdollisuudet
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

	// Toteuta paatos
	public void toteutaSeuraukset(Kuningas kunkku) {
		for (Seuraus s : seuraukset) {
			s.toteuta(kunkku);
		}
	}
}

enum Tyyppi implements Serializable{
	RAHA, RAHA_T, RUOKA, RUOKA_T, SUKUSUHDE, SUKUVALIT, SUKUVALIT_NEG, SUKUVALIT_MON, NULL
}

class Vaatimus implements Serializable{
	private static final long serialVersionUID = 1L;
	private Tyyppi tyyppi;
	private int arvo;
	private Suku kohde;
	private Suku kohde2;
	
	//kaksi useampaa konstruktoria joihin ei laitettaisi null-arvoja
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

	// Tarkista mahdollisuus. Jos ei mahdollista, poista sallituista päätäksistä.
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
		if(tyyppi == Tyyppi.NULL) {
			return true;
		}
		return false;
	}
}

class Seuraus implements Serializable {
	private static final long serialVersionUID = 1L;
	private Tyyppi tyyppi;
	private int arvo;
	private String kuvaus;
	private ArrayList<Suku> kohde;
	private ArrayList<Suku> uhri;

	public Seuraus(Tyyppi tyyppi, int arvo, String kuvaus, ArrayList<Suku> kohde, ArrayList<Suku> uhri) {
		this.tyyppi = tyyppi;
		this.arvo = arvo;
		this.kuvaus = kuvaus;
		if (kohde != null)
			this.kohde = kohde;
		if(uhri != null)
			this.uhri = uhri;
	}
	public Seuraus(Tyyppi tyyppi, int arvo, String kuvaus, ArrayList<Suku> kohde) {
		this.tyyppi = tyyppi;
		this.arvo = arvo;
		this.kuvaus = kuvaus;
		if (kohde != null)
			this.kohde = kohde;
	}
	public Seuraus(Tyyppi tyyppi, int arvo, String kuvaus) {
		this.tyyppi = tyyppi;
		this.arvo = arvo;
		this.kuvaus = kuvaus;
	}

	// Tee muutokset
	public void toteuta(Kuningas kunkku) {
		System.out.println(kuvaus);
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
			kohde.get(0).asetaSuhdeSukuun(arvo + kohde.get(0).annaSuhdeSukuun(kohde.get(1)), kohde.get(1));
		}
		if (tyyppi == Tyyppi.SUKUVALIT_MON) {
			for(Suku s : kohde) {
				for(Suku u : uhri) {
					s.asetaSuhdeSukuun(arvo + s.annaSuhdeSukuun(u), u);
				}
			}
		}
	}
}
