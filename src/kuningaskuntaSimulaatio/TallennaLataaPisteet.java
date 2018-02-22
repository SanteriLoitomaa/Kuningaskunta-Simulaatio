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
	
	@SuppressWarnings("unchecked")
	public static void lisaaPisteet(int piste, String nimi) {
		HashMap<Integer, String> pisteet = new HashMap<Integer, String>();
		try {
			FileInputStream tiedosto = new FileInputStream("pisteet.pis");
			ObjectInputStream lataa = new ObjectInputStream(tiedosto);
			pisteet = (HashMap<Integer, String>) lataa.readObject();
			lataa.close();
			tiedosto.close();
			pisteet.put(piste, nimi);
			int[] pis = new int[pisteet.size()];
			int x = 0;
			for(int i : pisteet.keySet()) {
				pis[x] = i;
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
			HashMap<Integer, String> uusi = new HashMap<Integer, String>();
			for(int i = 0; i < pisteet.size(); i++)
				uusi.put(pis[i], pisteet.get(pis[i]));
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
		HashMap<Integer, String> pisteet = new HashMap<Integer, String>();
		try {
			FileInputStream tiedosto = new FileInputStream("pisteet.pis");
			ObjectInputStream lataa = new ObjectInputStream(tiedosto);
			pisteet = (HashMap<Integer, String>) lataa.readObject();
			lataa.close();
			tiedosto.close();
			System.out.println("Parhaat pisteet ovat saaneet: ");
			for(int i : pisteet.keySet()) {
				System.out.println(pisteet.get(i) + "           " + i);
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}