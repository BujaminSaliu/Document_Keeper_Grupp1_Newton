/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml;

import static controller.FolderContentPageController.oList;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author ramonachantaf
 */
public class TagPopUpController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private ListView fileList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileList.setItems(oList);
    }   
    
    
    
}
