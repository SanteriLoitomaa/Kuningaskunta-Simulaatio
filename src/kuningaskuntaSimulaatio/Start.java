package kuningaskuntaSimulaatio;
/**
 * Avaa erillisen komentoikkunan, jotta peli on helpompi avata.
 * @author Santeri Loitomaa (lainattu)
 */
import java.io.*;
import java.awt.GraphicsEnvironment;
import java.net.URISyntaxException;
public class Start{
    public static void main(String [] args) throws IOException, InterruptedException, URISyntaxException{
        Console console = System.console();
        if(console == null && !GraphicsEnvironment.isHeadless()){
        	String filename = Start.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6,
        			Start.class.getProtectionDomain().getCodeSource().getLocation().toString().length()-1) + "Kuningaskunta-simulaatio.jar";
            System.out.println(filename);
            Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -jar \"" + filename + "\""});
        }else{
            Kuningaskunta.main(new String[0]);
            System.out.println("Peli loppui, kirjoita 'exit' poistuaksesi.");
        }
    }
}