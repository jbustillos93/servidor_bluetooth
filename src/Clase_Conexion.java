/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;
/**
 *
 * @author Jorge
 */
public class Clase_Conexion {

    private Connection conexion=null;
    private String servidor="127.0.0.1";
    private String database="checador";
    private String usuario="checador";
    private String password="checador123";
    private String url="jdbc:mysql://"+servidor+"/"+database;;
    
    public Clase_Conexion(){
        try {


            Class.forName("com.mysql.jdbc.Driver");  
            conexion=DriverManager.getConnection(url, usuario, password);
            System.out.println("Conexion a Base de Datos "+url+" . . . . .Ok");

        }
        catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex);
        }
    }

    public Connection getConexion(){
        return conexion;
    }

    public Connection cerrarConexion(){
        try {
            conexion.close();
             System.out.println("Cerrando conexion a "+url+" . . . . . Ok");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        conexion=null;
        return conexion;
    }
}