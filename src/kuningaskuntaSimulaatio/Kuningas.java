package kuningaskuntaSimulaatio;

import java.util.*;

public class Kuningas{ 
  @SuppressWarnings("unused")
  private String nimi;
  private int raha;
  private int ruoka;
  private int rahaTuotto;
  private int ruokaTuotto;
  private int vuorot;
  private int sukujenLKM;
  private boolean havitty = false;
  @SuppressWarnings("unused")
private Scanner vastaus = new Scanner(System.in);
  public ArrayList<Ongelma> ongelmat;
  public ArrayList<Suku> suvut;
  public ArrayList<String> maalaisnimet = new ArrayList<>(Arrays.asList("Kuokka","Vilja","Vihreä","Lihava","Pelto","Aura","Terve","Jussi"));
  public ArrayList<String> sotilasnimet = new ArrayList<>(Arrays.asList("Miekka","Kilpi","Rohkea","Keihäs","Soturi","Tappava","Nuoli","Lävistävä","Verinen","Lalli","Punainen"));
  public ArrayList<String> uskontonimet = new ArrayList<>(Arrays.asList("Risti","Pyhä","Loistava","Siunattu","Jeesus","Jumalan","Hymni","Synagoga","Protestantti","Sutra","Valkoinen"));
  public ArrayList<String> kauppiasnimet = new ArrayList<>(Arrays.asList("Kultainen","Varakas","Kolikko","Äveriäs","Roope","Tuote","Lentävä","Kärryt","Lompakko"));
  public ArrayList<String> magianimet = new ArrayList<>(Arrays.asList("Lohikäärme","Salamoiva","Liekehtivä","Feenix","Potter","Milla","Sauva","Alkemisti","Haltija"));
  
public Kuningas(String nimi,int vuorot) {
    Random r = new Random();
    this.nimi = nimi;
    this.raha = r.nextInt(10)+90;
    this.ruoka = r.nextInt(10)+90;
    this.rahaTuotto = 2;
    this.ruokaTuotto = 2;
    this.vuorot = vuorot;
    this.sukujenLKM = 15;
    for ( int i = 0; i < sukujenLKM-1; i++) {
    	int[] tyyppi = new int[5];
      int populaatio = r.nextInt(30)+20;
			tyyppi[r.nextInt(6)] += r.nextInt(1)+1;
      tyyppi[r.nextInt(6)] += r.nextInt(1)+1;
      //haetaan tyyppiin kuuluva nimi
      String lisattavaNimi ="";
      for ( int j = 0; j < tyyppi.length; j++) {
        if ( tyyppi[j] != 0) {
          switch ( j ) {
            case 0: lisattavaNimi += magianimet.get(r.nextInt(magianimet.size()));
            case 1: lisattavaNimi += sotilasnimet.get(r.nextInt(sotilasnimet.size()));
            case 2: lisattavaNimi += uskontonimet.get(r.nextInt(uskontonimet.size()));
            case 3: lisattavaNimi += kauppiasnimet.get(r.nextInt(kauppiasnimet.size()));
            case 4: lisattavaNimi += maalaisnimet.get(r.nextInt(maalaisnimet.size()));
          }
        }
      }
      suvut.add(new Suku(lisattavaNimi, "RandomEtuNimi", populaatio, 0, tyyppi[0], tyyppi[1], tyyppi[2], tyyppi[3], tyyppi[4])); 	//tehdään ensin sukukonstruktori...
    }
    
    //luodaan aateliset
    suvut.add(new Suku("Isoherrat", "RandomEtuNimi", r.nextInt(10) + 20, r.nextInt(3)+1,0,0,0,0,0));
    
    generoiSukuSuhteet();
  }
  
  public int annaSukujenLKM(){
	  return sukujenLKM;
  }
  
  public void vuorokierto(){ // Koko peli täällä
    laskePisteet();
    for(int i = 0; i < vuorot; i++){
      Random r = new Random();
      int x = r.nextInt(25);
      Ongelma vuoronOngelma = ongelmat.get(x);
      vuoronOngelma.tulosta();
      //Paatos paatos = kysyPaatos(); 
      //paatos.toteutaSeuraukset();
      laskePisteet();
      if(havitty) break;
    }
    tulostaPisteet();
  }
  
  /*private Paatos kysyPaatos(){
    return new Paatos();
  }*/
  
  private void laskePisteet(){
    this.raha += this.rahaTuotto;
    this.ruoka += this.ruokaTuotto;
    if(ruoka < 1 || raha < 1){
      System.out.println("Resurssisi loppuivat ja kuningaskuntasi vajosi anarkiaan.");
      havitty = true;
    }
    else System.out.println("Sinulla on " + this.raha + " kulta(a) ((+)" + this.rahaTuotto + ") ja " + this.ruoka + " ruoka(a) per kk ((+)" + this.ruokaTuotto + ").");
  }

  //pelin lopetus
  private void tulostaPisteet(){
    System.out.println("Valtakautesi on päättynyt.");
  }
  
  @SuppressWarnings("unused")
private void generoiNimi(Suku nimettava){
    nimettava.asetaNimi("randomnimi");
  }
                       
  private void generoiSukuSuhteet() {
  }
}