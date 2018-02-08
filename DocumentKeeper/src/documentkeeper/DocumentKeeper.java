/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documentkeeper;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import repository.DBConnection;

/**
 *
 * @author Grupp 1 & 2 Newton 2018
 */

public class DocumentKeeper extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/FolderContentPage.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Document Keeper Grupp 1 & 2 Newton 2018");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest((WindowEvent we) -> {
            final String dir = System.getProperty("user.dir");
            
            File directory = new File(dir + "/src/tempFiles");
            
            // Get all files in directory
            File[] files = directory.listFiles();
            
            for (File file : files) {
                
                // Delete each file
                if (!file.delete()) {
                    we.consume();
                    Alert alert = new Alert(AlertType.WARNING,
                            "Vänligen stäng alla öppna filer innan programmet avlutas.",
                            ButtonType.OK);
                    alert.showAndWait();
                    // Failed to delete file
                    System.out.println("Failed to delete " + file);                    
                }                
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DBConnection db = new DBConnection();
        db.createConnection();

        launch(args);
    }
}
