/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
/**
 *
 * @author rille
 */
public class DBConnection {
    
    private static Statement stmt = null;
    private static Connection conn = null;
     
    public static void createConnection()
    {
        try{
            String url = "jdbc:derby://localhost:1527/DocumentKeeperDB";
            String username = "root";
            String password = "root";
            
            conn = DriverManager.getConnection(url, username, password);
        }
        catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static void insertIntoTags(String test)
    {
        try
        {
            stmt = conn.createStatement();
            stmt.execute("insert into " + "TAGS (NAME) values ('" + test + "')");
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    public static void selectFromTags()
    {
        try
        {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from " + "TAGS");

            while(results.next())
            {
                int id = results.getInt(1);
                String restName = results.getString(2);
                System.out.println(id + "\t\t" + restName + "\t\t");
            }
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
}


