package kuningaskuntaSimulaatio;

import java.util.*;

public class Suku{
  private int suhdeKuninkaaseen;
  private HashMap<Suku,Integer> suhteet = new HashMap<Suku,Integer>();
  private String nimi;
  private String edustaja;
  private int populaatio;
  private int aatelisuus;				//Aina arvot 0-3
  
  private int magia;						//Suvun maagisuus (0-4)
  private int sotilaallinen;		//Suvun sotilaallisuus (0-4)
  private int uskonnollinen;		//Suvun uskonnollisuus (0-4)   ARVOT YHTEENSÄ 4 PER SUKU!
  private int kauppias;					//Suvun kauppiaisuus (0-4)
  private int maalainen;				//Suvun maallisuus (0-4)
 
  
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
  public void asetaSuhdeKuninkaaseen(int suhdeKuninkaaseen) {
	  this.suhdeKuninkaaseen=suhdeKuninkaaseen;
  }
  public void asetaSuhdeSukuun(int Suhdeluku, Suku kohde) {
	  suhteet.put(kohde, Suhdeluku);
  }
  public void asetaEdustaja(String edustaja) {
	  this.edustaja=edustaja;
  }
  public void asetaPopulaatio(int populaatio) {
	  this.populaatio=populaatio;
  }
  public void asetaAatelisuus(int aatelisuus) {
	  this.aatelisuus=aatelisuus;
  }
  public void asetaMagia(int magia) {
	  this.magia=magia;
  }
  public void asetaSotilaallinen(int sotilaallinen) {
	  this.sotilaallinen=sotilaallinen;
  }
  public void asetaUskonnollinen(int uskonnollinen) {
	  this.uskonnollinen=uskonnollinen;
  }
  public void asetaKauppias(int kauppias) {
	  this.kauppias=kauppias;
  }
  public void asetaMaalainen(int maalainen) {
	  this.maalainen=maalainen;
  }
  public String annaNimi() {
	  return nimi;
  }
  public int annaSuhdeKuninkaaseen() {
	  return suhdeKuninkaaseen;
  }
  public int annaSuhdeSukuun(Suku x) {
	  return suhteet.get(x);
  }
  public String annaEdustaja() {
	  return edustaja;
  }
  public int annaPopulaatio() {
	  return populaatio;
  }
  public int annaAatelisuus() {
	  return aatelisuus;
  }
  public int annaMagia() {
	  return magia;
  }
  public int annaSotilaallinen() {
	  return sotilaallinen;
  }
  public int annaUskonnollinen() {
	  return uskonnollinen;
  }
  public int annaKauppias() {
	  return kauppias;
  }
  public int annaMaalainen() {
	  return maalainen;
  }
  public void toString(Suku kohde) {
	  System.out.println(annaNimi() + ":");
	  System.out.println("Sukua edustaa " + annaEdustaja());
	  System.out.println("Suku teihin on: " + annaSuhdeKuninkaaseen());
	  int huonoin = 100;
	  Suku hsuku = suhteet.keySet().iterator().next();
	  int paras = -100;
	  Suku psuku = suhteet.keySet().iterator().next();
	  for(Suku tarkasteltava : suhteet.keySet()) {
		  if (suhteet.get(tarkasteltava)<huonoin) {
			  hsuku = tarkasteltava;
		  }
		  if (suhteet.get(tarkasteltava)>paras) {
			  psuku = tarkasteltava;
		  }
	  }
	  System.out.println("Heidän Läheisin liittolaisensa on " + psuku);
	  System.out.println("Heidän pahin vihamiehensä on " + hsuku);
	  System.out.println("Suvussa on " + annaPopulaatio() + " jäsentä.");
	  if (annaAatelisuus()>0) {
		  System.out.println("Suku on aatelinen.");
	  }
	  if (annaMagia()>0) {
		  System.out.println("Suku on maaginen.");
	  }
	  if (annaSotilaallinen()>0) {
		  System.out.println("Suku on sotilaallinen.");
	  }
	  if (annaUskonnollinen()>0) {
		  System.out.println("Suku on uskonnollinen.");
	  }
	  if (annaKauppias()>0) {
		  System.out.println("Suku on kauppiassuku.");
	  }
	  if (annaMaalainen()>0) {
		  System.out.println("Suku tuottaa ruokaa.");
	  }
  }
}
