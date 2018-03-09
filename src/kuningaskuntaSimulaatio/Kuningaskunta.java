package kuningaskuntaSimulaatio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

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

	/**
	 * Luo kaikki pelin ongelmat, pitää kutsua joka kerta ennen kuin pelissä tarvitaan ongelmia.
	 * Tässä metodissa on pääasiassa kaikki pelin sisältö, joka on niputettu kunkku.ongelmat listaan.
	 * @author Pasi Toivanen, Tommi Heikkinen, Santeri Loitomaa
	 * @param kunkku
	 */
	public static void meillaOnOngelmia(Kuningas kunkku) {
		ArrayList<Paatos> paatokset = new ArrayList<Paatos>();

		//Paatos(Vaatimus[] v, Seuraus[] s, String viesti)
		//Vaatimus(Tyyppi tyyppi, int arvo [{, Suku kohde}, Suku kohde2])
		//Seuraus(Tyyppi tyyppi, int arvo, String kuvaus[, ArrayList<Suku> kohde])
    	//ArrayList<Suku> etsiSukuTyypit(boolean magia, boolean sotilas, boolean uskonnollinen, boolean kauppias, boolean maalainen)
		
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
      
      
		paatokset.add(new Paatos(
          		new Vaatimus[] { new Vaatimus(Tyyppi.RAHA, kunkku.annaSukujenLKM() * 5) },
				new Seuraus[] { new Seuraus(Tyyppi.RAHA, -(kunkku.annaSukujenLKM() * 5)),
						new Seuraus(Tyyppi.SUKUSUHDE, 20, kunkku.suvut) },
				"Voin auttaa teitä korjaustäissä viidellä kullalla per suku. ("+ (kunkku.annaSukujenLKM()*5) +" kultaa)",
				"Kaikki suvut tykkäävät, vaikka olet nyt hiukan köyhempi kuningas!"));
		
		paatokset.add(new Paatos(
          		new Vaatimus[] { new Vaatimus(Tyyppi.SUKUSUHDE, 1, kunkku.etsiSukuTyypit(true, false, false, false, false).get(0), null) },
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
		
		paatokset.add(new Paatos(
          		new Vaatimus[] { new Vaatimus(Tyyppi.SUKUSUHDE, 1, y) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.suvut),
						new Seuraus(Tyyppi.SUKUSUHDE, -20, uhri),
						new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.suvut),
						new Seuraus(Tyyppi.SUKUVALIT, -20, new ArrayList<Suku>(Arrays.asList(x)), new ArrayList<Suku>(Arrays.asList(y)))},
				"Jos jumalat ovat tosiaan niin vihaisia meidän on uhrattava neitsyt " + neitsyenKoti + " suvulta mitä pikimmiten.",
				x.annaNimi() + " suku itkee kauniin neitsyensä kohtalosta ja suuttui " + y.annaNimi() + " suvulle. \nMuut suvut huokaisevat helpotuksesta,"+
					" koska heihin ei kohdistu vaaraa."));
		
		paatokset.add(new Paatos(
          		new Vaatimus[] { new Vaatimus(Tyyppi.NULL, 0) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, -20, kunkku.suvut)},
				"En näe miten tämä ongelma koskee minua.",
				"Kaikki ovat tyytymättömiä ratkaisuun!"));
		
		//Ongelma(String nimi, String selitys, Suku esittelijaSuku, ArrayList<Paatos> paatokset)
		
		kunkku.ongelmat.add(new Ongelma("Maanjäristys",
				"Jumalat ovat vihaisia meille. Kaikkia sukuja on kohdannut onnettomuus ja heidän "
						+ "tilansa ovat kokeneet suurta vahinkoa. Miten toimimme?",
				kunkku.annaAatelisin(), paatokset));
		
		/*Nimi: Maakiista (TOTEUTETTU)

		Selitys: "X:n isäntä Jussi on ojittanut pellon ja rakentanut talon kirkon maalle. Kirkon (Korkein uskonnollinen suku Y) kanssa on ollut suullinen sopimus,
        että joutomaalle rakennettu talo ja osa pellosta siirtyisi isännän Jussin omistukseen, mikäli hänen peltonsa tuottaisi hyvin.
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

		paatokset.add(
			new Paatos(
				new Vaatimus[]{new Vaatimus(Tyyppi.NULL, 0)},
				new Seuraus[]{
					new Seuraus(Tyyppi.SUKUSUHDE, 15, xList),
					new Seuraus(Tyyppi.SUKUSUHDE,  5, maalaiset),
					new Seuraus(Tyyppi.SUKUSUHDE,-10, yList),
					new Seuraus(Tyyppi.SUKUSUHDE, -5, uskonnolliset),
					new Seuraus(Tyyppi.SUKUVALIT,-15, xList, yList)
				},"Maa kuuluu sille, joka on sen eteen eniten tehnyt töitä. Saatte maan omistuksen nimiinne!"
                ,"Maalaissuku " + x.annaNimi() + " kiittää ja muistakin maalaisista tuntuu kivalt\n"
              	+"Suku " + y.annaNimi() + " ja kirkko ei tykkää yhtään ja " + x.annaNimi() 
                + " ja " + y.annaNimi() + " eivät tykkää toisistaan."
			)
		);

		paatokset.add(
			new Paatos(
				new Vaatimus[]{new Vaatimus(Tyyppi.NULL, 0)},
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
				new Vaatimus[]{new Vaatimus(Tyyppi.NULL, 0)},
				new Seuraus[]{
					new Seuraus(Tyyppi.RUOKA_T, 1)
				},
				"Vuokrasopimus on kirjattava ja kohtuullistettava. Vuokraksi riittää vain kaksi viljasäkkiä vuodessa" //x -10 ruoka +10
              	, "Maalaissuku tekee hommia ankarammin, jotta vuokra saadaan maksettua."
			)
		);

		paatokset.add(
			new Paatos(
				new Vaatimus[]{ new Vaatimus(Tyyppi.NULL, 0) },
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

/*
Nimi: Tuhopoltto (TOTEUTETTU)

Selitys: "Viimeisen viikon sisään on 4 aatelisen tiluksilla ollut tuhoisia tulipaloja, tällaista sattumaa tuskin on olemassa, joten jokin ulkopuolinen aiheuttaa ongelman. Miten selvitämme tilanteen?"

Kohteet: 4 sattumanvaraista sukua joilla on aatelisuusastetta.

Paatokset:	 1. "Järjestetään sotilaat vartioimaan alueita, nappaamme polttajan kyllä!" (jos hyvissä väleissä sotilassuvun kanssa)
			 2. "Korvataan vahingot kullalla, kaikki asiat voidaan ratkaista sillä." (jos varaa)
             3. "Uskottelemme heille tekevämme kaikkemme, eiköhän se riitä. Kyllä polttaja lopulta rauhoittuu" (jos hyvissä väleissä aatelissukujen kanssa)
             4. "Aatelisten ongelma se on, ei minun."

Vaikutukset: 1. +10 aateliskohteiden välit, -10 liittolaissotilaiden välit.
			 2. -20 kultaa, +10 välit aatelissukujen kanssa
             3. +10 atteliskohteiden välit -20 muiden aatelissukujen välit 
             4. -20 Aatelissukujen välit
*/
		paatokset = new ArrayList<Paatos>();     
			paatokset.add(
  				new Paatos(
    				new Vaatimus[] {new Vaatimus(Tyyppi.SUKUSUHDE,1,kunkku.etsiSukuTyypit(false,true,false,false,false).get(0)) },
  					new Seuraus[]{
      					new Seuraus(Tyyppi.SUKUSUHDE, -10,kunkku.etsiSukuTyypit(false,true,false,false,false)),
      					new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.etsiAateliset()),
      					new Seuraus(Tyyppi.SUKUVALIT, -10, kunkku.etsiSukuTyypit(false,true,false,false,false), kunkku.etsiAateliset())
    				},
  					"Järjestetään sotilaat vartioimaan alueita, nappaamme polttajan kyllä!",
  					"Tuhopoltot loppuivat, mutta sotilassuvut joutuivat ylitöihin."
  				)
    		);
      
      	paatokset.add(
      		new Paatos(
        		new Vaatimus[] {new Vaatimus(Tyyppi.RAHA, 50)},
        		new Seuraus[]{
              		new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.etsiAateliset()),
              		new Seuraus(Tyyppi.RAHA, -50),
              		new Seuraus(Tyyppi.SUKUPOPULAATIO, -2, kunkku.etsiAateliset())
            	},
        		"Korvataan vahingot kullalla, kaikki asiat voidaan ratkaista sillä.",
        		"Korjasit tuhot kerta toisensa jälkeen, paria kuollutta lukuunottamatta kaikki olivat tyytyväisiä."
        	)
      	);
      ArrayList<Suku> suosikki = new ArrayList<Suku>();
      suosikki.add(kunkku.etsiAateliset().get(0));
      	paatokset.add(
      		new Paatos(
            	new Vaatimus[] { new Vaatimus(Tyyppi.SUKUSUHDE,1,kunkku.etsiAateliset().get(0))},
            	new Seuraus[]{
                  new Seuraus (Tyyppi.SUKUSUHDE,30,suosikki),
                  new Seuraus (Tyyppi.SUKUSUHDE,-10,kunkku.etsiAateliset()),
                  new Seuraus (Tyyppi.SUKUPOPULAATIO, -5, kunkku.etsiAateliset()),
                  new Seuraus (Tyyppi.SUKUPOPULAATIO, +5,suosikki)
                },
              	"Kröhöm, yritämme toki kaikkemme ongelman ratkaisuun.... mutta tuholaiset \novat viekkaita, katsotaan onnistuuko.",
              	"Suosikkisukusi uskoi sinua, kun taas muut aateliset punovat nyrkkiä toimettomuudellesi."
            )
        );
      	paatokset.add(
      		new Paatos(
            	new Vaatimus[] {new Vaatimus(Tyyppi.NULL,0)},
            	new Seuraus[]{
                  new Seuraus (Tyyppi.SUKUSUHDE,-20,kunkku.etsiAateliset()),
                  new Seuraus (Tyyppi.SUKUPOPULAATIO, -10, kunkku.etsiAateliset())
                },
              	"Ei tämä minun ongelmani ole! Palkatkaa sotilaita, älkää tuhlatko aikaani!",
              	"Aateliset kärsivät tuhoja toisensa perään, kunens palot mystisesti loppuivat."
            )
        );
      
      kunkku.ongelmat.add(
			new Ongelma("Tuhopoltot","Suvun " + kunkku.etsiAateliset().get(0).annaNimi() + " edustaja " + kunkku.etsiAateliset().get(0).annaEdustaja() +
                    "kertoo että heidän maillaan on riehunut tuhopolttaja, joka uhkaa aateliston \nhenkiä ja arvokkaita kiinteistöjä. Mikseivät he polta peltoja?"
					, kunkku.etsiAateliset().get(0)
					,paatokset)
		);
      
