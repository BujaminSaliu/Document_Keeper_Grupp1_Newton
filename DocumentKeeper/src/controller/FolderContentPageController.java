/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author louiseahokas
 */
public class FolderContentPageController implements Initializable {

    @FXML
    private Button button;
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose your files");
        List <File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("PDF Files", "*.pdf"));
        
        System.out.println(selectedFiles.get(0).getName());
        addTag();
    }
    
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
