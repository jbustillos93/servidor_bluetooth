import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
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
         * Cerramos la conexión entrante utilizando el método close.
         */
        streamConnNotifier.close();

        /**
         * lineaRecibida contiene el String proveniente del cliente Bluetooth
         * (telefono android).
         */
        String lineaRecibida= bReader.readLine();
        /**
         * tipoFirma contiene un string que parte del primer caracter de la 
         * LineaRecibida
         *          Puede tener dos valores
         *              1 = Firma de entrada
         *              2 = Firma de salida.
         */        
        String tipoFirma = lineaRecibida.substring(0, 1);
        /**
         * intFirma contiene tipoFirma pero en tipo de dato entero.
         */          
        int intFirma = Integer.parseInt(tipoFirma);
        /**
         * tipo_firma contiene una cadena correspondiente al tipo de firma 
         *  firma de entrada o firma de salida.
         */            
        String tipo_firma = "Firma de entrada";
        //Si int firma = 1: tipo_firma  = firma de entrada.
            if(intFirma ==1){
                tipo_firma = "Firma de entrada";
            }else{
            //Si int firma = 2: tipo_firma  = firma de salida.            
              if(intFirma ==2){
                tipo_firma = "Firma de salida";
              }
            }
        /**
         * idRecibido: contiene 5 caracteres contados a partir del
         * tercer caracter; este dato es correspondiente a la clave que
         * envia el cliente bluetooth (telefono android).
         */
        String idRecibido = lineaRecibida.substring(2,7);
        //sizeLinea tiene el tamaño del String lineaRecibida
        int sizeLinea = lineaRecibida.length();
        /**
         * temaRecibido: contiene un string con el tema que va a firmar el 
         * cliente bluetooth (telefono android). Se separa de la cadena 
         * lineaRecibida a paritir del noveno caracter, y contiene n numero 
         * de caracteres, donde n es igual a sizeLinea.
         */
        String temaRecibido = lineaRecibida.substring(8, sizeLinea);
        /**
         * Utilizando la clase ManejoFechas se saca la fecha y hora actual del
         * servidor.
         */
        String fechaActual = ManejoFechas.getFechaActual();
        String horaActual = ManejoFechas.getHoraActual();

        try
        { 
                    /**
                     * Crea una conexión usando JDBC y MySQL
                     * Statement = Para enviar comandos SQL a la base de datos, se usa la clase Statement de java
                     * Consulta = Tiene el comando SQL para buscar dentro de la tabla profesores al profesor
                     *            cuyo id_profesor sea igual a idRecibo
                     * ResultSet = El ResultSet es una representación de los resultados de la consulta
                     * rs= se llama a los resultados mediante executeQuery en donde se especifica el comando SQL a 
                     *      utilizar, en este caso Consulta
                     * rs.next() = va a devolver verdadero en caso de tener un resultado.         
                     */
                    java.sql.Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/checador", "checador","checador123");
                    Statement st = conexion.createStatement();
                    String consulta = "SELECT * FROM profesores WHERE `id_profesor` = '"+idRecibido+"';";
                    ResultSet rs = st.executeQuery(consulta);
                    if(rs.next()){
                        /**
                         * Después de next(), el resultado recién traido está disponible en el rs
                         * mediante el método getString(nombrecolumna) se recaba la cadena correspondiente al campo
                         * identificado mediante la cadena nombrecolumna
                         * es decir rs.getString("nombre"); va a devolver la cadena contenida en la columna 
                         * nombre dentro de la base de datos profesores donde el id_profesor sea igual a idRecibido.
                         */
                        //Cadena nombre contiene la columna nombre de la tabla profesores
                        String nombre = rs.getString("nombre");
                        //Cadena apellido_pat contiene la columna apellido_pat de la tabla profesores                        
                        String apellido_pat = rs.getString("apellido_pat");
                        //Cadena apellido_mat contiene la columna apellido_mat de la tabla profesores                        
                        String apellido_mat = rs.getString("apellido_mat");
                        /**
                         * Después de tener los datos necesarios para registrar la firma de entrada 
                         * o de salida se va a construir el comando SQL para insertar una nueva firma
                         * utilizando las variables construidas previamente.
                         */
                        String insertar ="INSERT INTO  `registros` (`nombre`, `apellido_pat`, `apellido_mat`, `tipo_firma`, `fecha`, `hora`, `tema`) VALUES ('"+nombre+"', '"+apellido_pat+"', '"+apellido_mat+"','"+tipo_firma+"', '"+fechaActual+"', '"+horaActual+"', '"+temaRecibido+"');";
                        //Mediante el método executeUpdate() se ejecuta el comando SQL en la base de datos.
                        st.executeUpdate(insertar);
                        //Muestra mensaje en la pantalla
                        System.out.println("\nFirma registrada exitosamente.");
                    }
                    rs.close(); //Cierra los resultados
        } catch (SQLException ex) {
            //en caso de error
            System.out.println("\nFirma no registrada.");
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