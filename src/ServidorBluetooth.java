import java.io.*;
import javax.microedition.io.*;
import javax.bluetooth.*;


/**
 * La clase ServidorBluetooth implementa un servidor
 * que acepta una línea enviada por un cliente bluetooth
 * y después dicha linea la guarda en una base de datos.
*/
public class ServidorBluetooth {

    
    //El método iniciarServir, inicia el servidor de esucha
    private void iniciarServidor() throws IOException{
  
        /**
         * Define un UUID para el servidor, el cual debe ser único para que el 
         * cliente lo identifique.
         * La intención de los UUID es habilitar, a los sistemas distribuidos, 
         * un identificador de información único sin una importante coordinación 
         * central. 
         * http://es.wikipedia.org/wiki/Universally_unique_identifier
        */
        UUID uuid = new UUID("1101", true);
        /**
         * Crea la url del servicio el cual va estar alojado dentro de donde 
         * la computadora que lo ejecute y tendrá el nombre de ChecadorBluetooth
         * */
        String urlConexion = "btspp://localhost:" + uuid +";name=ChecadorBluetooth";
        /**
         * Abrimos el servidor utilizando el método open de BlueCove
         */
        StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier)Connector.open( urlConexion );
       /**
        * Esperamos una nueva conexión y aceptamos cualquiera, utilizando el método
        * acceptAndOpen de bluecove
        */
        System.out.println("\nEsperando conexión...");
        StreamConnection connection=streamConnNotifier.acceptAndOpen();
        /**
         * Guardamos en la variable dev la información del dispositivo en caso de
         necesitarla
         */
        RemoteDevice dev = RemoteDevice.getRemoteDevice(connection); 
        /**
         * Guardamos mediante la clase InputStream de Java IO para leer
         * la información de entrada del cliente
         */
        InputStream entradaCliente=connection.openInputStream();
        /**
         * Guardamos en buffer utlizando la clase BufferedReader de Java IO para
         * posteriormente pasarlo al string con el nombre de lineaRecibida
         */
        BufferedReader bReader=new BufferedReader(new InputStreamReader(entradaCliente));
        String lineaRecibida= bReader.readLine();
        System.out.println(lineaRecibida);
        /**
         * Cerramos la conexión entrante utilizando el método close
         */
        streamConnNotifier.close();
        /**
         * FALTA POR HACER:
         *          *REALIZAR LA CONEXIÓN CON MYSQL
         *          *GUARDAR LA ENTRADA O SALIDA DEPENDIENDO DEL CASO
         *          *IMPRIMIR LA FECHA
        */
    }
  
  
    public static void main(String[] args) throws IOException {
    	while(true)
    	{       
      ServidorBluetooth servidorBluetooth= new ServidorBluetooth();
        servidorBluetooth.iniciarServidor();
    	}
    }
}