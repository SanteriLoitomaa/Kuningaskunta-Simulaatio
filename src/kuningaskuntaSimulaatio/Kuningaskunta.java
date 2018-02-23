package kuningaskuntaSimulaatio;

import java.util.*;

public class Kuningaskunta{

	public static void main(String[] args) {
		Scanner vastaus = new Scanner(System.in);
		while(true) {
			System.out.println("Tervetuloa pelaamaan Kuningaskunta Simulaattoria! Haluatko...");
			System.out.println("1. aloittaa uuden pelin? (Mahdollinen vanha pelisi katoaa)");
			System.out.println("2. jatkaa siitä, mihin jäit?");
			System.out.println("3. nähdä parhaimmat pisteet?");
			System.out.println("4. nollata pisteet?");
			System.out.println("5. poistua pelistä?");
			while (!vastaus.hasNextInt()) {
				vastaus.next();
			}
			int vast = vastaus.nextInt();
			// Peli alkaa tästä
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
			// Pelin lataus
			if(vast == 2) {
				Kuningas kunkku = TallennaLataaPisteet.lataa();
				System.out.println("Hyvä että palasitte, " + kunkku.annaNimi());
				kunkku.scan(vastaus);
				kunkku.vuorokierto();
			}
			// Parhaat pisteet
			if(vast == 3) {
				TallennaLataaPisteet.tulostaPisteet();
			}
			// Resetoi pisteet
			if(vast == 4) TallennaLataaPisteet.luoPisteet();
			// Poistu pelistä
			if(vast == 5) break;
			// Huijauskoodi
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
		
		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.SUKUSUHDE, 1, kunkku.etsiSukuTyypit(true, false, false, false, false).get(0), null) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.suvut),
						new Seuraus(Tyyppi.SUKUSUHDE, -20, kunkku.etsiSukuTyypit(false, false, true, false, false)),
						new Seuraus(Tyyppi.SUKUSUHDE, 20, kunkku.etsiSukuTyypit(false, false, true, false, false))},
				//Puuttuu vielä sukujen väliset muutokset//,
				"Kansa on selvästikkin käsittänyt jotain väärin. Annas kun selitän, mistä tämä johtui."));

		Random r = new Random();
		Suku x = kunkku.suvut.get(r.nextInt(kunkku.suvut.size()));
		String neitsyenKoti = x.annaNimi();
		ArrayList<Suku> uhri = new ArrayList<Suku>(Arrays.asList(x));
		// Huumori mielessä jätimem mahdolliseksi tapattaa ehdottajan tyttären xD
		
		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.SUKUSUHDE, 1, kunkku.etsiSukuTyypit(false, false, true, false, false).get(0), null) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.suvut),
						new Seuraus(Tyyppi.SUKUSUHDE, -20, uhri),
						new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.suvut)},
						//Puuttuu vielä sukujen väliset muutokset},//
				"Jos jumalat ovat tosiaan niin vihaisia meidän on uhrattava neitsyt " + neitsyenKoti + " suvulta mitä pikimmiten."));
		
		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.NULL, 0, null, null) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, -20, kunkku.suvut)},
				"En näe miten tämä ongelma koskee minua."));
		
		kunkku.ongelmat.add(new Ongelma("Maanjäristys",
				"Jumalat ovat vihaisia meille. Kaikkia sukuja on kohdannut onnettomuus ja heidän "
						+ "tilansa ovat kokeneet suurta vahinkoa. Miten toimimme?",
				kunkku.annaAatelisin(), paatokset));
	}
}