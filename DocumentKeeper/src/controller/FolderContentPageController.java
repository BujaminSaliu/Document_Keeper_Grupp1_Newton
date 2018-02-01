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
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) {        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose your files");
        List <File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("PDF Files", "*.pdf"));
        
        System.out.println(selectedFiles.get(0).getName());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
