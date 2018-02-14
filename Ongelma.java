package kuningaskuntaSimulaatio;

import java.util.*;

public class Ongelma{
  private String nimi;
  private String selitys;
  private ArrayList<Suku> kohteet; //Ensimm�inen kohde on esittelija, 
  private ArrayList<Paatos> paatokset;
    // Mahdollisesti vaikeusastelis�ys n�ille ajan my�t�
  public void tulosta(){
    
  }
  public Ongelma(String nimi, String selitys, ArrayList<Suku> kohteet, ArrayList<Paatos> paatokset){ //(Sis�lsi alunperin intin) Ongelman luonnin j�lkeen pit�� ne lis�t� arraylistiin.
    this.nimi = nimi;
    this.selitys = selitys;
    this.kohteet = kohteet;
    this.paatokset = paatokset;
  }
}

class Paatos{
  private Vaatimus[] v;
  private Seuraus[] s;
  public Paatos(Vaatimus[] v, Seuraus[] s) {
	  this.v  = v;
    this.s = s;
  }
  public void tulostaPaatosrivi(){
    
  }
  public void toteutaSeuraukset(){
    
  }
}

enum Tyyppi{
	Raha,
	Ruoka,
	Sukusuhde
}

class Vaatimus{
  private Tyyppi tyyppi;
  private int arvo;
  private ArrayList<Suku> kohde;
  public Vaatimus(Tyyppi tyyppi, int arvo, ArrayList<Suku> kohde) {
		this.tyyppi = tyyppi;
		this.arvo = arvo;
		if(kohde != null) this.kohde = kohde;
  }
  public boolean tarkistaVaatimus() {
		return false;
  }
}

class Seuraus{
  private Tyyppi tyyppi;
  private int arvo;
  private ArrayList<Suku> kohde;
  public Seuraus(Tyyppi tyyppi, int arvo, ArrayList<Suku> kohde) {
		this.tyyppi = tyyppi;
		this.arvo = arvo;
		if(kohde != null) this.kohde = kohde;
  }
}
