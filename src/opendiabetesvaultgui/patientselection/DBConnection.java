/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.patientselection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import static opendiabetesvaultgui.launcher.FatherController.PREFS_FOR_ALL;

/**
 * Tool for connecting to the Database using the hsqldb jbdc Driver.
 *
 * @author Daniel Schäfer, Martin Steil, Julian Schwind, Kai Worsch
 */
public class DBConnection {

    //private static String DATABASE_URL = "jdbc:hsqldb:mem:vaultdb";
    //private static String DATABASE_URL = "jdbc:hsqldb:file:/Users/danielschafer/Desktop/testdb/testdb.db";
    //private static String DATABASE_URL = "jdbc:hsqldb:file:"+File.separator+"src"+File.separator+"opendiabetesvaultgui"+File.separator+"database"+File.separator+"test.db";
    //private static String DATABASE_URL = "jdbc:hsqldb:mem:vaultDB";
    //private static String DATABASE_URL = PREFS_FOR_ALL.get("pathDatabase", "");
    //private static String DATABASE_URL = "jdbc:hsqldb:file:"+pathString();
    private final static String DATABASE_URL = DatabaseType();
    private final static String user = "root";
    private final static String password = "root";
    private static Connection conn;

    public static Connection connect() throws SQLException, IOException {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver").newInstance();
            File yourFile = new File(PREFS_FOR_ALL.get("pathDatabase", "")
                    + File.separator + "vault.db");
            yourFile.createNewFile(); // if file already exists will do nothing
            FileOutputStream oFile = new FileOutputStream(yourFile, false);
        } catch (ClassNotFoundException ncfe) {
            //Driver not found
            System.err.println("Error: " + ncfe.getMessage());
        } catch (InstantiationException ie) {
            //Wrong Type
            System.err.println("Error: " + ie.getMessage());
        } catch (IllegalAccessException ae) {
            //Wrong Access Credentials
            System.err.println("Error: " + ae.getMessage());
        }
        System.out.println(pathString());
        System.out.println("DATABASE CONNECTED");
        conn = DriverManager.getConnection(DATABASE_URL, user, password);
        return conn;
    }
    
    public static String DatabaseType(){
        if (PREFS_FOR_ALL.getBoolean("databaseInMemory", false)) {
            return "jdbc:hsqldb:mem:vaultDB";
        }

        else {
            return "jdbc:hsqldb:file:" + pathString();
        }
    }

    public static Connection getConnection()
            throws SQLException, ClassNotFoundException, IOException {
        if (conn != null && !conn.isClosed()) {
            return conn;
        }

        connect();
        return conn;

    }

    public static void closeConnection() throws SQLException{
        Statement st = conn.createStatement();

        // db writes out to files and performs clean shuts down
        // otherwise there will be an unclean shutdown
        // when program ends
        System.out.println("CONNECTION CLOSED");
        st.execute("SHUTDOWN");
        conn.close();
    }

    private static String pathString() {
        StringBuilder test = new StringBuilder();
        test.append(PREFS_FOR_ALL.get("pathDatabase", ""));
        test.append(File.separator);
        test.append("vault.db");
        return test.toString();
    }

}
