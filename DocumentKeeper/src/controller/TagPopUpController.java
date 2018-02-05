/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static controller.FolderContentPageController.oList;
import java.awt.event.MouseEvent;
import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
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
    
    public static boolean filesAdded = false;
    
    @FXML
    private ListView<Document> fileListView;
    @FXML
    private ListView<Tag> listViewTags;
    @FXML
    private Button importButton;

    public ObservableList<Tag> obsListForTags = FXCollections.observableArrayList();

    @FXML
    private void addFiles(ActionEvent event) throws IOException {

        ObservableList<Document> selectedFiles = fileListView.getItems();

        for (Document doc : selectedFiles) {

            try {
                DBConnection.insertIntoFiles(doc.getName(), doc.getSize(), doc.getType(), doc.getPath(), doc.getTags());
                
            } catch (Exception e) {
                System.out.println(e);
                Alert alert = new Alert(AlertType.ERROR, "Filerna finns redan lagrade", ButtonType.OK);
                alert.showAndWait();
                return;
            }
        }
        filesAdded = true;
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void fileItemClicked(javafx.scene.input.MouseEvent event) throws IOException {
        listViewTags.getSelectionModel().clearSelection();

        if (!(fileListView.getSelectionModel().getSelectedIndex() == -1)) {

            try {
                for (Tag tag : fileListView.getSelectionModel().getSelectedItem().getTags()) {

                    listViewTags.getSelectionModel().select(tag);
                    

                }
            } catch (Exception e) {
                System.out.println(e);
            }

        }

    }

    @FXML
    private void tagItemClicked(javafx.scene.input.MouseEvent event) throws IOException {

        ObservableList<Document> selectedFiles = fileListView.getSelectionModel().getSelectedItems();
        ObservableList<Tag> selectedTags = listViewTags.getSelectionModel().getSelectedItems();

        if (!(fileListView.getSelectionModel().getSelectedIndex() == -1)) {

            for (Document doc : selectedFiles) {
                List tags = new ArrayList<Tag>();
                for (Tag tag : selectedTags) {
                    tags.add(tag);
                }

                doc.setTags(tags);

            }

        }
        fileListView.refresh();
    }
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<Tag> tags = DBConnection.selectFromTags();
        obsListForTags = FXCollections.observableArrayList(tags);
        listViewTags.setItems(obsListForTags);
        
        
        fileListView.setItems(oList);
        fileListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
         
        listViewTags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        fileListView.scrollTo(0);
        fileListView.getSelectionModel().select(0);

    }
}
