package kuningaskuntaSimulaatio;

import java.util.*;

public class Ongelma {
	private String nimi;
	private String selitys;
	private Suku esittelijaSuku;
	private ArrayList<Paatos> paatokset;
	private ArrayList<Paatos> sallitut;

	// Ongelman luonnin j�lkeen pit�� ne lis�t� arraylistiin.
	public Ongelma(String nimi, String selitys, Suku esittelijaSuku, ArrayList<Paatos> paatokset) {
		this.nimi = nimi;
		this.selitys = selitys;
		this.esittelijaSuku = esittelijaSuku;
		this.paatokset = paatokset;
		this.sallitut = paatokset;
	}

	// Mahdollisesti vaikeusastelis�ys n�ille ajan my�t�
	public void tulosta(Kuningas kunkku) {
		for (Paatos p : paatokset) {
			p.tulostaPaatosrivi(esittelijaSuku.annaNimi() + ":\n" + this.nimi + "!\n" + selitys, kunkku);
		}
	}

	// Pelaajan valitsema p��t�s l�htee liikkeelle
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
	public void tulostaPaatosrivi(String selitys, Kuningas kunkku) {
		int i = 1;
		for (Vaatimus v : vaatimukset) {
			if (v.tarkistaVaatimus(kunkku)) {
				System.out.println(i + ". " + selitys);
			}
			i++;
		}
	}

	// Toteuta paatos
	public void toteutaSeuraukset(Kuningas kunkku) {
		for (Seuraus s : seuraukset) {
			s.toteuta(kunkku);
		}
	}
}

enum Tyyppi {
	RAHA, RUOKA, SUKUSUHDE
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
	public boolean tarkistaVaatimus(Kuningas kunkku) {
		if (tyyppi == Tyyppi.RAHA) {
			if (kunkku.annaRaha() >= this.arvo)
				return true;
			else {
				
			}
		}
		if (tyyppi == Tyyppi.RUOKA) {
			if (kunkku.annaRuoka() >= this.arvo)
				return true;
			else {
				
			}
		}
		if (tyyppi == Tyyppi.SUKUSUHDE) {
			if (kohde.annaSuhdeKuninkaaseen() >= arvo)
				return true;
			else {
				
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
		if (tyyppi == Tyyppi.RUOKA) {
			kunkku.asetaRuoka(kunkku.annaRuoka() + this.arvo);
		}
		if (tyyppi == Tyyppi.SUKUSUHDE) {
			for (Suku s : kohde) {
				s.asetaSuhdeKuninkaaseen(s.annaSuhdeKuninkaaseen() + arvo);
			}
		}
	}
}
