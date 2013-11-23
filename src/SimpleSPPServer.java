import java.io.*;
import javax.microedition.io.*;
import javax.bluetooth.*;


/**
* Class that implements an SPP Server which accepts single line of
* message from an SPP client and sends a single line of response to the client.
*/
public class SimpleSPPServer {
    
    //start server
    private void startServer() throws IOException{
  
        //Create a UUID for SPP
        UUID uuid = new UUID("1101", true);
        //Create the servicve url
        String connectionString = "btspp://localhost:" + uuid +";name=Checador Bluetooth";
        //open server url
        StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier)Connector.open( connectionString );
        //Wait for client connection
        System.out.println("\nEsperando conexión...");
        StreamConnection connection=streamConnNotifier.acceptAndOpen();
        RemoteDevice dev = RemoteDevice.getRemoteDevice(connection); 
        //read string from spp client
        InputStream inStream=connection.openInputStream();
        BufferedReader bReader=new BufferedReader(new InputStreamReader(inStream));
        String lineRead= "FECHA \t" + bReader.readLine();
        System.out.println(lineRead);
        streamConnNotifier.close();
  
    }
  
  
    public static void main(String[] args) throws IOException {
    	while(true)
    	{       
        SimpleSPPServer sampleSPPServer=new SimpleSPPServer();
        sampleSPPServer.startServer();
    	}
    }
}