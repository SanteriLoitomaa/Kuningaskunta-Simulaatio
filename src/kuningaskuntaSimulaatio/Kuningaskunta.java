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

		//Paatos(Vaatimus[] v, Seuraus[] s, String viesti)
		//Vaatimus(Tyyppi tyyppi, int arvo [{, Suku kohde}, Suku kohde2])
		//Seuraus(Tyyppi tyyppi, int arvo, String kuvaus[, ArrayList<Suku> kohde])
		
		// Ongelma : Maanjäristys, Jumalat ovat vihaisia meille. Kaikkia sukuja on kohdannut onnettomuus ja heidän 
		// tilansa ovat kokeneet suurta vahinkoa. Miten toimimme?
		
		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.RAHA, kunkku.annaSukujenLKM() * 5) },
				new Seuraus[] { new Seuraus(Tyyppi.RAHA, -(kunkku.annaSukujenLKM() * 5), ""),
						new Seuraus(Tyyppi.SUKUSUHDE, 20, "Kaikki suvut tykkäävät, vaikka olet nyt hiukan köyhempi kuningas!", kunkku.suvut) },
				"Voin auttaa teitä korjaustäissä viidellä kullalla per suku."));
		
		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.SUKUSUHDE, 1, kunkku.etsiSukuTyypit(true, false, false, false, false).get(0), null) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, 10, "Kaikki suvut tykkäävät selityksestäsi", kunkku.suvut),
						new Seuraus(Tyyppi.SUKUSUHDE, -20, "paitsi uskonnolliset suvut joiden uskomuksia se loukkaa", kunkku.etsiSukuTyypit(false, false, true, false, false)),
						new Seuraus(Tyyppi.SUKUSUHDE, 20, "", kunkku.etsiSukuTyypit(false, false, true, true, false))},
				//Puuttuu vielä sukujen väliset muutokset//,
				"Kansa on selvästikkin käsittänyt jotain väärin. Annas kun selitän, mistä tämä johtui."));

		Random r = new Random();
		Suku x = kunkku.suvut.get(r.nextInt(kunkku.suvut.size()));
		String neitsyenKoti = x.annaNimi();
		ArrayList<Suku> uhri = new ArrayList<Suku>(Arrays.asList(x));
		// Huumori mielessä jätimem mahdolliseksi tapattaa ehdottajan tyttären xD
		
		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.SUKUSUHDE, 1, kunkku.etsiSukuTyypit(false, false, true, false, false).get(0)) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, 10, "", kunkku.suvut),
						new Seuraus(Tyyppi.SUKUSUHDE, -20, x.annaNimi() + " suku itkee kauniin neitsyensä kohtalosta, mutta", uhri),
						new Seuraus(Tyyppi.SUKUSUHDE, 10, "muut suvut huokaisevat helpotuksesta, koska heihin ei kohdistu vaaraa", kunkku.suvut)},
						//Puuttuu vielä sukujen väliset muutokset},//
				"Jos jumalat ovat tosiaan niin vihaisia meidän on uhrattava neitsyt " + neitsyenKoti + " suvulta mitä pikimmiten."));
		
		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.NULL, 0) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, -20, "Kaikki ovat tyytymättömiä ratkaisuun!", kunkku.suvut)},
				"En näe miten tämä ongelma koskee minua."));
		
		//Ongelma(String nimi, String selitys, Suku esittelijaSuku, ArrayList<Paatos> paatokset)
		
		kunkku.ongelmat.add(new Ongelma("Maanjäristys",
				"Jumalat ovat vihaisia meille. Kaikkia sukuja on kohdannut onnettomuus ja heidän "
						+ "tilansa ovat kokeneet suurta vahinkoa. Miten toimimme?",
				kunkku.annaAatelisin(), paatokset));
		
		//Ongelma: Maakiista! Maalainen on ojittanut pellon ja rakentanut talon kirkon maalle...
		//...mutta kirkko haluaa maat
		
		paatokset = new ArrayList<Paatos>();

		ArrayList<Suku> maalaiset = new ArrayList<Suku>(kunkku.etsiSukuTyypit(false, false, false, false, true));
		
		System.out.println(maalaiset.size());
		
		ArrayList<Suku> uskonnolliset = new ArrayList<Suku>(kunkku.etsiSukuTyypit(false, false, true, false, false));
		
		ArrayList<Suku> aateliset = new ArrayList<Suku>(kunkku.etsiAateliset());
		
		x = maalaiset.get(r.nextInt(maalaiset.size())); //arvotaan x maalaissuku
		Suku y = uskonnolliset.get(r.nextInt(uskonnolliset.size())); // arvotaan y uskonnollissuku

		ArrayList<Suku> xList = new ArrayList<Suku>(); //lista jossa on vain x
		xList.add(x);

		ArrayList<Suku> yList = new ArrayList<Suku>(); //lista jossa on vain y
		yList.add(y);

		ArrayList<Suku> xyList = new ArrayList<Suku>(); //lista jossa on x ja y
		xyList.add(x);
		xyList.add(y);

		paatokset.add(
			new Paatos(
				new Vaatimus[]{new Vaatimus(Tyyppi.NULL, 0, null, null)},
				new Seuraus[]{
					new Seuraus(Tyyppi.SUKUSUHDE, 15, "Maalaissuku " + x.annaNimi() + " kiittää ", xList),
					new Seuraus(Tyyppi.SUKUSUHDE,  5, "ja muistakin maalaisista tuntuu kivalta", maalaiset),
					new Seuraus(Tyyppi.SUKUSUHDE,-10, "Suku " + y.annaNimi() + " ei tykkää yhtään", yList),
					new Seuraus(Tyyppi.SUKUSUHDE, -5, "Eikä myöskään kirkko tykkää", uskonnolliset),
					new Seuraus(Tyyppi.SUKUVALIT,-15, x.annaNimi() + " ja " + y.annaNimi() + " eivät tykkää toisistaan", xyList)
				},
				"Maa kuuluu sille, joka on sen eteen eniten tehnyt töitä. Saatte maan omistuksen nimiinne!"
			)
		);

		paatokset.add(
			new Paatos(
				new Vaatimus[]{new Vaatimus(Tyyppi.NULL, 0, null, null)},
				new Seuraus[]{
					new Seuraus(Tyyppi.SUKUSUHDE, 20, "Kirkko on tyytyväinen, että maaoikeuksia kunnioitetaan", uskonnolliset),
					new Seuraus(Tyyppi.SUKUSUHDE, -10, "Maalaissuvun edustaja raahataan itkevänä pois", xList),
					new Seuraus(Tyyppi.SUKUSUHDE, 1, "Aateliset tykkää aina, kun duunaria kyykytetään", aateliset)
				},
				"Olette vaatimassa itsellenne maita joita ette omista. Rankaisen teitä 20 ruoskaniskulla." //uskonnolliset +20 ja x -20
			)
		);

		paatokset.add(
			new Paatos(
				new Vaatimus[]{new Vaatimus(Tyyppi.NULL, 0, null, null)},
				new Seuraus[]{
					new Seuraus(Tyyppi.RUOKA_T, 1, "Maalaissuku tekee hommia ankarammin, jotta vuokra saadaan maksettua.")
				},
				"Vuokrasopimus on kirjattava ja kohtuullistettava. Vuokraksi riittää vain kaksi viljasäkkiä vuodessa" //x -10 ruoka +10
			)
		);

		paatokset.add(
			new Paatos(
				new Vaatimus[]{ new Vaatimus(Tyyppi.NULL, 0, null, null) },
				new Seuraus[]{
					new Seuraus(Tyyppi.SUKUSUHDE, 10, "Riitely jatkuu viikkoja, mutta lopulta kirkko saa tahtonsa läpi", yList),
					new Seuraus(Tyyppi.SUKUVALIT, -5, "Riitelyn kauna leviää myös muihin sukuihin", aateliset, maalaiset)
				},
				"(Älä tee mitään)" 
			)
		);

		kunkku.ongelmat.add(
			new Ongelma("Maakiista","Suvun " + x.annaNimi() + " edustaja " + x.annaEdustaja() + " on ojittanut pellon ja rakentanut talon kirkon maalle. Kirkon suvun \n" 
					+ y.annaNimi() + " kanssa on ollut suullinen sopimus, että joutomaalle rakennettu talo"+
					" ja osa pellosta siirtyisi isännän " + x.annaEdustaja() + " omistukseen, mikäli hänen peltonsa tuottaisi hyvin.\nNyt suku "+y.annaNimi()+
					" on pakkolunastamassa kaiken maansa takaisin lupauksista huolimatta ja tarjoamassa kallista vuokrasopimusta sen sijaan."
					, x
					,paatokset)
		);
	}
	
	static void cloneSukuLista(ArrayList<Suku> source, ArrayList<Suku> target) {
		for ( Suku suku : source) {
			target.add(suku);
		}
	}
}