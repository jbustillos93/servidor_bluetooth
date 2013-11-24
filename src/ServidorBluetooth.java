import java.io.*;
import javax.microedition.io.*;
import javax.bluetooth.*;


/**
 * La clase ServidorBluetooth implementa un servidor
 * que acepta una línea enviada por un cliente bluetooth
 * y después dicha linea la guarda en una base de datos.
 * Para la creación del servidor se utilizó la libreria BlueCove 
 * esta proporciona una manera sencilla de establecer conexiones
 * para Java utilizando bluetooth.
 * http://bluecove.org/
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
         * Guardamos mediante la clase InputStream de Java IO para leer
         * la información de entrada del cliente
         */
        InputStream entradaCliente=connection.openInputStream();
        /**
         * Guardamos en buffer utlizando la clase BufferedReader de Java IO para
         * posteriormente pasarlo al string con el nombre de lineaRecibida
         */
        BufferedReader bReader=new BufferedReader(new InputStreamReader(entradaCliente));
        /**
         * Cerramos la conexión entrante utilizando el método close
         */
        streamConnNotifier.close();
        /**
         * FALTA POR HACER:
         *          *REALIZAR LA CONEXIÓN CON MYSQL
         *          *GUARDAR LA ENTRADA O SALIDA DEPENDIENDO DEL CASO
         *          *IMPRIMIR LA FECHA DE SALIDA O ENTRADA
        */
        /**
         * Se van a guardar en una variable tipo String la fecha y la hora actual
         * al momento de hacer la firma de Entrada y de Salida
         * Para esto se crea el objeto manejoFechas usando la clase ManejoFechas.java
         */
        String lineaRecibida= bReader.readLine();
        String fechaActual = ManejoFechas.getFechaActual();
        String horaActual = ManejoFechas.getHoraActual();
        /*
        PARA PRUEBA VAMOS A SACAR EN PANTALLA EL DATO RECIBIDO 
        POR PARTE DEL CLIENTE LA HORA Y FECHA
        */
        System.out.println(lineaRecibida + "\t" + fechaActual + "\t" + horaActual);
        /*
        EJEMPLO DE SALIDA @23-11-2013: 
        2 12e45	23-11-2013	11:42:59
        */
        
    }
  
  
    public static void main(String[] args) throws IOException, InterruptedException  {
    	/**
         * Repite la esucha del servidor mientras este activo el proceso, mediante
         * un bucle indeterminado.
         */
        boolean repetir = true;
                while(repetir)
    	{ 
                try{
                    ServidorBluetooth servidorBluetooth= new ServidorBluetooth();
                    servidorBluetooth.iniciarServidor();
                }
                catch (IOException e){
                    //En caso de haber una excepción o error, reiniciar el servidor
                    System.out.println(e);
                    System.out.println("Reiniciando servidor...");
                }
        }
    }
}