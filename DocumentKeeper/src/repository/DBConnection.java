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
import java.util.Arrays;
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

    public static void linkFiles(int a, int b) {
        try {
            stmt = conn.createStatement();
            stmt.execute("INSERT INTO root.LINKED_FILES (FILE_A, FILE_B) VALUES (" + a + "," + b + ")");
            System.out.println("LINKED file with index: " + b + " and file with index: " + a);
            stmt.close();
        } catch (SQLException sqlExcept) {
            System.out.println("THIS FILES ARE ALREADY LINKED");
            
        }  
    }
        
    public static ArrayList<Document> getLinkedFiles(int a){
         ArrayList<Document> fileList = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT DISTINCT FILES.* FROM LINKED_FILES, FILES WHERE (FILE_A ="+ a +" OR FILE_B ="+ a +") AND FILE_A = ID OR FILE_B = ID");
            while (results.next()) {
               int id = results.getInt(1);
                String restName = results.getString(2);
                Date date = results.getDate(3);
                int size = results.getInt(4);
                String type = results.getString(5);
                String path = results.getString(6);
                ArrayList<Tag> tagList = new ArrayList<>();
                tagList=selectFromTagsFromFile(id);
                Document file = new Document(id, restName, date, type, size ,path);
                file.setTags(tagList);
                fileList.add(file);
            }
            results.close();
            stmt.close();
             } catch (SQLException sqlExcept) {
            System.out.println("Could not get linked files");
            
        }  
            return fileList;
      
       
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
                    + "    VALUES (" + fileId + ", " + tagId + ")");
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
                int size = results.getInt(4);
                String type = results.getString(5);
                String path = results.getString(6);
                ArrayList<Tag> tagList = new ArrayList<>();
                tagList = selectFromTagsFromFile(id);
                Document file = new Document(id, restName, date, type, size, path);
                file.setTags(tagList);
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

    public static ArrayList<Tag> selectFromTagsFromFile(int fileId) {
        ArrayList<Tag> tagList = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT t.* FROM TAGS t INNER JOIN FILE_HAS_TAGS ft on t.ID = ft.TAG_ID INNER JOIN FILES f on f.ID = ft.FILE_ID where f.ID =" + fileId);
            while (results.next()) {
                int id = results.getInt(1);
                String restName = results.getString(2);
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

    public static ArrayList<Document> search(String search) {
        ArrayList<Document> fileList = new ArrayList<>();

        //this searches for documents with same tags
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT distinct f.* FROM FILES f INNER JOIN FILE_HAS_TAGS ft on f.ID = ft.FILE_ID INNER JOIN TAGS t on t.ID = ft.TAG_ID where t.NAME like '" + search + "%' or UPPER(t.NAME) like UPPER('" + search + "%') order by f.ID");

            while (results.next()) {
                int id = results.getInt(1);
                String restName = results.getString(2);
                Date date = results.getDate(3);
                int size = results.getInt(4);
                String type = results.getString(5);
                String path = results.getString(6);
                ArrayList<Tag> tagList = new ArrayList<>();
                tagList = selectFromTagsFromFile(id);

                Document file = new Document(id, restName, date, type, size, path);
                file.setTags(tagList);
                fileList.add(file);

            }
            results.close();
            stmt.close();
            //this searches for documents for same name.
            ArrayList<Document> names = searchForFileNames(search);
            //this searches for documents for same type.
            ArrayList<Document> types;
            //this if makes sure ".jpg" and "jpg" results in a successful search
            if (search.indexOf(".") == 0) {
                types = searchForFileTypes(search.substring(1));
            } else {
                types = searchForFileTypes(search);
            }
            //this list will be "code" searched instead of "sql" searched.
            ArrayList<Document> dates = DBConnection.selectFromFiles();

            //Adding and removing same object to avoid duplicates
            for (Document doc : names) {
                fileList.removeIf(p -> p.getId() == (doc.getId()));
                fileList.add(doc);
            }
            for (Document doc : types) {
                fileList.removeIf(p -> p.getId() == (doc.getId()));
                fileList.add(doc);
            }
            for (Document doc : dates) {
                if (doc.getDate().toString().contains(search)) {
                    fileList.removeIf(p -> p.getId() == (doc.getId()));
                    fileList.add(doc);
                }

            }

            return fileList;

        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return fileList;
    }

    public static ArrayList<Document> searchForFileNames(String search) {
        ArrayList<Document> fileList = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT f.* FROM FILES f where f.NAME like '" + search + "%' or UPPER(f.NAME) like UPPER('" + search + "%')");

            while (results.next()) {
                int id = results.getInt(1);
                String restName = results.getString(2);
                Date date = results.getDate(3);
                int size = results.getInt(4);
                String type = results.getString(5);
                String path = results.getString(6);
                ArrayList<Tag> tagList = new ArrayList<>();
                tagList = selectFromTagsFromFile(id);

                Document file = new Document(id, restName, date, type, size, path);
                file.setTags(tagList);
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

    public static ArrayList<Document> searchForFileTypes(String search) {
        ArrayList<Document> fileList = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT f.* FROM FILES f where f.TYPE like '" + search + "%' or UPPER(f.TYPE) like UPPER('" + search + "%')");

            while (results.next()) {
                int id = results.getInt(1);
                String restName = results.getString(2);
                Date date = results.getDate(3);
                int size = results.getInt(4);
                String type = results.getString(5);
                String path = results.getString(6);
                ArrayList<Tag> tagList = new ArrayList<>();
                tagList = selectFromTagsFromFile(id);

                Document file = new Document(id, restName, date, type, size, path);
                file.setTags(tagList);
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
