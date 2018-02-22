package kuningaskuntaSimulaatio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;


public class TallennaLataaPisteet implements Serializable{

	private static final long serialVersionUID = 1L;

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

	public static Kuningas lataa() {
		try {
			FileInputStream tiedosto = new FileInputStream("tallennus.tal");
			ObjectInputStream lataa = new ObjectInputStream(tiedosto);
			Kuningas kunkku = (Kuningas) lataa.readObject();
			lataa.close();
			return kunkku;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	public static void lisaaPisteet(int pist, String nimi) {
		HashMap<String, String> pisteet = new HashMap<String, String>();
		String piste = "" + pist;
		try {
			FileInputStream tiedosto = new FileInputStream("pisteet.pis");
			ObjectInputStream lataa = new ObjectInputStream(tiedosto);
			pisteet = (HashMap<String, String>) lataa.readObject();
			lataa.close();
			tiedosto.close();
			if(pisteet.containsKey(piste)) piste = "0" + piste;
			pisteet.put(piste, nimi);
			int[] pis = new int[pisteet.size()];
			int x = 0;
			for(String s : pisteet.keySet()) {
				pis[x] = Integer.parseInt(s);
				x++;
			}
			java.util.Arrays.sort(pis);
			for(int i = 0; i < pis.length / 2; i++) {
			    int temp = pis[i];
			    pis[i] = pis[pis.length - i - 1];
			    pis[pis.length - i - 1] = temp;
			}
			if(pis.length == 11)
				pisteet.remove(pis[10]);
			String[] pistee = new String[pisteet.size()];
			for(int i = 0; i < pisteet.size(); i++) {
				pistee[i] = pis[i] + "";
			}
			HashMap<String, String> uusi = new HashMap<String, String>();
			for(int i = 0; i < pisteet.size(); i++)
				if(uusi.containsKey(pistee[i])) {
					uusi.put("0" + pistee[i], pisteet.get(pistee[i]));
				}
				else uusi.put(pistee[i], pisteet.get(pistee[i]));
			FileOutputStream tiedosto1 = new FileOutputStream("pisteet.pis");
			ObjectOutputStream tallenna = new ObjectOutputStream(tiedosto1);
			tallenna.writeObject(uusi);
			tallenna.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void tulostaPisteet() {
		HashMap<String, String> pisteet = new HashMap<String, String>();
		try {
			FileInputStream tiedosto = new FileInputStream("pisteet.pis");
			ObjectInputStream lataa = new ObjectInputStream(tiedosto);
			pisteet = (HashMap<String, String>) lataa.readObject();
			lataa.close();
			tiedosto.close();
			System.out.println("Parhaat pisteet ovat saaneet: ");
			for(String i : pisteet.keySet()) {
				String p = i;
				for(int x = 0; x < p.length(); x++) {
					if(p.charAt(x) == '0') p = p.substring(1, p.length());
					else break;
				}
				System.out.println(pisteet.get(i) + "           " + Integer.parseInt(i));
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void luoPisteet() {
		HashMap<String, String> pisteet = new HashMap<String, String>();
		pisteet.put("10000", "King Arthur");
		pisteet.put("010000", "King Arthur");
		pisteet.put("0010000", "King Arthur");
		pisteet.put("00010000", "King Arthur");
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