package conect;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Leyaim
 */
public class conexion {
private String driver ="com.mysql.jdbc.Driver";
private String cadenaConexion = "jdbc:mysql://127.0.0.1/ticketspiv2";
private String user ="root";
private String passw ="";

public Connection con;

public conexion(){
try{

Class.forName(driver);
con = DriverManager.getConnection(cadenaConexion,user,passw);

 JOptionPane.showMessageDialog(null, "si funciona");
}catch(Exception e){

    JOptionPane.showMessageDialog(null, "No funca" + e.getMessage());
}



}

public Connection getConexion(){
return con;
}

 public static void main(String[] args) {
   conexion con = new conexion();
 
 }
  
    
}
