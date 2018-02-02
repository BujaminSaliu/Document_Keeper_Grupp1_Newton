/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;



import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import models.Document;



/**
 * FXML Controller class
 *
 * @author louiseahokas
 */
public class FolderContentPageController implements Initializable {

    @FXML
    private Button button;
    
    List <File> selectedFiles;
    
  
    public static ObservableList<Document> oList = FXCollections.observableArrayList();
    
   
    @FXML

    private void addFileButtonAction(ActionEvent event) throws IOException {        

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose your files");
        selectedFiles = fileChooser.showOpenMultipleDialog(null);
        fileChooser.getExtensionFilters().addAll();
        try {
            for (int i = 0; i < selectedFiles.size(); i++) {
                Document files = new Document(1,selectedFiles.get(i).getName(),new Date(), "jpg",233.45,"user/desktop/bla.pdf");
                
                
            System.out.println("Selected files: " + selectedFiles.get(i).getName() 
                    + "  files amount : "+ selectedFiles.size());
            oList.add(files);
             
        }
        } catch (Exception e) {
            System.out.println(e);
        }
        
        addTag();
    }
    
    // Document måste göras om till objekt nu är det bara en string som visas
    
    @FXML
    private void addTag() throws IOException { // läger till kunder i popUp1
        
        
        Stage stage;
        Parent root;
        
        stage = new Stage();
        stage.setTitle("Lägg till namntagg");
        stage.setResizable(false);
        root = FXMLLoader.load(getClass().getResource("/fxml/TagPopUp.fxml"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(button.getScene().getWindow());
        stage.setOnCloseRequest((WindowEvent we) -> {
            stage.close();
        });
        stage.showAndWait(); // öppnar popUp


    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
