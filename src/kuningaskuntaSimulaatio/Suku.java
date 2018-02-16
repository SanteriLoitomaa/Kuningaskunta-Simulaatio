package kuningaskuntaSimulaatio;

import java.util.*;

public class Suku {
	private int suhdeKuninkaaseen;
	private HashMap<Suku, Integer> suhteet = new HashMap<Suku, Integer>();
	private String nimi;
	private String edustaja;
	private int populaatio;
	private int aatelisuus; // Aina arvot 0-3

	private int magia; // Suvun maagisuus (0-4)
	private int sotilaallinen; // Suvun sotilaallisuus (0-4)
	private int uskonnollinen; // Suvun uskonnollisuus (0-4) ARVOT YHTEENS� 4 PER SUKU!
	private int kauppias; // Suvun kauppiaisuus (0-4)
	private int maalainen; // Suvun maallisuus (0-4)

	public Suku() { // Laitetaan default arvot, up to change.
		this.populaatio = 0;
		this.aatelisuus = 0;
		this.magia = 0;
		this.sotilaallinen = 0;
		this.uskonnollinen = 0;
		this.kauppias = 0;
		this.maalainen = 0;
	}

	public void asetaNimi(String nimi) {
		this.nimi = nimi;
	}

	public void asetaSuhdeKuninkaaseen(int suhdeKuninkaaseen) {
		this.suhdeKuninkaaseen = suhdeKuninkaaseen;
	}

	public void asetaSuhdeSukuun(int Suhdeluku, Suku kohde) {
		suhteet.put(kohde, Suhdeluku);
	}

	public void asetaEdustaja(String edustaja) {
		this.edustaja = edustaja;
	}

	public void asetaPopulaatio(int populaatio) {
		this.populaatio = populaatio;
	}

	public void asetaAatelisuus(int aatelisuus) {
		this.aatelisuus = aatelisuus;
	}

	public void asetaMagia(int magia) {
		this.magia = magia;
	}

	public void asetaSotilaallinen(int sotilaallinen) {
		this.sotilaallinen = sotilaallinen;
	}

	public void asetaUskonnollinen(int uskonnollinen) {
		this.uskonnollinen = uskonnollinen;
	}

	public void asetaKauppias(int kauppias) {
		this.kauppias = kauppias;
	}

	public void asetaMaalainen(int maalainen) {
		this.maalainen = maalainen;
	}

	public String annaNimi() {
		return nimi;
	}

	public int annaSuhdeKuninkaaseen() {
		return suhdeKuninkaaseen;
	}

	public int annaSuhdeSukuun(Suku x) {
		return suhteet.get(x);
	}

	public String annaEdustaja() {
		return edustaja;
	}

	public int annaPopulaatio() {
		return populaatio;
	}

	public int annaAatelisuus() {
		return aatelisuus;
	}

	public int annaMagia() {
		return magia;
	}

	public int annaSotilaallinen() {
		return sotilaallinen;
	}

	public int annaUskonnollinen() {
		return uskonnollinen;
	}

	public int annaKauppias() {
		return kauppias;
	}

	public int annaMaalainen() {
		return maalainen;
	}

	public String toString() { // Muuttaa stringiksi suvun tiedot niiss� paikoissa joissa ne vaaditaan, tyyppi
											// ja aatelisuus tulostetaan vain silloin kun ne ovat olemassa.
		String mjono = "";
		mjono += annaNimi() + ":\n";
		mjono += "Sukua edustaa " + annaEdustaja() + "\n";
		mjono += "Suku teihin on: " + annaSuhdeKuninkaaseen() + "\n";
		// Selvitet��n keihin suvulla on huonoin ja paras suhde.

		int huonoin = 100;
		Suku hsuku = suhteet.keySet().iterator().next();
		int paras = -100;
		Suku psuku = suhteet.keySet().iterator().next();
		for (Suku tarkasteltava : suhteet.keySet()) {
			if (suhteet.get(tarkasteltava) < huonoin) {
				huonoin = suhteet.get(tarkasteltava);
				hsuku = tarkasteltava;
			}
			if (suhteet.get(tarkasteltava) > paras) {
				psuku = tarkasteltava;
				paras = suhteet.get(tarkasteltava);
			}
		}
		
		// selvitetty
		mjono += "Heid�n L�heisin liittolaisensa on " + psuku.annaNimi() + "\n";
		mjono += "Heid�n pahin vihamiehens� on " + hsuku.annaNimi() + "\n";
		mjono += "Suvussa on " + annaPopulaatio() + " j�sent�.\n";
		if (annaAatelisuus() > 0) {
			mjono += "Suku on aatelinen.\n";
		}
		if (annaMagia() > 0) {
			mjono += "Suku on maaginen.\n";
		}
		if (annaSotilaallinen() > 0) {
			mjono += "Suku on sotilaallinen.\n";
		}
		if (annaUskonnollinen() > 0) {
			mjono += "Suku on uskonnollinen.\n";
		}
		if (annaKauppias() > 0) {
			mjono += "Suku on kauppiassuku.\n";
		}
		if (annaMaalainen() > 0) {
			mjono += "Suku tuottaa ruokaa.\n";
		}
		return mjono;
	}
	public void tulostaSuhteet() {
		System.out.println("Suvulla " + this.annaNimi() + " on seuraavat suhteet muihin sukuihin:");
		for (Suku tarkasteltava : suhteet.keySet()) {
			System.out.println(tarkasteltava.annaNimi() + ": " + suhteet.get(tarkasteltava));
		}
	}
}
