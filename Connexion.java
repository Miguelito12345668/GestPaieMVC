package models;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author MIGUEL PYTHON
 */
public class Connexion {

    private static final String URL = "jdbc:mysql://localhost:3306/gestpaie";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            //Chargement du driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver charge");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }

}
