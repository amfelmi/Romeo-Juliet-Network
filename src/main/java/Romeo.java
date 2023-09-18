/*
 * Romeo.java
 *
 * Romeo class.  Implements the Romeo subsystem of the Romeo and Juliet ODE system.
 */


import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetAddress;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

import javafx.util.Pair;

public class Romeo extends Thread {

    private ServerSocket ownServerSocket = null; //Romeo's (server) socket
    private Socket serviceMailbox = null; //Romeo's (service) socket

    private double currentLove = 0;
    private double a = 0; //The ODE constant

    //Class construtor
    public Romeo(double initialLove) {
        currentLove = initialLove;
        a = 0.02;
        try {

			//TO BE COMPLETED
            ownServerSocket = new ServerSocket(7778, 0, InetAddress.getByName("localhost"));
            System.out.println("Romeo: What lady is that, which doth enrich the hand\n" +
                    "       Of yonder knight?");
        } catch(Exception e) {
            System.out.println("Romeo: Failed to create own socket " + e);
        }
   }

    //Get acquaintance with lover;
    public Pair<InetAddress,Integer> getAcquaintance() {
        System.out.println("Romeo: Did my heart love till now? forswear it, sight! For I ne'er saw true beauty till this night.");

			//TO BE COMPLETED
        InetAddress romeoIP = ownServerSocket.getInetAddress();
        int romeoPort = ownServerSocket.getLocalPort();
        return new Pair<>(romeoIP, romeoPort);

    }


    //Retrieves the lover's love
    public double receiveLoveLetter()
    {

			//TO BE COMPLETED
        double tmp = 0;
        try {
            serviceMailbox = ownServerSocket.accept();
            InputStream inputStream = serviceMailbox.getInputStream();
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
        } catch (Exception e) {
            System.out.println("Romeo: Failed to receive love letter " + e);
        }
        return tmp;
    }


    //Love (The ODE system)
    //Given the lover's love at time t, estimate the next love value for Romeo
    public double renovateLove(double partnerLove){
        System.out.println("Romeo: But soft, what light through yonder window breaks?\n" +
                "       It is the east, and Juliet is the sun.");
        currentLove = currentLove+(a*partnerLove);
        return currentLove;
    }


    //Communicate love back to playwriter
    public void declareLove(){

			//TO BE COMPLETED
        try {
                OutputStream outputStream = serviceMailbox.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                outputStreamWriter.write(currentLove + "R");
                outputStreamWriter.flush();
                System.out.println("Romeo: I would I were thy bird. (->" + currentLove + "R)");
        } catch (Exception e) {
            System.out.println("Romeo: Failed to declare love " + e);
        }
    }

    //Execution
    public void run () {
        try {
            while (!this.isInterrupted()) {
                //Retrieve lover's current love
                double JulietLove = this.receiveLoveLetter();

                //Estimate new love value
                this.renovateLove(JulietLove);

                //Communicate love back to playwriter
                this.declareLove();
            }
        }catch (Exception e){
            System.out.println("Romeo: " + e);
        }
        if (this.isInterrupted()) {
            System.out.println("Romeo: Here's to my love. O true apothecary,\n" +
                    "Thy drugs are quick. Thus with a kiss I die." );
        }
    }

}

