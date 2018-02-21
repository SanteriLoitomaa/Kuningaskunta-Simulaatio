package kuningaskuntaSimulaatio;

import java.util.*;

public class Kuningaskunta {

	public static void main(String[] args) {

		// prologi
		Scanner vastaus = new Scanner(System.in);
		System.out.println(
				"Tervetuloa pelaamaan Kuningaskunta Simulaattoria! Miten haluatte että kutsun teitä, teidän ylhäisyytenne? (nimi ja titteli)");
		String nimi = vastaus.nextLine();
		System.out.println("Kuinka monta vuotta aiot hallita? (Joka kuukausi tapahtuu jotain!)");
		while (!vastaus.hasNextInt()) {
			vastaus.next();
		}
		int vuorot = vastaus.nextInt();
		vuorot = vuorot * 12;
		Kuningas kunkku = new Kuningas(nimi, vuorot);
		meillaOnOngelmia(kunkku, vuorot);
		System.out.println("On kunnia tavata teidät, " + kunkku.annaNimi());
		
		kunkku.tulostaSuvut();
		
		// peli alkaa
		kunkku.vuorokierto();
		vastaus.close();
	}

	// Luo kaikki pelin ongelmat, pitää kutsua sukujen luonnin jälkeen.
	public static void meillaOnOngelmia(Kuningas kunkku, int vuorot) {
		ArrayList<Paatos> paatokset = new ArrayList<Paatos>();

		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.RAHA, kunkku.annaSukujenLKM() * 5, null) },
				new Seuraus[] { new Seuraus(Tyyppi.RAHA, -(kunkku.annaSukujenLKM() * 5), null),
						new Seuraus(Tyyppi.SUKUSUHDE, 20, kunkku.suvut) },
				"Voin auttaa teitä korjaustöissä viidellä kullalla per suku."));
		
		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.SUKUSUHDE, 1, /*kunkku.etsiSuosituinMagia().get(0)*/) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.suvut),
						new Seuraus(Tyyppi.SUKUSUHDE, -20, /*kunkku.etsiSuosituinUskonnollinen()*/),
						new Seuraus(Tyyppi.SUKUSUHDE, 20, /*etsisuku(maaginen & uskonnollinen)*/),
						/*Puuttuu vielä sukujen väliset muutokset*/},
				"Kansa on selvästikkin käsittänyt jotain väärin. Annas kun selitän, mistä tämä johtui."));

		Random r = new Random();
		Suku x = kunkku.suvut.get(r.nextInt(kunkku.suvut.size()));
		String neitsyenKoti = x.annaNimi();
		ArrayList<Suku> uhri = new ArrayList<Suku>(Arrays.asList(x));
		// Huumori mielessä jätimem mahdolliseksi tapattaa ehdottajan tyttären xD
		
		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.SUKUSUHDE, 1, /*kunkku.etsiSuosituinUskonnollinen().get(0)*/) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.suvut),
						new Seuraus(Tyyppi.SUKUSUHDE, -20, uhri),
						new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.suvut),
						/*Puuttuu vielä sukujen väliset muutokset*/},
				"Jos jumalat ovat tosiaan niin vihaisia meidän on uhrattava neitsyt " + neitsyenKoti + " suvulta mitä pikimmiten."));
		
		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.NULL, 0, null) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, -20, kunkku.suvut)},
				"En näe miten tämä ongelma koskee minua."));
		
		kunkku.ongelmat.add(new Ongelma("Maanjäristys",
				"Jumalat ovat vihaisia meille. Kaikkia sukuja on kohdannut onnettomuus ja heidän "
						+ "tilansa ovat kokeneet suurta vahinkoa. Miten toimimme?",
				kunkku.annaAatelisin(), paatokset));
	}
}