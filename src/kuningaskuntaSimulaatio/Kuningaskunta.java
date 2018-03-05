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
		
		/*
       	Nimi: Maanjäristys (TOTEUTETTU)

		Selitys: "Jumalat ovat vihaisia meille. Kaikkia sukuja on kohdannut onnettomuus ja heidän tilansa ovat kokeneet suurta vahinkoa. Miten toimimme?"

		Kohteet: Kaikki

		Paatokset: 	 	1. "Voin auttaa teitä korjaustöissä viidellä kullalla per suku." (jos varaa)
           	 			2. "Kansa on selvästikkin käsittänyt jotain väärin. Annas kun selitän, mistä tämä johtui." (jos hyvissä väleissä maagisen suvun kanssa)
           	 			3. "Jos jumalat ovat tosiaan niin vihaisia meidän on uhrattava neitsyt (x) suvulta mitä pikimmiten." (jos hyvissä väleissä uskonnollisen suvun kanssa)
           	 			4. "En näe miten tämä ongelma koskee minua."

		Vaikutukset: 	1. -5 kultaa per suku, +20 vaikutettujen sukujen välit.
			 			2. +10 Ei uskonnolisten sukujen välit, -10 uskonnolisten sukujen välit kuninkaaseen ja maagiseen sukuun, jolta kuningas sai idean. (paitsi jos myös maaginen).
             			3. +10 Vaikutettujen sukujen välit paitsi -10 neitsyen suvun välit kuninkaaseen ja uskonnoliseen sukuun, jolta kuningas sai idean.
             			4. -20 Kaikkien sukujen välit.
       	*/
		
		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.RAHA, kunkku.annaSukujenLKM() * 5) },
				new Seuraus[] { new Seuraus(Tyyppi.RAHA, -(kunkku.annaSukujenLKM() * 5)),
						new Seuraus(Tyyppi.SUKUSUHDE, 20, kunkku.suvut) },
				"Voin auttaa teitä korjaustäissä viidellä kullalla per suku.",
				"Kaikki suvut tykkäävät, vaikka olet nyt hiukan köyhempi kuningas!"));
		
		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.SUKUSUHDE, 1, kunkku.etsiSukuTyypit(true, false, false, false, false).get(0), null) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.suvut),
						new Seuraus(Tyyppi.SUKUSUHDE, -20, kunkku.etsiSukuTyypit(false, false, true, false, false)),
						new Seuraus(Tyyppi.SUKUSUHDE, 20, kunkku.etsiSukuTyypit(false, false, true, true, false))},
				//Puuttuu vielä sukujen väliset muutokset//,
				"Kansa on selvästikkin käsittänyt jotain väärin. Annas kun selitän, mistä tämä johtui.",
				"Kaikki suvut tykkäävät selityksestäsi paitsi uskonnolliset suvut joiden uskomuksia se loukkaa."));

		Random r = new Random();
		Suku x = kunkku.suvut.get(r.nextInt(kunkku.suvut.size()));
		Suku y = kunkku.etsiSukuTyypit(false, false, true, false, false).get(0);
		String neitsyenKoti = x.annaNimi();
		ArrayList<Suku> uhri = new ArrayList<Suku>(Arrays.asList(x));
		// Huumori mielessä jätimem mahdolliseksi tapattaa ehdottajan tyttären xD
		
		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.SUKUSUHDE, 1, y) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.suvut),
						new Seuraus(Tyyppi.SUKUSUHDE, -20, uhri),
						new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.suvut),
						new Seuraus(Tyyppi.SUKUVALIT, -20, new ArrayList<Suku>(Arrays.asList(x, y)))},
				"Jos jumalat ovat tosiaan niin vihaisia meidän on uhrattava neitsyt " + neitsyenKoti + " suvulta mitä pikimmiten.",
				x.annaNimi() + " suku itkee kauniin neitsyensä kohtalosta ja suuttui " + x + " suvulle. Muut suvut huokaisevat helpotuksesta,"+
					" koska heihin ei kohdistu vaaraa."));
		
		paatokset.add(new Paatos(new Vaatimus[] { new Vaatimus(Tyyppi.NULL, 0) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, -20, kunkku.suvut)},
				"En näe miten tämä ongelma koskee minua.",
				"Kaikki ovat tyytymättömiä ratkaisuun!"));
		
		//Ongelma(String nimi, String selitys, Suku esittelijaSuku, ArrayList<Paatos> paatokset)
		
		kunkku.ongelmat.add(new Ongelma("Maanjäristys",
				"Jumalat ovat vihaisia meille. Kaikkia sukuja on kohdannut onnettomuus ja heidän "
						+ "tilansa ovat kokeneet suurta vahinkoa. Miten toimimme?",
				kunkku.annaAatelisin(), paatokset));
		
		/*Nimi: Maakiista (TOTEUTETTU)

		Selitys: "X:n isäntä Jussi on ojittanut pellon ja rakentanut talon kirkon maalle. Kirkon (Korkein uskonnollinen suku Y) kanssa on ollut suullinen sopimus, että joutomaalle rakennettu talo ja osa pellosta siirtyisi isännän Jussin omistukseen, mikäli hänen peltonsa tuottaisi hyvin.
			Nyt Y on pakkolunastamassa kaiken maansa takaisin lupauksista huolimatta ja tarjoamassa kallista vuokrasopimusta sen sijaan."

		Kohteet: Maanviljelijäsuku

		Paatokset:	1. "Maa kuuluu sille, joka on sen eteen eniten tehnyt töitä. Saatte maan omistuksen nimiinne!"
					2. "Vuokrasopimus on kohtuullistettava ja vuokran määräksi riittää vain kaksi viljasäkkiä vuodessa"
		            3. "Olette vaatimassa itsellenne maita joita ette omista. Rankaisen teitä 20 ruoskaniskulla."
		            4. "(Älä tee mitään)"

		Vaikutukset:	1. Muutaman muun (satunnaisesti valitun) maalaissuuku +3 ja heidän tuottavuutensa kasvaa. 
										Suhde: Maalaissuku - Uskonnollinen suku +10
										Suhde: Uskonnollinen suku - Kuningas -20
						2. Uskonnollisten sukujen kunnioitus +20
		            	3. X:n kunnioitus -10 ruoka +10
		            	4. Uskonnollisten sukujen kunnioitus +10 */
		
		paatokset = new ArrayList<Paatos>();

		ArrayList<Suku> maalaiset = new ArrayList<Suku>(kunkku.etsiSukuTyypit(false, false, false, false, true));
		
		System.out.println(maalaiset.size());
		
		ArrayList<Suku> uskonnolliset = new ArrayList<Suku>(kunkku.etsiSukuTyypit(false, false, true, false, false));
		
		ArrayList<Suku> aateliset = new ArrayList<Suku>(kunkku.etsiAateliset());
		
		x = maalaiset.get(r.nextInt(maalaiset.size())); //arvotaan x maalaissuku
		y = uskonnolliset.get(r.nextInt(uskonnolliset.size())); // arvotaan y uskonnollissuku

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
						new Seuraus(Tyyppi.SUKUSUHDE, 15, xList),
						new Seuraus(Tyyppi.SUKUSUHDE,  5, maalaiset),
						new Seuraus(Tyyppi.SUKUSUHDE,-10, yList),
						new Seuraus(Tyyppi.SUKUSUHDE, -5, uskonnolliset),
						new Seuraus(Tyyppi.SUKUVALIT,-15, xyList)
					},"Maa kuuluu sille, joka on sen eteen eniten tehnyt töitä. Saatte maan omistuksen nimiinne!"
	                ,"Maalaissuku " + x.annaNimi() + " kiittää ja muistakin maalaisista tuntuu kivalta."
	              	+"Suku " + y.annaNimi() + " ja kirkko ei tykkää yhtään ja " + x.annaNimi() 
	                + " ja " + y.annaNimi() + " eivät tykkää toisistaan"
				)
			);

			paatokset.add(
				new Paatos(
					new Vaatimus[]{new Vaatimus(Tyyppi.NULL, 0, null, null)},
					new Seuraus[]{
						new Seuraus(Tyyppi.SUKUSUHDE, 20, uskonnolliset),
						new Seuraus(Tyyppi.SUKUSUHDE, -10, xList),
						new Seuraus(Tyyppi.SUKUSUHDE, 1, aateliset)
					},
					"Olette vaatimassa itsellenne maita joita ette omista. Rankaisen teitä 20 ruoskaniskulla." //uskonnolliset +20 ja x -20
	              	, "Kirkko on tyytyväinen, että maaoikeuksia kunnioitetaan"
	              	+ "Maalaissuvun " + x.annaEdustaja() + " raahataan itkevänä pois. "
	              	+ "Positiivisena puolena on, että aateliset tykkää aina, kun duunaria kyykytetään."
				)
			);

			paatokset.add(
				new Paatos(
					new Vaatimus[]{new Vaatimus(Tyyppi.NULL, 0, null, null)},
					new Seuraus[]{
						new Seuraus(Tyyppi.RUOKA_T, 1)
					},
					"Vuokrasopimus on kirjattava ja kohtuullistettava. Vuokraksi riittää vain kaksi viljasäkkiä vuodessa" //x -10 ruoka +10
	              	, "Maalaissuku tekee hommia ankarammin, jotta vuokra saadaan maksettua."
				)
			);

			paatokset.add(
				new Paatos(
					new Vaatimus[]{ new Vaatimus(Tyyppi.NULL, 0, null, null) },
					new Seuraus[]{
						new Seuraus(Tyyppi.SUKUSUHDE, 10, yList),
						new Seuraus(Tyyppi.SUKUVALIT, -5, aateliset, maalaiset)
					},
					"(Älä tee mitään)"
	              	,"Riitely jatkuu viikkoja, mutta lopulta kirkko saa tahtonsa läpi ja maalaisille jää karvas maku suuhun."
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