/*Nimi: Lohikäärme hyökkää

Selitys:

Kohteet:

Paatokset:	1.
			2.
			3.
			4.

Vaikutukset:	1.
			2.
        		3.
        		4.

*/
      
      paatokset = new ArrayList<Paatos>();
      ArrayList<Suku> pasifistit = new ArrayList<Suku>();
      for (int i = 0; i<kunkku.suvut.size();i++){
        if (kunkku.suvut.get(i).annaSotilaallinen() == 0){
          pasifistit.add(kunkku.suvut.get(i));
        }
      }
      ArrayList<Suku> puolustus = new ArrayList<Suku>();
      for (int i = 0; i<5; i++){
        puolustus.add(kunkku.etsiSukuTyypit(false,true,false,false,false).get(i));
      }
      int puol = 0;
      for (int i=0;i<puolustus.size();i++){
        puol += puolustus.get(i).annaPopulaatio();
      }
      puol = puol/10;
      	paatokset.add(
      		new Paatos(
            	new Vaatimus[] {new Vaatimus(Tyyppi.SUKUSUHDE,40,kunkku.etsiSukuTyypit(false,true,false,false,false).get(4))},
            	new Seuraus[]{
                  new Seuraus(Tyyppi.SUKUSUHDE, -40,kunkku.etsiSukuTyypit(false,true,false,false,false)),
                  new Seuraus (Tyyppi.SUKUSUHDE, +20,kunkku.suvut),
                  new Seuraus (Tyyppi.SUKUVALIT,+30,kunkku.etsiSukuTyypit(false,true,false,false,false),pasifistit),
                  new Seuraus (Tyyppi.SUKUPOPULAATIO, -puol,puolustus)
                },
              "Komentakaa sotilaamme marssimaan lohikäärmettä vastaan, suojelemme kansaa viimeiseen asti!",
              "Sotilaat kärsivät suuria tappioita, mutta suurelta tuholta vältyttiin, \nheidän uhrauksensa musitetaan sukupolvien ajan!"
            )
        );
       ArrayList<Suku> muut = new ArrayList<Suku>();
      for (int i = 0; i<kunkku.suvut.size();i++){
        if (kunkku.suvut.get(i).annaSotilaallinen() == 0 || kunkku.suvut.get(i).annaMagia() == 0){
          muut.add(kunkku.suvut.get(i));
        }
      }
      try {
      	paatokset.add(
        	new Paatos(
            	new Vaatimus[] {new Vaatimus(Tyyppi.SUKUSUHDE,40,kunkku.etsiSukuKombo(true,true,false,false,false).get(0))},
            	new Seuraus[]{
                  new Seuraus(Tyyppi.SUKUPOPULAATIO, -20, kunkku.etsiSukuKombo(true,true,false,false,false)),
                  new Seuraus (Tyyppi.SUKUSUHDE, +20,kunkku.suvut),
                  new Seuraus (Tyyppi.SUKUVALIT, +30, muut,kunkku.etsiSukuKombo(true,true,false,false,false))
                },
            	"Maagiset joukkomme ovat experttejä tällaisten asioiden kanssa, jättäkää se heidän huolekseen.",
            	"Arkaaniset tiedot ja sotilasmahti yhdessä kukistivat lohikäärmeen ilman suurempia ongelmia."
            )
        );
      }catch(IndexOutOfBoundsException e) {
    	  paatokset.add(null);
      }
      paatokset.add(
      		new Paatos(
            	new Vaatimus[] {new Vaatimus(Tyyppi.RUOKA, 200), new Vaatimus (Tyyppi.RAHA, 200)},
            	new Seuraus[]{
                  new Seuraus(Tyyppi.RAHA,-200),
                  new Seuraus(Tyyppi.RUOKA,-200),
                  new Seuraus(Tyyppi.SUKUSUHDE, +20,kunkku.suvut)
                },
            	"Koetetaan lahjoa petoa, kenties sen raivo laantuu jos annamme \nsille ruokaa ja kultaa ilman vastustusta!",
            	"Kirennätte vyötä ja kavennatte leipää, henkenne säästyi mutta kyllä se maksoi."
            )
      	);
      int kesk =0;
      for (int i = 0; i<kunkku.suvut.size();i++) {
    	  kesk += kunkku.suvut.get(i).annaPopulaatio();
      }
      kesk = kesk/(kunkku.suvut.size()*2);
      paatokset.add(
      		new Paatos(
            	new Vaatimus[] {new Vaatimus(Tyyppi.NULL, 0)},
            	new Seuraus[]{
                  new Seuraus(Tyyppi.RAHA,-100),
                  new Seuraus(Tyyppi.RUOKA,-100),
                  new Seuraus(Tyyppi.SUKUSUHDE, -kesk,kunkku.suvut),
                  new Seuraus(Tyyppi.RAHA_T,-5),
                  new Seuraus(Tyyppi.RUOKA_T,-5),
                  new Seuraus(Tyyppi.SUKUPOPULAATIO,-20,kunkku.suvut)
                },
            	"Mitään ei ole tehtävissä, kärsikäämme kohtalomme.",
            	"Lohikäärme tuli ja poltti peltonne, varanne, ihmisenne ja ylpeytenne. \nUusi nousu tulee olemaan vaikea..."
            )
      	);
      
       kunkku.ongelmat.add(
			new Ongelma("Lohikäärme hyökkää","Suvun " + kunkku.etsiAateliset().get(0).annaNimi() + " edustaja " + kunkku.etsiAateliset().get(0).annaEdustaja() +
                    "varoittaa kuningaskuntaa lähestyvästä verenhimoisesta lohikäärmeestä, joka polttaa maat, syö ihmiset ja varastaa kullan."
					, kunkku.etsiAateliset().get(0)
					,paatokset)
			);
