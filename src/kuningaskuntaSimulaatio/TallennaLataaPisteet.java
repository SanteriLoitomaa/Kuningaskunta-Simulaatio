package kuningaskuntaSimulaatio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Sisältää metodit tallennustiedoston ja pisteiden hallinnalle
 * @author Santeri Loitomaa
 */
public class TallennaLataaPisteet implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * Tallenna peli tallennus.tal tiedostoon
	 * 
	 * @param kunkku (Kuninkaan nykyinen instanssi)
	 */
	public static void tallenna(Kuningas kunkku) {
		try {
			FileOutputStream tiedosto = new FileOutputStream("tallennus.tal");
			ObjectOutputStream tallenna = new ObjectOutputStream(tiedosto);
			tallenna.writeObject(kunkku);
			tallenna.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lataa peli tallennus.tal tiedostosta
	 * 
	 * @return kuninkaan vanha instanssi
	 */
	public static Kuningas lataa() throws FileNotFoundException, ClassNotFoundException {
		try {
			FileInputStream tiedosto = new FileInputStream("tallennus.tal");
			ObjectInputStream lataa = new ObjectInputStream(tiedosto);
			Kuningas kunkku = (Kuningas) lataa.readObject();
			lataa.close();
			return kunkku;
		} catch (IOException e) {
			
		}
		return null;
	}
	
	/**
	 * Tarkistaa, onko tulostaulukko olemassa ja tuleeko pisteet lisätä listaan.
	 * Jos näin, päivittää listatiedoston pisteet.pis
	 * 
	 * @param pist (Pelaajan saamien pisteiden määrä)
	 * @param nimi (Hallitsijan nimi)
	 */
	public static void lisaaPisteet(int pist, String nimi) {
		try {
			FileInputStream tiedosto3 = new FileInputStream("pisteet.pis");
			tiedosto3.close();
		}catch(IOException e){
			TallennaLataaPisteet.luoPisteet();
		}
		try {
			FileInputStream tiedosto = new FileInputStream("pisteet.pis");
			ObjectInputStream lataa = new ObjectInputStream(tiedosto);
			Pisteet pisteet = (Pisteet) lataa.readObject();
			lataa.close();
			tiedosto.close();
			int[] piste = pisteet.annaPisteet();
			int[] temp = new int[11];
			for(int i = 0; i < piste.length; i++) {
				temp[i] = piste[i];
			}
			temp[10] = pist;
			String[] nimet = pisteet.annaNimet();
			String[] tem = new String[11];
			for(int i = 0; i < piste.length; i++) {
				tem[i] = nimet[i];
			}
			tem[10] = nimi;
			Arrays.sort(temp);
			for(int i = 0; i < temp.length / 2; i++) {
			    int tempo = temp[i];
			    temp[i] = temp[temp.length - i - 1];
			    temp[temp.length - i - 1] = tempo;
			}
			int nykyinen = 10;
			if(temp[10] != pist) {
				for(int i = 0; i < 10; i++) {
					if(temp[i] != piste[i]) {
						nykyinen = i;
						break;
					}
				}
				for(int i = nykyinen + 1; i < 10; i++) {
					tem[i] = nimet[i - 1];
				}
				tem[nykyinen] = nimi;
			}
			for(int i = 0; i < 10; i++) {
				nimet[i] = tem[i];
				piste[i] = temp[i];
			}
			pisteet.asetaNimet(nimet);
			pisteet.asetaPisteet(piste);
			FileOutputStream tiedosto1 = new FileOutputStream("pisteet.pis");
			ObjectOutputStream tallenna = new ObjectOutputStream(tiedosto1);
			tallenna.writeObject(pisteet);
			tallenna.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
			
	}
	
	/**
	 * Tulostaa pistetaulukon tiedostosta pisteet.pis
	 */
	public static void tulostaPisteet() {
		try {
			FileInputStream tiedosto3 = new FileInputStream("pisteet.pis");
			tiedosto3.close();
		}catch(IOException e){
			TallennaLataaPisteet.luoPisteet();
		}
		try {
			FileInputStream tiedosto = new FileInputStream("pisteet.pis");
			ObjectInputStream lataa = new ObjectInputStream(tiedosto);
			Pisteet pisteet = (Pisteet) lataa.readObject();
			lataa.close();
			tiedosto.close();
			int[] piste = pisteet.annaPisteet();
			String[] nimet = pisteet.annaNimet();
			System.out.println("Parhaat pisteet ovat saaneet: ");
			for(int i = 0; i < 10; i++) {
				System.out.println(nimet[i] + " sai " + piste[i] + " pistettä.");
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Luo/Nollaa pistetaulukon pisteet.pis
	 */
	public static void luoPisteet() {
		Pisteet pisteet = new Pisteet(
			new int[] {100000, 75000, 50000, 25000, 10000, 7500, 5000, 2500, 1000, 100},
			new String[] {"Kuningas Arthur", "Kuningas Arthur", "Kuningas Arthur", "Kuningas Arthur", "Kuningas Arthur",
					"Kuningas Arthur", "Kuningas Arthur", "Kuningas Arthur", "Kuningas Arthur", "Kuningas Arthur"});
		try {
			FileOutputStream tiedosto = new FileOutputStream("pisteet.pis");
			ObjectOutputStream tallenna = new ObjectOutputStream(tiedosto);
			tallenna.writeObject(pisteet);
			tallenna.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/**
 * Runko pistetaulukko-oliolle, joka tallennetaan tiedostoon pisteet.pis
 * @author Santeri Loitomaa
 */
class Pisteet implements Serializable{
	private static final long serialVersionUID = 1L;
	private int[] pisteet;
	private String[] nimet;
	
	public Pisteet(int[] pisteet, String[] nimet) {
		this.pisteet = pisteet;
		this.nimet = nimet;
	}
	
	public int[] annaPisteet() {
		return this.pisteet;
	}
	
	public String[] annaNimet() {
		return this.nimet;
	}
	
	public void asetaPisteet(int[] pisteet) {
		this.pisteet = pisteet;
	}
	
	public void asetaNimet(String[] nimet) {
		this.nimet = nimet;
	}
	
}