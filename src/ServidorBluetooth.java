import java.io.*;
import java.sql.Statement;
import javax.microedition.io.*;
import javax.bluetooth.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;



        


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
         * Se van a guardar en una variable tipo String la fecha y la hora actual
         * al momento de hacer la firma de Entrada y de Salida
         * Para esto se crea el objeto manejoFechas usando la clase ManejoFechas.java
         */
        String lineaRecibida= bReader.readLine();
        String tipoFirma = lineaRecibida.substring(0, 1);
        int intFirma = Integer.parseInt(tipoFirma);
        String tipo_firma = "Firma de entrada";
        if(intFirma ==1){
          tipo_firma = "Firma de entrada";
        }else{
            if(intFirma ==2){
          tipo_firma = "Firma de salida";
            }
        }
        String idRecibido = lineaRecibida.substring(2,7);
        int sizeLinea = lineaRecibida.length();
        String temaRecibido = lineaRecibida.substring(8, sizeLinea);
        String fechaActual = ManejoFechas.getFechaActual();
        String horaActual = ManejoFechas.getHoraActual();

        try
        { 

                    java.sql.Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/checador", "checador","checador123");
                    Statement st = conexion.createStatement();
                    String consulta = "SELECT * FROM profesores WHERE `id_profesor` = '"+idRecibido+"';";
                    ResultSet rs = st.executeQuery(consulta);
                    if(rs.next()){
                    String nombre = rs.getString("nombre");
                    String apellido_pat = rs.getString("apellido_pat");
                    String apellido_mat = rs.getString("apellido_mat");
                    String insertar ="INSERT INTO  `registros` (`nombre`, `apellido_pat`, `apellido_mat`, `tipo_firma`, `fecha`, `hora`, `tema`) VALUES ('"+nombre+"', '"+apellido_pat+"', '"+apellido_mat+"','"+tipo_firma+"', '"+fechaActual+"', '"+horaActual+"', '"+temaRecibido+"');";
                        st.executeUpdate(insertar);
                    }
                    rs.close();
        } catch (SQLException ex) {
                    Logger.getLogger(ServidorBluetooth.class.getName()).log(Level.SEVERE, null, ex);
                }        
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
                }
        }
    }
}