/*
Nimi: Riskialtis sijoitus (TOTEUTETTU)

Selitys: "Olemme hiljattain saaneet haltuumme harvinaisen hedelmälajikkeen liikekumppaniltamme. Uskomme sen viljelyn tuottavan meille suurta voittoa, tarvitsemme kuitenkin tukea isomman tilan perustamiseen. Voisitteko kenties tukea yhteistä hyväämme?"

Kohteet: Kauppias-Maanviljely suku

Paatokset:	 1. "Tottakai, paljonko tarvitsette?" (Jos varat riittävät alkusijoitukseen)
			 2. "Järjestän teille maa-alaa muilta suvuilta, saatte tilanne."
             3. "Kokeilen mielelläni tätä, mutta vain itse!"
             4. "Viekää rehunne muualle."

Vaikutukset: 1. -30 kultaa, +5 ruokatuotto ja +5 kultatuotto ja +10 tyytyväisyys kohteen kanssa.
			 2. -10 tyytyväisyys muilta maalaissuvuilta, +5 kultaa ja +10 tyytyväisyys kohteen kanssa.
             3. -10 tyytyväisyys kaikilta maalaissuvuilta, +15 ruokaa ja +10 kultaa.
             4. -10 tyytyväisyys kohteen kanssa.
*/

       	try {
		paatokset = new ArrayList<Paatos>();
      	x = kunkku.etsiSukuKombo(false, false, false, true, true).get(0);
      
      	paatokset.add(new Paatos(
          		new Vaatimus[] { new Vaatimus(Tyyppi.RAHA, 30) },
				new Seuraus[] { new Seuraus(Tyyppi.RAHA_T, 5),
                              	new Seuraus(Tyyppi.RUOKA_T, 5),
                              	new Seuraus(Tyyppi.SUKUSUHDE, 10, new ArrayList<Suku>(Arrays.asList(x))),
                              	new Seuraus(Tyyppi.RAHA, -30)},
				"Tottakai, paljonko tarvitsette? (30 kultaa)",
				"Suku pitää sinusta nyt enemmän ja tuottosi kasvoi."));
      
      	ArrayList<Suku> muutMaalaiset = kunkku.etsiSukuTyypit(false, false, false, false, true);
      	muutMaalaiset.remove(x);
      
      	paatokset.add(new Paatos(
          		new Vaatimus[] { new Vaatimus(Tyyppi.NULL, 0) },
				new Seuraus[] { new Seuraus(Tyyppi.RAHA_T, 5),
                              	new Seuraus(Tyyppi.SUKUSUHDE, 20, new ArrayList<Suku>(Arrays.asList(x))),
                              	new Seuraus(Tyyppi.SUKUSUHDE, -10, kunkku.etsiSukuTyypit(false, false, false, false, true)),
                              	new Seuraus(Tyyppi.SUKUVALIT, -10, new ArrayList<Suku>(Arrays.asList(x)), muutMaalaiset)},
				"Järjestän teille maa-alaa muilta suvuilta, saatte tilanne.",
				"Suku pitää sinusta nyt enemmän ja tuottosi kasvoi mutta muut maalaissuvut \npahastuivat ja ruuan tuotto jäi +-nollaksi."));
      
      	paatokset.add(new Paatos(
          		new Vaatimus[] { new Vaatimus(Tyyppi.NULL, 0) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, -10, kunkku.etsiSukuTyypit(false, false, false, false, true)),
                              	new Seuraus(Tyyppi.RAHA_T, 1)},
				"Kokeilen mielelläni tätä, mutta vain itse!",
				"Viljelysi ei juuri onnistunut ja kaikki maalaiset ovat tyytymättömiä ratkaisuun!"));
      
      	paatokset.add(new Paatos(
          		new Vaatimus[] { new Vaatimus(Tyyppi.NULL, 0) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, -10, new ArrayList<Suku>(Arrays.asList(x)))},
				"Viekää rehunne muualle.",
				"Suku " + x.annaNimi() + " ei pitänyt päätöksestäsi!"));
				
		kunkku.ongelmat.add(new Ongelma("Riskialtis sijoitus",
				"Olemme hiljattain saaneet haltuumme harvinaisen hedelmälajikkeen liikekumppaniltamme.\n"+
                "Uskomme sen viljelyn tuottavan meille suurta voittoa, tarvitsemme kuitenkin tukea isomman\n"+
                "tilan perustamiseen. Voisitteko kenties tukea yhteistä hyväämme?",
				x, paatokset));
       	}catch(IndexOutOfBoundsException e) {
      	
        }

