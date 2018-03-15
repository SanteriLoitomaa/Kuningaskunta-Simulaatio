package kuningaskuntaSimulaatio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Kuningaskunta{

	/**
	 * main()-metodi toimii tässä pelissä päävalikkona, jonka avulla voi...
		1. aloittaa uuden pelin
		2. jatkaa siitä, mihin jäit
		3. nähdä parhaimmat pisteet
		4. nollata pisteet
		5. poistua pelistä
		6. syöttää huijauskoodin (1337)
	 * @param args
	 * @author Santeri Loitomaa
	 */
	public static void main(String[] args) {
		Scanner vastaus = new Scanner(System.in);
		Scanner scan = new Scanner(System.in);
		while(true) {
			try {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
			System.out.println("Welcome to the Kingdom Simulator.");
			System.out.println("1. Start a new game (Old one will be lost)");
			System.out.println("2. Continue");
			System.out.println("3. Top-10");
			System.out.println("4. Reset points");
			System.out.println("5. Exit the game");
			while (!vastaus.hasNextInt()) {
				vastaus.next();
			}
			int vast = vastaus.nextInt();
			// Peli alkaa tästä
			if(vast == 1) {
				try {
					new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
				System.out.println("How would you like to be called, your majesty?");
				String nimi = "";
				nimi = scan.nextLine();
				System.out.println();
				System.out.println("How many years do you plan on ruling over us? (1 month, 1 event)");
				while (!vastaus.hasNextInt()) {
					vastaus.next();
				}
				int vuorot = vastaus.nextInt();
				System.out.println();
				vuorot = vuorot * 12;
				Kuningas kunkku = new Kuningas(nimi, vuorot);
				meillaOnOngelmia(kunkku);
				System.out.println("It is an honor to meet you, " + kunkku.annaNimi());
				System.out.println();
				kunkku.scan(vastaus);
				kunkku.vuorokierto(0);
			}
			// Pelin lataus
			if(vast == 2) {
				try {
					new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
				Kuningas kunkku = new Kuningas("Bug king", 1);
				try {
					kunkku = TallennaLataaPisteet.lataa();
					if(kunkku == null) {
						System.out.println("Your save is corrupted or it doesn't exist.");
						continue;
					}
				}catch(FileNotFoundException | ClassNotFoundException | NullPointerException e) {
					System.out.println("Your save is corrupted or it doesn't exist.");
					continue;
				}
				System.out.println("Glad to have you back, " + kunkku.annaNimi());
				kunkku.scan(vastaus);
				kunkku.vuorokierto(kunkku.annaNykyinenVuoroIndex());
			}
			// Parhaat pisteet
			if(vast == 3) {
				try {
					new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
				TallennaLataaPisteet.tulostaPisteet();
			}
			// Resetoi pisteet
			if(vast == 4) {
				try {
					new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
				TallennaLataaPisteet.luoPisteet();
			}
			// Poistu pelistä
			if(vast == 5) {
				try {
					new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
				break;
			}
			// Huijauskoodi
			if(vast == 1337) {
				try {
					new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
				Kuningas kunkku = new Kuningas("King Arthur", 80);
				kunkku.asetaRaha(10000);
				kunkku.asetaRuoka(10000);
				kunkku.asetaRahaTuotto(10);
				kunkku.asetaRuokaTuotto(10);
				for(Suku s : kunkku.suvut) {
					s.asetaSuhdeKuninkaaseen(80);
				}
				kunkku.tulostaSuvut();
				meillaOnOngelmia(kunkku);
				System.out.println("It is an honor to meet you, " + kunkku.annaNimi());
				kunkku.scan(vastaus);
				kunkku.vuorokierto(0);
			}
		}
		vastaus.close();
		scan.close();
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
				"I will give you 5 gold each for repairs. ("+ (kunkku.annaSukujenLKM()*5) +" gold)",
				"Everyone likes you a little more now!"));
		
		ArrayList<Suku> uskot = new ArrayList<Suku>(kunkku.etsiSukuTyypit(false, false, true, false, false));
		ArrayList<Suku> maagit = new ArrayList<Suku>(kunkku.etsiSukuTyypit(true, false, false, false, false));
		try {
			ArrayList<Suku> molemmat = new ArrayList<Suku>(kunkku.etsiSukuKombo(true, false, true, false, false));
			for(Suku s : molemmat) {
				uskot.remove(s);
				maagit.remove(s);
			}
		}catch(NullPointerException e) {
			
		}
		
		paatokset.add(new Paatos(
          		new Vaatimus[] { new Vaatimus(Tyyppi.SUKUSUHDE, 1, kunkku.etsiSukuTyypit(true, false, false, false, false).get(0), null) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.suvut),
						new Seuraus(Tyyppi.SUKUSUHDE, -20, uskot),
						new Seuraus(Tyyppi.SUKUSUHDE, 20, maagit),
						new Seuraus(Tyyppi.SUKUVALIT, -20, uskot, maagit)},
				"People have misunderstood. This is no work of a god. Let me explain...",
				"Everyone, except for the religious families, likes you more now."));

		Random r = new Random();
		Suku x = kunkku.suvut.get(r.nextInt(kunkku.suvut.size()));
		Suku y = kunkku.etsiSukuTyypit(false, false, true, false, false).get(0);
		while (x.equals(y)) {
			x = kunkku.suvut.get(r.nextInt(kunkku.suvut.size()));
		}
		String neitsyenKoti = x.annaNimi();
		ArrayList<Suku> uhri = new ArrayList<Suku>(Arrays.asList(x));
		// Huumori mielessä jätimem mahdolliseksi tapattaa ehdottajan tyttären xD
		
		paatokset.add(new Paatos(
          		new Vaatimus[] { new Vaatimus(Tyyppi.SUKUSUHDE, 1, y) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.suvut),
						new Seuraus(Tyyppi.SUKUSUHDE, -20, uhri),
						new Seuraus(Tyyppi.SUKUSUHDE, 10, kunkku.suvut),
						new Seuraus(Tyyppi.SUKUVALIT, -20, new ArrayList<Suku>(Arrays.asList(x)), new ArrayList<Suku>(Arrays.asList(y)))},
				"If gods really are this mad at we will have to scrifice a virgin from the " + neitsyenKoti + " family immediatly.",
				x.annaNimi() + " family cries for the death of their virgin and blame " + y.annaNimi() + " family for it. \nEveryone else is relieved"+
					" because they are no longer in danger."));
		
		paatokset.add(new Paatos(
          		new Vaatimus[] { new Vaatimus(Tyyppi.NULL, 0) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, -20, kunkku.suvut)},
				"I don't see how this is my problem.",
				"Everyone hates you more now!"));
		
		//Ongelma(String nimi, String selitys, Suku esittelijaSuku, ArrayList<Paatos> paatokset)
		
		kunkku.ongelmat.add(new Ongelma("Earthquake",
				"Gods are mad at us! Everyone has suffered great losses of property."
						+ "What should we do?",
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
				},"The land belongs to the one who has done more to gain it. I will give you the field."
                ,"Farmer family " + x.annaNimi() + " thanks you and other farmers feel relieved as well.\n"
              	+y.annaNimi() + "family and the church don't like this at all. The hate between " + x.annaNimi() 
                + " and " + y.annaNimi() + " is mutual."
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
				"You are trying to claim a land that you do not own. This will not go unpunished." //uskonnolliset +20 ja x -20
              	, "The church is happy with the result. "
              	+ "Farmer family " + x.annaEdustaja() + " is carried away while begging for justice. "
              	+ "On a positive side note, nobles always like to see the worker suffer."
			)
		);

		paatokset.add(
			new Paatos(
				new Vaatimus[]{new Vaatimus(Tyyppi.NULL, 0)},
				new Seuraus[]{
					new Seuraus(Tyyppi.RUOKA_T, 1)
				},
				"The rent has to be regulated and lowered. 2 bags of wheat per year are more than enough." //x -10 ruoka +10
              	, "The farmers work harder to pay the rent."
			)
		);

		paatokset.add(
			new Paatos(
				new Vaatimus[]{ new Vaatimus(Tyyppi.NULL, 0) },
				new Seuraus[]{
					new Seuraus(Tyyppi.SUKUSUHDE, 10, yList),
					new Seuraus(Tyyppi.SUKUVALIT, -5, aateliset, maalaiset)
				},
				"(Stay neutral)"
              	,"The fight goes on for weeks until the church finally have their way. The farmers are not happy."
			)
		);

		kunkku.ongelmat.add(
			new Ongelma("Fight over a field",x.annaEdustaja() + " from family " + x.annaNimi() + " has drained a field and built a house on land owned by the church.\n"
					+ "They had a deal with the church's " + y.annaNimi() + " family, that if they managed to make enough profit, the field and the house built near it\n"+
					" would be given to " + x.annaEdustaja() + ". Now the "+y.annaNimi()+
					" family wants their land back and is ignoring the old deal, giving a much more expensive one instead."
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
  					"Let's have the soldiers do some extra work, we'll catch them!",
  					"The arson has ended but the soldiers had to work overtime."
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
        		"Will 50 gold cover the damage? I have other things to worry about.",
        		"You repaired the damage but a few people died."
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
              	"Well we can TRY but arsonists can be quite slippery...",
              	"Your favorite noble believed you but the other are giving you funny looks..."
            )
        );
      	paatokset.add(
      		new Paatos(
            	new Vaatimus[] {new Vaatimus(Tyyppi.NULL,0)},
            	new Seuraus[]{
                  new Seuraus (Tyyppi.SUKUSUHDE,-20,kunkku.etsiAateliset()),
                  new Seuraus (Tyyppi.SUKUPOPULAATIO, -10, kunkku.etsiAateliset())
                },
              	"This isn't my problem! Go hire some soldiers or something!",
              	"Nobles suffer from the arson for weeks, until it suddenly ends. No one ever knew why."
            )
        );
      
      kunkku.ongelmat.add(
			new Ongelma("Arson", kunkku.etsiAateliset().get(0).annaEdustaja() + " from the " + kunkku.etsiAateliset().get(0).annaNimi() + " family" +
                    " says there is an arsonist on the loose. Nobility is suffering. What to do?"
					, kunkku.etsiAateliset().get(0)
					,paatokset)
		);
      
/*Nimi: Lohikäärme hyökkää

Selitys: "Kuningaskuntaa lähestyvästä verenhimoisesta lohikäärmeestä, joka polttaa maat, syö ihmiset ja varastaa kullan."
					

Kohteet: Kaikki

Paatokset:	1. "Komentakaa sotilaamme marssimaan lohikäärmettä vastaan, suojelemme kansaa viimeiseen asti!"
			2. "Maagiset joukkomme ovat experttejä tällaisten asioiden kanssa, jättäkää se heidän huolekseen."
			3. "Koetetaan lahjoa petoa, kenties sen raivo laantuu jos annamme sille ruokaa ja kultaa ilman vastustusta!"
			4. "Mitään ei ole tehtävissä, kärsikäämme kohtalomme."

Vaikutukset:1. -20 tyytyväisyys puolustaneille, +20 tyytyväisyys muille, +30 sukuvälit puolustaneille ja muille, - puolet pulustus populasta
			2. +20 tyytyväisyys kaikille ei puolustaneille, +30 sukuvälit puolustaneiden ja muidne välillä, -10 populaatio puolustaneille
        	3. -200 kultaa, -200 ruokaa
        	4. -40 kultaa, -40 ruokaa , -5 ruoka tuottoa, -5 kultatuottoa, - 20 tyytyväisyys kaikilta, - 5 populaatiosta. 

*/
      
      paatokset = new ArrayList<Paatos>();
      try {
      ArrayList<Suku> pasifistit = new ArrayList<Suku>();
      for (int i = 0; i<kunkku.suvut.size();i++){
        if (kunkku.suvut.get(i).annaSotilaallinen() == 0){
          pasifistit.add(kunkku.suvut.get(i));
        }
      }
      	paatokset.add(
      		new Paatos(
            	new Vaatimus[] {new Vaatimus(Tyyppi.SUKUSUHDE,40,kunkku.etsiSukuTyypit(false,true,false,false,false).get(4))},
            	new Seuraus[]{
            	  new Seuraus (Tyyppi.SUKUPOPULAATIO, -20,new ArrayList<Suku>(Arrays.asList(kunkku.etsiSukuTyypit(false,true,false,false,false).get(4),kunkku.etsiSukuTyypit(false,true,false,false,false).get(3),kunkku.etsiSukuTyypit(false,true,false,false,false).get(2),kunkku.etsiSukuTyypit(false,true,false,false,false).get(1),kunkku.etsiSukuTyypit(false,true,false,false,false).get(0)))),
                  new Seuraus(Tyyppi.SUKUSUHDE, -40,kunkku.etsiSukuTyypit(false,true,false,false,false)),
                  new Seuraus (Tyyppi.SUKUSUHDE, +20,kunkku.suvut),
                  new Seuraus (Tyyppi.SUKUVALIT,+30,kunkku.etsiSukuTyypit(false,true,false,false,false),pasifistit)
                },
              "Our soldiers shall defeat this dragon. We have to protect our people to the last man!",
              "The soldiers suffer great loss, but a far greater loss was evaded. \nThey will be remembered for generations!"
            )
        );
      }catch(IndexOutOfBoundsException e) {
    	  paatokset.add(null);
      }
       ArrayList<Suku> muut = new ArrayList<Suku>();
      for (int i = 0; i<kunkku.suvut.size();i++){
        if (kunkku.suvut.get(i).annaSotilaallinen() == 0 && kunkku.suvut.get(i).annaMagia() == 0){
          muut.add(kunkku.suvut.get(i));
        }
      }
      try {
      	paatokset.add(
        	new Paatos(
            	new Vaatimus[] {new Vaatimus(Tyyppi.SUKUSUHDE,40,kunkku.etsiSukuKombo(true,true,false,false,false).get(0))},
            	new Seuraus[]{
                  new Seuraus(Tyyppi.SUKUPOPULAATIO, -10, kunkku.etsiSukuKombo(true,true,false,false,false)),
                  new Seuraus (Tyyppi.SUKUSUHDE, +20,kunkku.suvut),
                  new Seuraus (Tyyppi.SUKUVALIT, +30, muut,kunkku.etsiSukuKombo(true,true,false,false,false))
                },
            	"Our magical troops are experts at this kind of thig. Just leave it to them.",
            	"Arcane knowledge and military power defeated the dragon together without much of a problem."
            )
        );
      }catch(IndexOutOfBoundsException e) {
    	  paatokset.add(null);
      }
      paatokset.add(
      		new Paatos(
            	new Vaatimus[] {new Vaatimus(Tyyppi.RUOKA, 200), new Vaatimus (Tyyppi.RAHA, 200)},
            	new Seuraus[]{
                  new Seuraus(Tyyppi.RAHA,-80),
                  new Seuraus(Tyyppi.RUOKA,-80),
                  new Seuraus(Tyyppi.SUKUSUHDE, +20,kunkku.suvut)
                },
            	"We could try to bribe the beast. Let's give it some food and gold to calm it down.",
            	"The price was high but no one died. Let's hope it was worth it."
            )
      	);
      
      paatokset.add(
      		new Paatos(
            	new Vaatimus[] {new Vaatimus(Tyyppi.NULL, 0)},
            	new Seuraus[]{
                  new Seuraus(Tyyppi.RAHA,-40),
                  new Seuraus(Tyyppi.RUOKA,-40),
                  new Seuraus(Tyyppi.SUKUSUHDE, -20,kunkku.suvut),
                  new Seuraus(Tyyppi.RAHA_T,-5),
                  new Seuraus(Tyyppi.RUOKA_T,-5),
                  new Seuraus(Tyyppi.SUKUPOPULAATIO,-5,kunkku.suvut)
                },
            	"There's nothing to do. Let's hide and hope for the best.",
            	"The dragon burns down your resources, their source, your people and your pride. \nCan we rise from these ashes..."
            )
      	);
      
       kunkku.ongelmat.add(
			new Ongelma("A dragon attacks", kunkku.etsiAateliset().get(0).annaEdustaja() + " from the family " + kunkku.etsiAateliset().get(0).annaNimi() +
                    " warns you about a bloodthirsty dragon that might steal your gold, eat your food and burn you kingdom to the ground.\n" +
					"How will you react?"
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
				"Of course! How much do you need? (30 gold should be enough)",
				"The family likes you more and your production went up."));
      
      	ArrayList<Suku> muutMaalaiset = kunkku.etsiSukuTyypit(false, false, false, false, true);
      	muutMaalaiset.remove(x);
      
      	paatokset.add(new Paatos(
          		new Vaatimus[] { new Vaatimus(Tyyppi.NULL, 0) },
				new Seuraus[] { new Seuraus(Tyyppi.RAHA_T, 5),
                              	new Seuraus(Tyyppi.SUKUSUHDE, 20, new ArrayList<Suku>(Arrays.asList(x))),
                              	new Seuraus(Tyyppi.SUKUSUHDE, -10, kunkku.etsiSukuTyypit(false, false, false, false, true)),
                              	new Seuraus(Tyyppi.SUKUVALIT, -10, new ArrayList<Suku>(Arrays.asList(x)), muutMaalaiset)},
				"I'll arrange you more land from the other farmers.",
				"The family likes you more now and you got a little bit of production out of it but\n the other farmers seem angry at you."));
      
      	paatokset.add(new Paatos(
          		new Vaatimus[] { new Vaatimus(Tyyppi.NULL, 0) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, -10, kunkku.etsiSukuTyypit(false, false, false, false, true)),
                              	new Seuraus(Tyyppi.RAHA_T, 1)},
				"Sounds good! Might as well try it myself. ONLY myself!",
				"Your project didn't go as well as you planned. Your production only went up a little and now all farmers like you less."));
      
      	paatokset.add(new Paatos(
          		new Vaatimus[] { new Vaatimus(Tyyppi.NULL, 0) },
				new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, -10, new ArrayList<Suku>(Arrays.asList(x)))},
				"Take your greens elsewhere!",
				x.annaNimi() + " family didn't like your decision!"));
				
		kunkku.ongelmat.add(new Ongelma("Risky investment",
				"We've recently aquired a rare fruit from one of our contacts.\n"+
                "We believe that cultivating it will be beneficial to us both. We only need some money\n"+
                " to establish a new farm here. How about we get rich together?",
				x, paatokset));
       	}catch(IndexOutOfBoundsException e) {
      	
        }
       	
       	/*
       	Nimi: Viskialtis sijoitus (TOTEUTETTU)

       	Selitys: "Olemme hiljattain saaneet haltuumme harvinaisen hedelmälajikkeen liikekumppaniltamme. Uskomme sen viljelyn tuottavan meille suurta voittoa, tarvitsemme kuitenkin tukea isomman tilan perustamiseen. Voisitteko kenties tukea yhteistä hyväämme?"

       	Kohteet: Maanviljely suku

       	Paatokset:	 1. "Tottakai, paljonko tarvitsette?" (Jos varat riittävät alkusijoitukseen)
       				 2. "Järjestän teille maa-alaa muilta suvuilta, saatte tilanne."
       	             3. "Kokeilen mielelläni tätä, mutta vain itse!"
       	             4. "Viekää rehunne muualle."

       	Vaikutukset: 1. -30 kultaa ja +10 tyytyväisyys kohteen kanssa.
       				 2. +10 tyytyväisyys kohteelle, -10 tyytyväisyys muilta maalaissuvuilta.
       	             3. -10 tyytyväisyys kohteen kanssa.
       	             4. null
       	*/

       	paatokset = new ArrayList<Paatos>();
       	x = kunkku.etsiSukuTyypit(false, false, false, false, true).get(r.nextInt(kunkku.etsiSukuTyypit(false, false, false, false, true).size()));
       	
       	paatokset.add(new Paatos(
       	       new Vaatimus[] { new Vaatimus(Tyyppi.RAHA, 30) },
       	       new Seuraus[] {new Seuraus(Tyyppi.SUKUSUHDE, 10, new ArrayList<Suku>(Arrays.asList(x))),
       	                     new Seuraus(Tyyppi.RAHA, -30)},
       	       	"Of course! How much do you need? (30 gold should be enough)",
       			"You gave the family some money and they spent in on booze. The family likes you more now."));
       	 
       	 ArrayList<Suku> muutMaalaiset = kunkku.etsiSukuTyypit(false, false, false, false, true);
       	 muutMaalaiset.remove(x);
       	 
       	 paatokset.add(new Paatos(
       	        new Vaatimus[] { new Vaatimus(Tyyppi.NULL, 0) },
       			new Seuraus[] {new Seuraus(Tyyppi.SUKUSUHDE, 10, new ArrayList<Suku>(Arrays.asList(x))),
       	                       new Seuraus(Tyyppi.SUKUSUHDE, -10, muutMaalaiset),
       	                       new Seuraus(Tyyppi.SUKUVALIT, -10, new ArrayList<Suku>(Arrays.asList(x)), muutMaalaiset)},
       			"I'll arrange you more land from the other farmers.",
       			"They sold the land and used the money on booze. The family likes you more now but the other farmer don't like you at all."));
       	 
       	paatokset.add(new Paatos(
       	        new Vaatimus[] { new Vaatimus(Tyyppi.NULL, 0) },
       			new Seuraus[] { new Seuraus(Tyyppi.SUKUSUHDE, -10, kunkku.etsiSukuTyypit(false, false, false, false, true))},
       			"Sounds good! Might as well try it myself. ONLY myself!",
       			x.annaNimi() + " family got mad at you since you tried to steal their booze."));
       	      
       	paatokset.add(new Paatos(
       	        new Vaatimus[] { new Vaatimus(Tyyppi.NULL, 0) },
       			new Seuraus[] { new Seuraus(Tyyppi.NULL, 0)},
       			"Take your greens elsewhere!",
       			x.annaNimi() + " family didn't care that much, they were all quite drunk. They might've just spent anything you'd given them on booze.\n Good call!"));
       	
       	kunkku.ongelmat.add(new Ongelma("Whisky investment",
				"We've recently aquired a rare fruit from one of our contacts.\n"+
                "We believe that cultivating it will be beneficial to us both. We only need some money\n"+
                " to establish a new farm here. How about we get rich together?",
				x, paatokset));

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
                "Let the soldiers lynch them all.",
                "The soldiers are happy to get some enemy blood on their hands \n and "
                + x.annaNimi()+" family feels safe again."
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
				"Have the soldiers capture them and convert the unbelievers.",
                "The soldiers feel burdened by the extra work but the church\n is happy to see their congregation grow."
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
                "Have the soldiers capture them and make them work at the fields.",
                "Your food production goes up but the farmers aren't happy about shielding outlaws."
            )
        );
        
        paatokset.add(
            new Paatos(
                new Vaatimus[] {new Vaatimus(Tyyppi.SUKUSUHDE,3,kunkku.etsiSukuTyypit(false,true,false,false,false).get(0)) },
                new Seuraus[]{
                  new Seuraus(Tyyppi.RAHA, 10),
                  new Seuraus(Tyyppi.SUKUSUHDE, +25, kunkku.etsiSukuTyypit(false, false, false, true, false))
                },
                "Have the soldiers capture them and give them to the merchants. They should be able to make a nice profit out of slaves.",
                "The salve trade proves itself profitable and the merchants are happy."
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
                "(Don't do anything)",
                "Half of your farmers die from the attacks but this doesn't seem to worry the nobles at all."
            )
        );
      
		kunkku.ongelmat.add(
			new Ongelma("Barbarian attack", randomRaakalaisnimi + " has attacked " + x.annaNimi() + " family's farm! What should we do?"
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
	            "I'll have some merchants fetch the best possible grapes they can find.",
	            "The merchants weren't too pleased to hear about an unnecessary trip across dangerous lands \n"
	            + ", but " + x.annaEdustaja() + " let's out a delighted cry as they hear that the king will buy them \n"
	            + "more grapes!"
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
	            "I'll have the farmers plant more grapes so you won't run out next time.",
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
	            + "jälkeen ei enää pitkään aikaan syöty rypäleitä."
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
		
		/*Nimi:	Mieletön mäihä!
		Selitys: "Kauppiaat ovat löytäneet suuren rahakätkön kauppamatkoillaan ja koska aarre on niin mittaamattoman suuri (noin 50 rahaa), he 
					eivät tiedä mitä tehdä sillä. Saat päättää miten arteen tuoma vauraus jaetaan."
		Kohteet: random kauppiassuku

		Paatokset:	1. "Annat rahat maalaisille, jotta he voivat sijoittaa sen ruuantuotannon parantamiseen"
					2. "Rakennutat rahoilla uuden kirkon"
					3. "Kehität rahoilla sotilaiden hyvinvointia ja hankit heille paremmat miekat ja kilvet"
		            4. "Sijoitat varat magian koulutukseen ja uusien loitsujen kehitykseen"
		            5. "Kauppiaat saavat pitää aarteensa keskenään ja kehittää omaa kaupankäyntiään vapaasti"
					6. "Kestitset rahoilla aatelisia seuraavat kaksi kuukautta"
					7. "Jaat varat tasaisesti kaikkien kanssa"
					8. "Pidät kaikki arteet ja rahat itselläsi"

		Vaikutukset:	1. RUOKA_T++, maalaiset ++
						2. Uskonnolliset++
						3. Sotilaalliset++
		            	4. Maagiset++
		            	5. Kauppiaat++
						6. Aateliset++
						7. Kaikki+
						8. Raha++ */
				
				
				paatokset = new ArrayList<Paatos>();

				ArrayList<Suku> kauppiaat = new ArrayList<Suku>(kunkku.etsiSukuTyypit(true, false, false, false, false));
				
				x = kauppiaat.get(r.nextInt(kauppiaat.size()));
			    
			    paatokset.add(
			        new Paatos(
			            new Vaatimus[] {new Vaatimus(Tyyppi.NULL, 0)},
			            new Seuraus[]{
			              new Seuraus(Tyyppi.RUOKA_T,10),
			              new Seuraus(Tyyppi.SUKUSUHDE, 30,maalaiset),
						  new Seuraus(Tyyppi.SUKUPOPULAATIO, 10, maalaiset)
			            },
			            "Haluan jakaa varat kaikista köyhimmälle kansanosallemme maalaisille!",
						"Ruuantuotto lisääntyy, koska maalaiset sijoittavat varoja viisaasti\n" +
						"uusiin maataloustyövälineisiin."
			        )
			    );

			    paatokset.add(
			        new Paatos(
			            new Vaatimus[] {new Vaatimus(Tyyppi.NULL, 0)},
			            new Seuraus[]{
			              new Seuraus(Tyyppi.SUKUSUHDE, 30, kunkku.etsiSukuTyypit(false,false,true,false,false)),
						  new Seuraus(Tyyppi.SUKUPOPULAATIO, 10, kunkku.etsiSukuTyypit(false,false,true,false,false))
			            },
			            "Rakennutat rahoilla uuden kirkon.",
						"Uskonnollisuus kuningaskunnassasi kasvaa."
			        )
			    );

			    paatokset.add(
			        new Paatos(
			            new Vaatimus[] {new Vaatimus(Tyyppi.NULL, 0)},
			            new Seuraus[]{
			              new Seuraus(Tyyppi.SUKUSUHDE, 30, kunkku.etsiSukuTyypit(false,true,false,false,false)),
						  new Seuraus(Tyyppi.SUKUPOPULAATIO, 10, kunkku.etsiSukuTyypit(false,true,false,false,false))
			            },
			            "Kehität rahoilla sotilaiden hyvinvointia ja hankit heille paremmat miekat ja kilvet.",
						"Kuningaskuntasi sotilasvoimat kasvavat!"
			        )
			    );

			    paatokset.add(
			        new Paatos(
			            new Vaatimus[] {new Vaatimus(Tyyppi.NULL, 0)},
			            new Seuraus[]{
			              new Seuraus(Tyyppi.SUKUSUHDE, 30, kunkku.etsiSukuTyypit(true,false,false,false,false)),
						  new Seuraus(Tyyppi.SUKUPOPULAATIO, 10, kunkku.etsiSukuTyypit(true,false,false,false,false))
			            },
			            "Sijoitat varat magian koulutukseen ja uusien loitsujen kehitykseen",
						"Kuningaskuntasi maagiset voimat kasvavat!"
			        )
			    );

			    paatokset.add(
			        new Paatos(
			            new Vaatimus[] {new Vaatimus(Tyyppi.NULL, 0)},
			            new Seuraus[]{
			              new Seuraus(Tyyppi.SUKUSUHDE, 30, kunkku.etsiSukuTyypit(false,false,false,true,false)),
						  new Seuraus(Tyyppi.SUKUPOPULAATIO, 10, kunkku.etsiSukuTyypit(false,false,false,true,false)),
						  new Seuraus(Tyyppi.RAHA_T, 3)
			            },
			            "Kauppiaat saavat pitää aarteensa keskenään ja kehittää omaa kaupankäyntiään vapaasti",
						"Kaupankäynti kuningaskunnassasi lisääntyy ja kauppa kukoistaa!"
			        )
			    );

			    paatokset.add(
			        new Paatos(
			            new Vaatimus[] {new Vaatimus(Tyyppi.NULL, 0)},
			            new Seuraus[]{
			              new Seuraus(Tyyppi.SUKUSUHDE, 30, aateliset),
						  new Seuraus(Tyyppi.SUKUPOPULAATIO, 10, aateliset)
			            },
			            "Kestitset rahoilla aatelisia seuraavat kaksi kuukautta.",
						"Aatelisten kanssa juhlitaan semmoisia juhlia, notta ei ole aiemmin nähty!"
			        )
			    );
			    
			    paatokset.add(
			        new Paatos(
			            new Vaatimus[] {new Vaatimus(Tyyppi.NULL, 0)},
			            new Seuraus[]{
			              new Seuraus(Tyyppi.SUKUSUHDE, 5, kunkku.etsiSukuTyypit(true, true, true, true, true)),
						  new Seuraus(Tyyppi.SUKUPOPULAATIO, 5, kunkku.etsiSukuTyypit(true, true, true, true, true))
			            },
			            "Jaat varat tasaisesti kaikkien kanssa",
						"Kaikki hyötyvät ja kuningaskuntasi kukoistaa!"
			        )
			    );

			    paatokset.add(
			        new Paatos(
			            new Vaatimus[] {new Vaatimus(Tyyppi.NULL, 0)},
			            new Seuraus[]{
			              new Seuraus(Tyyppi.RAHA, 52),
						  new Seuraus(Tyyppi.SUKUSUHDE, -1, kunkku.etsiSukuTyypit(true, true, true, true, true))
			            },
			            "Pidät arteen kokonaan itselläsi.",
						"Olet rikas! MUTTA kaikkia muita kyllä vähän kismittää"
			        )
			    );
				
				kunkku.ongelmat.add(
					new Ongelma("Mieletön mäihä!", "Kauppiassuku " + x.annaNimi() + " on löytänyt suuren rahakätkön\n" 
						+ "kauppamatkoillaan ja koska aarre on niin mittaamattoman suuri (noin 50 rahaa), \n" 
						+ "he eivät tiedä mitä tehdä sillä. Saat päättää miten arteen tuoma vauraus jaetaan."
							, x
							,paatokset)
				);
	}
	
	static void cloneSukuLista(ArrayList<Suku> source, ArrayList<Suku> target) {
		for (Suku suku : source) {
			target.add(suku);
		}
	}
}