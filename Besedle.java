import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Besedle {
    static final int BELA = 0;
    static final int CRNA = 1;
    static final int RUMENA = 2;
    static final int ZELENA = 3;
    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_GREEN_BG = "\u001b[42m";
    static final String ANSI_YELLOW_BG = "\u001b[43m";
    static final String ANSI_WHITE_BG = "\u001b[47;1m";
    static final String ANSI_BLACK_BG = "\u001b[40m";
    static final String ANSI_WHITE = "\u001b[37m";
    static final String ANSI_BLACK = "\u001b[30m";
    static String[] seznamBesed; // Seznam vseh možnih besed
    static String iskanaBeseda;
    static String datoteka = "besede.txt";
    static final String abeceda = "ABCČDEFGHIJKLMNOPRSŠTUVZŽ"; // Veljavne črke
    static final int MAX_POSKUSOV = 6; // Število poskusov
    static int[] barveAbecede; // Barve črk pri izpisu abecede
    static Scanner sc = new Scanner(System.in);
    static void izpisiZBarvo(char znak, int barva) {
        String slog;
        if (barva == ZELENA) {
            slog = ANSI_BLACK + ANSI_GREEN_BG;
        } else if (barva == RUMENA) {
            slog = ANSI_BLACK + ANSI_YELLOW_BG;
        } else if (barva == BELA) {
            slog = ANSI_BLACK + ANSI_WHITE_BG;
        } else {
            slog = ANSI_WHITE + ANSI_BLACK_BG;
        }
        System.out.print(slog + " " + znak + " " + ANSI_RESET);
    }

    public static void main(String[] args) {
        preberiBesede();
        novaIgra();
        boolean stanje = true;
        int stevec=0;
        while(stanje && stevec<MAX_POSKUSOV){
            izpisAbecede();
            System.out.println("Poskus "+(stevec+1)+"/6");
            System.out.print("Vnesi besedo: ");
            String ugibanaBeseda = sc.nextLine().toUpperCase();
            if (veljavnaBeseda(ugibanaBeseda)){
                izpisiBesedo(pobarvajBesedo(ugibanaBeseda), ugibanaBeseda);
            }
            int konec = 0;
            for (int e:pobarvajBesedo(ugibanaBeseda)) {
                if (e==3) konec++;
            }
            if (konec==5) // Enaka beseda
                stanje = false;
            stevec++;
        }
        if (stanje){
            System.out.println("Ni vam uspelo, iskana beseda je bila: "+iskanaBeseda);
        }
        else {
            System.out.println("Čestitam, uspelo vam je!");
        }
    }
    static void preberiBesede(){
        List<String> listOfStrings
                = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(datoteka))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length()!=4){
                    listOfStrings.add(line.toUpperCase());
                    line = br.readLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        seznamBesed = listOfStrings.toArray(new String[0]);

    }
    static void novaIgra(){
        barveAbecede = new int[abeceda.length()];
        Random rand = new Random();
        int random = rand.nextInt(seznamBesed.length);

        for (int stevec=0;stevec< barveAbecede.length;stevec++){
            barveAbecede[stevec] = 0;
        }
        iskanaBeseda = seznamBesed[random];
    }
    static void izpisAbecede(){
        for (int stevec=0;stevec< barveAbecede.length;stevec++){
            izpisiZBarvo(abeceda.charAt(stevec),barveAbecede[stevec]);
        }
        System.out.println();
    }
    static boolean veljavnaBeseda(String beseda) {
        if (beseda.length()!=5){
            System.out.println("Neveljavna dolžina besede!");
            return false;
        }
        for (int stevec=0; stevec<beseda.length();stevec++){
            if (!Character.isLetter(beseda.charAt(stevec))){
                System.out.println("V besedi so neveljavni znaki!");
                return false;
            }
        }
        return true;
    }
    static int[] pobarvajBesedo(String ugibanaBeseda){
        int[] tabelaBesed = new int[ugibanaBeseda.length()];
        for (int crka=0;crka<ugibanaBeseda.length();crka++){
            for (int stevec = 0; stevec<ugibanaBeseda.length();stevec++){
                if (ugibanaBeseda.charAt(stevec)==iskanaBeseda.charAt(stevec)){
                    tabelaBesed[stevec] = 3;
                    int indeks = abeceda.indexOf(ugibanaBeseda.charAt(stevec));
                    barveAbecede[indeks] = 3;
                } else if (iskanaBeseda.contains(Character.toString(ugibanaBeseda.charAt(stevec)))) {
                    tabelaBesed[stevec] = 2;
                    int indeks = abeceda.indexOf(ugibanaBeseda.charAt(stevec));
                    if (barveAbecede[indeks]!=3) barveAbecede[indeks] = 2;
                }
                else{
                    tabelaBesed[stevec] = 1;
                    int indeks = abeceda.indexOf(ugibanaBeseda.charAt(stevec));
                    barveAbecede[indeks] = 1;
                }
            }
        }

        return tabelaBesed;
    }
    static void izpisiBesedo(int[] tabelaBesed, String ugibanaBeseda){
        for (int stevec=0;stevec<tabelaBesed.length;stevec++){
            izpisiZBarvo(ugibanaBeseda.charAt(stevec), tabelaBesed[stevec]);
        }
        System.out.println();
    }
}