/*
Nimi: Barbaarihyökkäys (TOTEUTETTU)

Selitys: Kaukaisilta mailta kuningaskuntasi läheisyyteen on tullut riehumaan kaikenkarvaisia vulgaareja ryöstäjäklaaneja, jotka
piileksivät metsissä ja hyökkäävät kyliin varastaen ja harrastaen riettauksia.

Kohteet:

Paatokset:	1. "Sotilaat ottavat kiinni ja hirttävät jokaisen villi-ihmisen"
			2. "Sotilaat ottavat kiinni ja pakkokäännyttävät kirkon kanssa yhdessä raakalaiset pakanat oikeauskoisiksi"
            3. "Sotilaat alistavat raakalaiset pakkotyöhön pelloilla"
            4. "Sotilaat ottavat kiinni ja myydään orjina naapurikuninkaille"
            5. "(Älä tee mitään)"

Vaikutukset:	1. Sotilaalliset suvut ++, raha -, 
				2. Sotilaalliset -, raha -, uskonnolliset +
                3. Sotilaalliset -, raha -, ruoka++ 
            	4. Sotilaalliset -, kauppiaat+, raha+
            	5. Maalaissuvut sukupopulaatio--, Ruuantuotto--
*/
        String[] raakalaisnimia = {"Hirmuinen Haraldi", "Valtteri Viiltäjä","Kalle Kaulankatkoja","Pasi Päidenpullottaja","Tommi Taaperoiden Tappaja","Santeri Säärien Sahaaja"};
      	String randomRaakalaisnimi = raakalaisnimia[r.nextInt(raakalaisnimia.length)];
      	
      	x = maalaiset.get(r.nextInt(maalaiset.size())); //arvotaan x maalaissuku
      	xList = new ArrayList<Suku>(); //lista jossa on vain x
		xList.add(x);
      
        paatokset = new ArrayList<Paatos>();
        
        //tätä tarvitaan viimeisessä seurauksessa
        int maalaisiaKeskimaarin = 0;
        for ( Suku maalaissuku : maalaiset) {
        	maalaisiaKeskimaarin += maalaissuku.annaPopulaatio();
        }
        maalaisiaKeskimaarin /= maalaiset.size();
      
        paatokset.add(
            new Paatos(
                new Vaatimus[] {new Vaatimus(Tyyppi.SUKUSUHDE,-10,kunkku.etsiSukuTyypit(false,true,false,false,false).get(0)) },
                new Seuraus[]{
                  new Seuraus(Tyyppi.RAHA,-4),
                  new Seuraus(Tyyppi.SUKUSUHDE, 10,kunkku.etsiSukuTyypit(false,true,false,false,false)),
                  new Seuraus(Tyyppi.SUKUSUHDE, 3,kunkku.etsiSukuTyypit(false,false,false,false,true))
                },
                "Sotilaat ottavat kiinni ja hirttävät jokaisen villi-ihmisen!",
                "Sotilaat ovat tyytyväisiä saadessaan vuodatettua verta vihollisista \nja suku "
                + x.annaNimi()+" tuntee olonsa turvalliseksi"
            )
        );
      
      	paatokset.add(
            new Paatos(
                new Vaatimus[] {new Vaatimus(Tyyppi.SUKUSUHDE,3,kunkku.etsiSukuTyypit(false,true,false,false,false).get(0)) },
                new Seuraus[]{
                  new Seuraus(Tyyppi.RAHA,-4),
                  new Seuraus(Tyyppi.SUKUSUHDE, -3,kunkku.etsiSukuTyypit(false,true,false,false,false)),
                  new Seuraus(Tyyppi.SUKUSUHDE, +25, kunkku.etsiSukuTyypit(false, false, true, false, false))
                },
				"Sotilaat ottavat kiinni ja pakkokäännyttävät pakanalliset oikeauskoisiksi",
                "Sotilaille joudutaan maksamaan ylimääräistä vaivannäöstä, mutta uskonnolliset\n ovat erittäin iloisia, kun seurakunta laajenee."
            )
        );
        
    	paatokset.add(
            new Paatos(
                new Vaatimus[] {new Vaatimus(Tyyppi.SUKUSUHDE,3,kunkku.etsiSukuTyypit(false,true,false,false,false).get(0)) },
                new Seuraus[]{
                  new Seuraus(Tyyppi.RUOKA_T, 3),
                  new Seuraus(Tyyppi.SUKUSUHDE, -3,kunkku.etsiSukuTyypit(false,true,false,false,false)),
                  new Seuraus(Tyyppi.SUKUSUHDE, +25, kunkku.etsiSukuTyypit(false, false, false, false, true))
                },
                "Sotilaat pakottavat raakalaiset pakkotyöhön pelloilla",
                "Ruuantuottosi lisääntyy, mutta maalaiset eivät ole täysin tyytyväisiä, \nkun joutuvat huolehtimaan lainsuojattomista."
            )
        );
        
        paatokset.add(
            new Paatos(
                new Vaatimus[] {new Vaatimus(Tyyppi.SUKUSUHDE,3,kunkku.etsiSukuTyypit(false,true,false,false,false).get(0)) },
                new Seuraus[]{
                  new Seuraus(Tyyppi.RAHA, 10),
                  new Seuraus(Tyyppi.SUKUSUHDE, +25, kunkku.etsiSukuTyypit(false, false, false, true, false))
                },
                "Sotilaat ottavat raakalaiset kiinni ja antavat heidät kauppiaille myytäväksi orjakaupassa",
                "Orjien myynnistä saadaan paljon rahaa ja kauppiaat hierovat käsiään tyytyväisenä."
            )
        );
        
        paatokset.add(
            new Paatos(
                new Vaatimus[] {new Vaatimus(Tyyppi.NULL, 0) },
                new Seuraus[]{
                  new Seuraus(Tyyppi.SUKUPOPULAATIO, -maalaisiaKeskimaarin/2, maalaiset),
                  new Seuraus(Tyyppi.RUOKA_T, -2),
                  new Seuraus(Tyyppi.SUKUSUHDE, +10, aateliset)
                },
                "(Älä tee mitään)",
                "Puolet maalaisista kuolee raakalaisten hyökkäyksissä, mutta aatelisiahan se tunnetusti ei haittaa"
            )
        );
      
		kunkku.ongelmat.add(
			new Ongelma("Barbaarihyökkäys", randomRaakalaisnimi + " on hyökännyt " + x.annaNimi() + " suvun farmille!"
					, x
					,paatokset)
		);
		
