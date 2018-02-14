package kuningaskuntaSimulaatio;

import java.util.*;

public class Suku{
  private int suhdeKuninkaaseen;
  private HashMap<Suku,Integer> suhteet;
  private String nimi;
  private String edustaja;
  private int populaatio;
  private int aatelisuus;				//Aina arvot 0-3
  
  private int magia;						//Suvun maagisuus (0-4)
  private int sotilaallinen;		//Suvun sotilaallisuus (0-4)
  private int uskonnollinen;		//Suvun uskonnollisuus (0-4)   ARVOT YHTEENSÄ 4 PER SUKU!
  private int kauppias;					//Suvun kauppiaisuus (0-4)
  private int maalainen;				//Suvun maallisuus (0-4)
  
  public void annaTiedot(){
    //Anna edustaja, suvun nimi, aatelisuus ja muut ominaisuudet pisteineen. Annetaan aina kun vuorossa esitellään ongelma.
  }
  
  public Suku(String nimi, String edustaja, int populaatio, int aatelisuus, int magia, int sotilaallinen, int uskonnollinen, int kauppias, int maalainen){
  this.populaatio = populaatio;
  this.aatelisuus = aatelisuus;
  
  this.magia = magia;
  this.sotilaallinen = sotilaallinen;
  this.uskonnollinen = uskonnollinen;
  this.kauppias = kauppias;
  this.maalainen = maalainen;
}
  
  public void asetaNimi(String nimi){
  	this.nimi = nimi;
  }
}
