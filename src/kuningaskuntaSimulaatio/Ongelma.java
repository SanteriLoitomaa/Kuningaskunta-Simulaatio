package kuningaskuntaSimulaatio;

import java.util.*;

public class Ongelma {
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
		this.sallitut = paatokset;
	}

	// Mahdollisesti vaikeusastelisäys näille ajan myötä
	public void tulosta(Kuningas kunkku) {
		for (Paatos p : paatokset) {
			p.tulostaPaatosrivi(esittelijaSuku.annaNimi() + ":\n" + this.nimi + "!\n" + selitys, kunkku, this, paatokset.indexOf(p));
		}
	}

	// Pelaajan valitsema päätös lähtee liikkeelle
	public void valitsePaatos(int valinta, Kuningas kunkku) {
		paatokset.get(valinta - 1).toteutaSeuraukset(kunkku);
	}
	
	public ArrayList<Paatos> annaSallitut() {
		return sallitut;
	}
	
	public void asetaSallitut(ArrayList<Paatos> sallitut) {
		this.sallitut = sallitut;
	}
}

class Paatos {
	private Vaatimus[] vaatimukset;
	private Seuraus[] seuraukset;

	public Paatos(Vaatimus[] v, Seuraus[] s, String viesti) {
		this.vaatimukset = v;
		this.seuraukset = s;
	}

	// Tulostaa mahdollisuudet
	public void tulostaPaatosrivi(String selitys, Kuningas kunkku, Ongelma o, int paatosIndex) {
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
			System.out.println((paatosIndex + 1) + ". " + selitys);
	}

	// Toteuta paatos
	public void toteutaSeuraukset(Kuningas kunkku) {
		for (Seuraus s : seuraukset) {
			s.toteuta(kunkku);
		}
	}
}

enum Tyyppi {
	RAHA, RAHA_T, RUOKA, RUOKA_T, SUKUSUHDE
}

class Vaatimus {
	private Tyyppi tyyppi;
	private int arvo;
	private Suku kohde;

	public Vaatimus(Tyyppi tyyppi, int arvo, Suku kohde) {
		this.tyyppi = tyyppi;
		this.arvo = arvo;
		if (kohde != null)
			this.kohde = kohde;
	}

	// Tarkista mahdollisuus
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
		return false;
	}
}

class Seuraus {
	private Tyyppi tyyppi;
	private int arvo;
	private ArrayList<Suku> kohde;

	public Seuraus(Tyyppi tyyppi, int arvo, ArrayList<Suku> kohde) {
		this.tyyppi = tyyppi;
		this.arvo = arvo;
		if (kohde != null)
			this.kohde = kohde;
	}

	// Tee muutokset
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
	}
}