/*Nimi: Rypäleet loppuneet

Selitys: "Aatelissuvulta x on illallispöydässä loppunut viinirypäleet
					aatelissuvun edustaja on tullut pyytämään lisää"
Kohteet: yksi aatelissuku

Paatokset:	1. "Lähetän kauppiaita hakemaan parhaimpia viinirypäleitä" (jos kauppiaihin on hyvät suhteet)
			2. "Maanviljelijät viljelkööt tästä lähtien useamman palstan viinirypäleitä"
            3. "Hahhahhah! Syökööt vaikka perunaa rypäleiden tilalla"
            4. "(Älä tee mitään)"

Vaikutukset:	1. kauppias-, x++, raha-
			2. RUOKA_T+, RAHA_T+, X-- (koska pitäisi saada heti)
            	3. X:n kunnioitus -10, raha+, maalaiset+
            	4. x-, maalaiset+	*/
		
		
		paatokset = new ArrayList<Paatos>();
		
		x = kunkku.etsiAateliset().get(r.nextInt(kunkku.etsiAateliset().size()));
		xList = new ArrayList<Suku>();
		xList.add(x);
		
		paatokset.add(
	        new Paatos(
	            new Vaatimus[] { new Vaatimus(Tyyppi.SUKUSUHDE,5,kunkku.etsiSukuTyypit(false,false,false,true,false).get(0)) },
	            new Seuraus[]{
	              new Seuraus(Tyyppi.RAHA,-4),
	              new Seuraus(Tyyppi.SUKUSUHDE, -2,kunkku.etsiSukuTyypit(false,false,false,true,false)),
	              new Seuraus(Tyyppi.SUKUSUHDE, 20, xList)
	            },
	            "Lähetän kauppiaita hakemaan parhaimpia viinirypäleitä",
	            "Kauppiaat eivät ole kovinkaan riemuissaan ylimääräisistä vaarallisista kaupparetkistä \n"
	            + ", mutta " + x.annaEdustaja() + " itkahtaa ilosta, kun kuninkaalta löytyy rahaa ostaa \n"
	            + "lisää viinirypäleitä!"
	        )
	    );
	    
		paatokset.add(
	        new Paatos(
	            new Vaatimus[] {new Vaatimus(Tyyppi.SUKUSUHDE,-3,kunkku.etsiSukuTyypit(false,false,false,false,true).get(0)) },
	            new Seuraus[]{
	              new Seuraus(Tyyppi.RAHA_T,1),
	              new Seuraus(Tyyppi.RUOKA_T,1),
	              new Seuraus(Tyyppi.SUKUSUHDE, -5,xList)
	            },
	            "Maanviljelijät viljelkööt tästä lähtien useamman palstan viinirypäleitä.",
	            "Rahan ja ruuan tuotanto lisääntyy, kun viinirypäletilukset alkavat kantaa hedelmää\n ja kauppiaat saavat "
	            + "uusia vientituotteita. Rypäleiden saantiin kestää kuitenkin tovi, \n"
	            + "jolloin suku " + x.annaNimi() + " joutuu pitämään monta illallista ilman herkullisia rypäleitä."
	        )
	    );
	    
	    paatokset.add(
	        new Paatos(
	            new Vaatimus[] {new Vaatimus(Tyyppi.NULL, 0)},
	            new Seuraus[]{
	              new Seuraus(Tyyppi.RAHA,2),
	              new Seuraus(Tyyppi.SUKUSUHDE, -15,xList),
	              new Seuraus(Tyyppi.SUKUSUHDE, 10, maalaiset)
	            },
	            "Ahhahhahha! Syökööt vaikka perunaa rypäleiden sijasta. Säästyypähän nekin rahat!",
	            "Rahaa säästyy, mutta vain vähän. Suku " + x.annaNimi() + " on kovin loukkaantunut \n"
	            + "vastauksestasi, mutta huhu pihtaruudestasi kiirii maalaisten korviin, mikä \n"
	            + "tekee sinusta suositumman maalaisten piireissä."
	        )
	    );
	    
	    paatokset.add(
	        new Paatos(
	            new Vaatimus[] {new Vaatimus(Tyyppi.NULL, 0)},
	            new Seuraus[]{
	              new Seuraus(Tyyppi.RAHA,2),
	              new Seuraus(Tyyppi.SUKUSUHDE, -3,xList)
	            },
	            "(Älä tee mitään)",
	            "Edustaja " + x.annaEdustaja() + " poistuu paikalta surullisin mielin. Sen päätöksen\n "
	            + "ei enää pitkään aikaan syöty rypäleitä."
	        )
	    );
		
		kunkku.ongelmat.add(
			new Ongelma("Ne on loppu nyt", "Aatelissuvulta " + x.annaNimi() + " on illallispöydässä loppunut viinirypäleet \n"
											+ "aatelissuvun edustaja " + x.annaEdustaja() + " on tullut häntä koipien välissä \n"
											+ "pyytämään, että voisit lähettä jonkun hakemaan herkkuja lisää pöytään, \n"
											+ "koska muuten illallisella ei voi tarjota sitä parasta mitä maailmasta löytyy"
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