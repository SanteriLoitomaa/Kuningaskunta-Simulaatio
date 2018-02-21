package kuningaskuntaSimulaatio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TallennaLataa implements Serializable{

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
}