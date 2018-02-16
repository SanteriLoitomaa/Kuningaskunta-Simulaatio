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

		// peli alkaa
		kunkku.vuorokierto();
		vastaus.close();
	}

	// Luo kaikki pelin ongelmat, pitää kutsua sukujen luonnin jälkeen.
	public static void meillaOnOngelmia(Kuningas kunkku, int vuorot) {
		ArrayList<Paatos> paatokset = new ArrayList<Paatos>();

		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.RAHA, kunkku.annaSukujenLKM() * 5, null) },
				new Seuraus[] { new Seuraus(Tyyppi.RAHA, -kunkku.annaSukujenLKM() * 5, null),
						new Seuraus(Tyyppi.SUKUSUHDE, 20, kunkku.suvut) },
				"Voin auttaa teitä korjaustöissä viidellä kullalla per suku."));

		kunkku.ongelmat.add(new Ongelma("Maanjäristys",
				"Jumalat ovat vihaisia meille. Kaikkia sukuja on kohdannut onnettomuus ja heidän "
						+ "tilansa ovat kokeneet suurta vahinkoa. Miten toimimme?",
				kunkku.suvut.get(0), paatokset));
	}
}