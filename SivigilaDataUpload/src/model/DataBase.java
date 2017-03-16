/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Fabian
 */
public class DataBase {

    private static Connection connection = null;

    public DataBase() {

        if (connection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:SivigilaDataBase.db");
                createOrUpdateDB();
                getConnection().setAutoCommit(false);

            } catch (ClassNotFoundException | SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al conectar :" + ex.getMessage());
                //Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                /*try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e);
                }*/
            }
        }
    }

    public Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:SivigilaDataBase.db");
            createOrUpdateDB();

        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al conectar :" + ex.getMessage());
            //Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    private void createOrUpdateDB() throws SQLException {
        if (connection != null) {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String line = "";
            String data = "";
            try {
                BufferedReader r = new BufferedReader(new FileReader(new File("script.txt")));
                while ((line = r.readLine()) != null) {
                    data += line;
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            }
            statement.executeUpdate(data);
        }
    }
    

    public PreparedStatement statement(String sql) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return statement;
    }
    
    public void executeQuery( String sql ){
        try {
            Statement sta = connection.createStatement();
            sta.executeQuery(sql);
            connection.commit();

        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        
    }

}
