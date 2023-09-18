/*
 * PlayWriter.java
 *
 * PLayWriter class.
 * Creates the lovers, and writes the two lover's story (to an output text file).
 */


import java.net.Socket;
import java.net.InetAddress;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class PlayWriter {

    private Romeo  myRomeo  = null;
    private InetAddress RomeoAddress = null;
    private int RomeoPort = 0;
    private Socket RomeoMailbox = null;

    private Juliet myJuliet = null;
    private InetAddress JulietAddress = null;
    private int JulietPort = 0;
    private Socket JulietMailbox = null;

    double[][] theNovel = null;
    int novelLength = 0;

    public PlayWriter()
    {
        novelLength = 500; //Number of verses
        theNovel = new double[novelLength][2];
        theNovel[0][0] = 0;
        theNovel[0][1] = 1;
    }

    //Create the lovers
    public void createCharacters() {
        //Create the lovers
        System.out.println("PlayWriter: Romeo enters the stage.");

			//TO BE COMPLETED
             myRomeo = new Romeo(theNovel[0][0]);
             Thread rThread = new Thread(myRomeo);
             rThread.start();


        System.out.println("PlayWriter: Juliet enters the stage.");

			//TO BE COMPLETED
         myJuliet = new Juliet(theNovel[0][1]);
        Thread jThread = new Thread(myJuliet);
        jThread.start();
    }

    //Meet the lovers and start letter communication
    public void charactersMakeAcquaintances() {

			//TO BE COMPLETED
			try {
                RomeoAddress = myRomeo.getAcquaintance().getKey();
                RomeoPort = myRomeo.getAcquaintance().getValue();
            } catch (Exception e) {
                e.getStackTrace();


            }
        System.out.println("PlayWriter: I've made acquaintance with Romeo");


			//TO BE COMPLETED
        try {
            JulietAddress = myJuliet.getAcquaintance().getKey();
            JulietPort = myJuliet.getAcquaintance().getValue();
        } catch (Exception e) {
            e.getStackTrace();
        }

        System.out.println("PlayWriter: I've made acquaintance with Juliet");
    }


    //Request next verse: Send letters to lovers communicating the partner's love in previous verse
    public void requestVerseFromRomeo(int verse) {
        System.out.println("PlayWriter: Requesting verse " + verse + " from Romeo. -> (" + theNovel[verse-1][1] + ")");

			//TO BE COMPLETED
			try {
                RomeoMailbox = new Socket(RomeoAddress, RomeoPort);
                String request = (theNovel[verse - 1][1]) + "J";
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(RomeoMailbox.getOutputStream());
                outputStreamWriter.write(request);
                outputStreamWriter.flush();
            } catch (Exception e) {
                e.getStackTrace();
            }
            System.out.println("Romeo: Letter received");
    }


    //Request next verse: Send letters to lovers communicating the partner's love in previous verse
    public void requestVerseFromJuliet(int verse) {
        System.out.println("PlayWriter: Requesting verse " + verse + " from Juliet. -> (" + theNovel[verse-1][0] + ")");

			//TO BE COMPLETED
        try {
            JulietMailbox = new Socket(JulietAddress, JulietPort);
            String request = (theNovel[verse - 1][0]) + "R";
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(JulietMailbox.getOutputStream());
            outputStreamWriter.write(request);
            outputStreamWriter.flush();
        } catch (Exception e) {
            e.getStackTrace();
        }
        System.out.println("Romeo: Letter received");
    }


    //Receive letter from Romeo with renovated love for current verse
    public void receiveLetterFromRomeo(int verse) {
        System.out.println("PlayWriter: Receiving letter from Romeo for verse " + verse + ".");

            double tmp = 0;
			//TO BE COMPLETED
			try {
                InputStream inputStream = RomeoMailbox.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                StringBuffer stringBuffer = new StringBuffer();
                char c;
                while (true) {
                    c = (char) inputStreamReader.read();
                    if (c == 'R') {
                        break;
                    }
                    stringBuffer.append(c);
                }
                tmp = Double.parseDouble(stringBuffer.toString().substring(0,stringBuffer.length()));
                RomeoMailbox.close();
            } catch (IOException e) {
                System.out.println("Romeo I/O error " + e);
            }
            theNovel[verse][0] = tmp;
            System.out.println("Romeo: Exiting service iteration.");
            System.out.println("Romeo: Entering service iteration");
            System.out.println("Romeo: Awaiting Letter");

        System.out.println("PlayWriter: Romeo's verse " + verse + " -> " + theNovel[verse][0]);
    }

    //Receive letter from Juliet with renovated love from current verse
    public void receiveLetterFromJuliet(int verse) {

			//TO BE COMPLETED
        double tmp = 0;
        //TO BE COMPLETED
        try {
            InputStream inputStream = JulietMailbox.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            StringBuffer stringBuffer = new StringBuffer();
            char c;
            while (true) {
                c = (char) inputStreamReader.read();
                if (c == 'J') {
                    break;
                }
                stringBuffer.append(c);
            }
            tmp = Double.parseDouble(stringBuffer.toString().substring(0,stringBuffer.length()));
            JulietMailbox.close();
        } catch (IOException e) {
            System.out.println("Juliet I/O error " + e);
        }
        theNovel[verse][0] = tmp;
        System.out.println("Juliet: Exiting service iteration.");
        System.out.println("Juliet: Entering service iteration");
        System.out.println("Juliet: Awaiting Letter");

        System.out.println("PlayWriter: Juliet's verse " + verse + " -> " + theNovel[verse][1]);
    }





    //Let the story unfold
    public void storyClimax() {
        for (int verse = 1; verse < novelLength; verse++) {
            //Write verse
            System.out.println("PlayWriter: Writing verse " + verse + ".");

			//TO BE COMPLETED
			try {
                requestVerseFromRomeo(verse);                                               //mistake here
                receiveLetterFromRomeo(verse);
                requestVerseFromJuliet(verse);
                receiveLetterFromJuliet(verse);
            } catch (Exception e) {
                e.getStackTrace();
            }
            System.out.println("PlayWriter: Verse " + verse + " finished.");
        }
    }

    //Character's death
    public void charactersDeath() {

			//TO BE COMPLETED
			try {
                RomeoMailbox = new Socket(RomeoAddress, RomeoPort);
                String death = "T\n";
                OutputStream outputStream = RomeoMailbox.getOutputStream();
                outputStream.write(death.getBytes());
                outputStream.flush();
                RomeoMailbox.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        try {
            JulietMailbox = new Socket(JulietAddress, JulietPort);
            String death = "T\n";
            OutputStream outputStream = JulietMailbox.getOutputStream();
            outputStream.write(death.getBytes());
            outputStream.flush();
            JulietMailbox.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    //A novel consists of introduction, conflict, climax and denouement
    public void writeNovel() {
        System.out.println("PlayWriter: The Most Excellent and Lamentable Tragedy of Romeo and Juliet.");
        System.out.println("PlayWriter: A play in IV acts.");
        //Introduction,
        System.out.println("PlayWriter: Act I. Introduction.");
        this.createCharacters();
        //Conflict
        System.out.println("PlayWriter: Act II. Conflict.");
        this.charactersMakeAcquaintances();
        //Climax
        System.out.println("PlayWriter: Act III. Climax.");
        this.storyClimax();
        //Denouement
        System.out.println("PlayWriter: Act IV. Denouement.");
        this.charactersDeath();

    }


    //Dump novel to file
    public void dumpNovel() {
        FileWriter Fw = null;
        try {
            Fw = new FileWriter("RomeoAndJuliet.csv");
        } catch (IOException e) {
            System.out.println("PlayWriter: Unable to open novel file. " + e);
        }

        System.out.println("PlayWriter: Dumping novel. ");
        StringBuilder sb = new StringBuilder();
        for (int act = 0; act < novelLength; act++) {
            String tmp = theNovel[act][0] + ", " + theNovel[act][1] + "\n";
            sb.append(tmp);
            //System.out.print("PlayWriter [" + act + "]: " + tmp);
        }

        try {
            BufferedWriter br = new BufferedWriter(Fw);
            br.write(sb.toString());
            br.close();
        } catch (Exception e) {
            System.out.println("PlayWriter: Unable to dump novel. " + e);
        }
    }

    public static void main (String[] args) {
        PlayWriter Shakespeare = new PlayWriter();
        Shakespeare.writeNovel();
        Shakespeare.dumpNovel();
        System.exit(0);
    }


}
