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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.Document;
import models.Tag;

/**
 *
 * @author rille
 */
public class DBConnection {

    private static Statement stmt = null;
    private static Connection conn = null;

    public static void createConnection() {
        try {
            String url = "jdbc:derby://localhost:1527/DocumentKeeperDB";
            String username = "root";
            String password = "root";

            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void insertIntoTags(String test) {
        try {
            stmt = conn.createStatement();
            stmt.execute("insert into " + "TAGS (NAME) values ('" + test + "')");
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }

    public static void insertIntoFiles(String name, double size, String type, String path, List<Tag> tags) throws SQLException {

        Statement stmt = null;
        ResultSet rs;
        String query = "insert into " + "FILES (NAME, SIZE, TYPE, PATH) values ('" + name + "', " + size + ", '" + type + "', '" + path + "')";

        stmt = conn.createStatement();

        stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);   // Indicate you want automatically generated keys
        rs = stmt.getGeneratedKeys();
        while (rs.next()) {
            java.math.BigDecimal idColVar = rs.getBigDecimal(1);
            // Get automatically generated key value
            tags.forEach((tag) -> {
                insertIntoFile_Has_Tags(idColVar.intValueExact(), tag.getId());
            });
        }
        rs.close();
        stmt.close();

    }

    public static void insertIntoFile_Has_Tags(int fileId, int tagId) {
        try {
            stmt = conn.createStatement();
            stmt.execute("INSERT INTO ROOT.FILE_HAS_TAGS (FILE_ID, TAG_ID) \n"
                    + "	VALUES (" + fileId + ", " + tagId + ")");
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }

    public static ArrayList<Tag> selectFromTags() {
        ArrayList<Tag> tagList = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from " + "TAGS");

            while (results.next()) {
                int id = results.getInt(1);
                String restName = results.getString(2);
                //System.out.println(id + "\t\t" + restName + "\t\t");

                Tag tag = new Tag(id, restName);
                tagList.add(tag);

            }
            results.close();
            stmt.close();
            return tagList;

        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return tagList;
    }
    
      public static ArrayList<Document> selectFromFiles() {
        ArrayList<Document> fileList = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from " + "FILES");

            while (results.next()) {
                int id = results.getInt(1);
                String restName = results.getString(2);
                Date date = results.getDate(3);
                double size = results.getDouble(4);
                String type = results.getString(5);
                String path = results.getString(6);

                Document file = new Document(id, restName, date, type, size ,path);
                //System.out.println(file);
                fileList.add(file);

            }
            results.close();
            stmt.close();
            return fileList;

        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return fileList;
    }
}  
