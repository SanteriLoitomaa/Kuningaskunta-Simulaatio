package kuningaskuntaSimulaatio;

import java.util.*;

public class Ongelma {
	private String nimi;
	private String selitys;
	private Suku esittelijaSuku;
	private ArrayList<Paatos> paatokset;

	// Ongelman luonnin jälkeen pitää ne lisätä arraylistiin.
	public Ongelma(String nimi, String selitys, Suku esittelijaSuku, ArrayList<Paatos> paatokset) {
		this.nimi = nimi;
		this.selitys = selitys;
		this.esittelijaSuku = esittelijaSuku;
		this.paatokset = paatokset;
	}

	// Mahdollisesti vaikeusastelisäys näille ajan myötä
	public void tulosta(Kuningas kunkku) {
		for (Paatos p : paatokset) {
			p.tulostaPaatosrivi(selitys, kunkku);
		}
	}

	// Pelaajan valitsema päätös lähtee liikkeelle
	public void valitsePaatos(int valinta, Kuningas kunkku) {
		paatokset.get(valinta - 1).toteutaSeuraukset(kunkku);
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
	private ArrayList<Suku> kohde;

	public Vaatimus(Tyyppi tyyppi, int arvo, ArrayList<Suku> kohde) {
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
		}
		if (tyyppi == Tyyppi.RUOKA) {
			if (kunkku.annaRuoka() >= this.arvo)
				return true;
		}
		if (tyyppi == Tyyppi.SUKUSUHDE) {
			for (Suku s : kohde) {
				if (s.annaSuhdeKuninkaaseen() >= arvo)
					return true;
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
