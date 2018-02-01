/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * FXML Controller class
 *
 * @author louiseahokas
 */
public class FolderContentPageController implements Initializable {

    @FXML
    private Button button;
    
    List <File> selectedFiles;
    
    @FXML
    private void addFileButtonAction(ActionEvent event) {        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose your files");
        selectedFiles = fileChooser.showOpenMultipleDialog(null);
        fileChooser.getExtensionFilters().addAll();
        try {
            for (int i = 0; i < selectedFiles.size(); i++) {
            System.out.println("Selected files: " + selectedFiles.get(i).getName() 
                    + " Amount of files: "+ selectedFiles.size());
        }
        } catch (Exception e) {
            System.out.println("Canceled");
        }
        
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
