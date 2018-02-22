package kuningaskuntaSimulaatio;

import java.util.*;

public class Kuningaskunta{

	public static void main(String[] args) {

		// prologi
		Scanner vastaus = new Scanner(System.in);
		System.out.println("Tervetuloa pelaamaan Kuningaskunta Simulaattoria! Haluatko...");
		System.out.println("1. aloittaa uuden pelin? (Mahdollinen vanha pelisi katoaa)");
		System.out.println("2. jatkaa siitä, mihin jäit?");
		while (!vastaus.hasNextInt()) {
			vastaus.next();
		}
		int vast = vastaus.nextInt();
		if(vast == 1) {
			System.out.println("Miten haluatte että kutsun teitä, teidän ylhäisyytenne? (nimi ja titteli)");
			String nimi = vastaus.next();
			System.out.println("Kuinka monta vuotta aiot hallita? (Joka kuukausi tapahtuu jotain!)");
			while (!vastaus.hasNextInt()) {
				vastaus.next();
			}
			int vuorot = vastaus.nextInt();
			vuorot = vuorot * 12;
			Kuningas kunkku = new Kuningas(nimi, vuorot);
			meillaOnOngelmia(kunkku);
			System.out.println("On kunnia tavata teidät, " + kunkku.annaNimi());
			kunkku.scan(vastaus);
			kunkku.vuorokierto();
		}
		if(vast == 2) {
			Kuningas kunkku = TallennaLataaPisteet.lataa();
			System.out.println("Hyvä että palasitte, " + kunkku.annaNimi());
			kunkku.scan(vastaus);
			kunkku.vuorokierto();
		}
		if(vast == 1337) {
			Kuningas kunkku = new Kuningas("Kuningas Arthur", 80);
			kunkku.asetaRaha(10000);
			kunkku.asetaRuoka(10000);
			kunkku.asetaRahaTuotto(10);
			kunkku.asetaRuokaTuotto(10);
			for(Suku s : kunkku.suvut) {
				s.asetaSuhdeKuninkaaseen(80);
			}
			kunkku.tulostaSuvut();
			meillaOnOngelmia(kunkku);
			System.out.println("On kunnia tavata teidät, " + kunkku.annaNimi());
			kunkku.scan(vastaus);
			kunkku.vuorokierto();
		}
		
		vastaus.close();
	}

	// Luo kaikki pelin ongelmat, pitää kutsua sukujen luonnin jälkeen.
	public static void meillaOnOngelmia(Kuningas kunkku) {
		ArrayList<Paatos> paatokset = new ArrayList<Paatos>();

		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.RAHA, kunkku.annaSukujenLKM() * 5, null, null) },
				new Seuraus[] { new Seuraus(Tyyppi.RAHA, -(kunkku.annaSukujenLKM() * 5), null),
						new Seuraus(Tyyppi.SUKUSUHDE, 20, kunkku.suvut) },
				"Voin auttaa teitä korjaustöissä viidellä kullalla per suku."));
		
		/*paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.SUKUSUHDE, 1, kunkku.etsiSuosituinMagia().get(0), null) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.suvut),
						new Seuraus(Tyyppi.SUKUSUHDE, -20, kunkku.etsiSuosituinUskonnollinen()),
						new Seuraus(Tyyppi.SUKUSUHDE, 20, etsisuku(maaginen & uskonnollinen)),
						Puuttuu vielä sukujen väliset muutokset},
				"Kansa on selvästikkin käsittänyt jotain väärin. Annas kun selitän, mistä tämä johtui."));

		Random r = new Random();
		Suku x = kunkku.suvut.get(r.nextInt(kunkku.suvut.size()));
		String neitsyenKoti = x.annaNimi();
		ArrayList<Suku> uhri = new ArrayList<Suku>(Arrays.asList(x));
		// Huumori mielessä jätimem mahdolliseksi tapattaa ehdottajan tyttären xD
		
		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.SUKUSUHDE, 1, kunkku.etsiSuosituinUskonnollinen().get(0), null) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.suvut),
						new Seuraus(Tyyppi.SUKUSUHDE, -20, uhri),
						new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.suvut),
						Puuttuu vielä sukujen väliset muutokset},
				"Jos jumalat ovat tosiaan niin vihaisia meidän on uhrattava neitsyt " + neitsyenKoti + " suvulta mitä pikimmiten."));*/
		
		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.NULL, 0, null, null) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, -20, kunkku.suvut)},
				"En näe miten tämä ongelma koskee minua."));
		
		kunkku.ongelmat.add(new Ongelma("Maanjäristys",
				"Jumalat ovat vihaisia meille. Kaikkia sukuja on kohdannut onnettomuus ja heidän "
						+ "tilansa ovat kokeneet suurta vahinkoa. Miten toimimme?",
				kunkku.annaAatelisin(), paatokset));
	}
}