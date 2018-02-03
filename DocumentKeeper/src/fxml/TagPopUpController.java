/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml;

import static controller.FolderContentPageController.oList;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import models.Document;
import models.Tag;
import repository.DBConnection;

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
    private ListView<Document> fileListView;
    @FXML
    private ListView<Tag> listViewTags;
    
    public ObservableList<Tag> obsListForTags = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList <Tag> tags = DBConnection.selectFromTags();
        obsListForTags = FXCollections.observableArrayList(tags);
        listViewTags.setItems(obsListForTags);
        
        
        fileListView.setItems(oList);
        fileListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        System.out.println("Från db" + DBConnection.selectFromTags());
         
    }   
    
    
    
}
