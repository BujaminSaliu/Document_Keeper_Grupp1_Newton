/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
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
import models.Tag;

/**
 * FXML Controller class
 *
 * @author louiseahokas
 */
public class FolderContentPageController implements Initializable {

    @FXML

    private Button addFileButton;
    
    List <File> selectedFiles;
    
  
 


    public static ObservableList<Document> oList = FXCollections.observableArrayList();

    @FXML

    private void addFileButtonAction(ActionEvent event) throws IOException {
        oList.clear();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose your files");
        selectedFiles = fileChooser.showOpenMultipleDialog(null);
        fileChooser.getExtensionFilters().addAll();
        try {
            for (int i = 0; i < selectedFiles.size(); i++) {
                String file = selectedFiles.get(i).getName();
                String name = file.substring(0, file.indexOf("."));
                String fileType = file.substring(file.indexOf(".") + 1, selectedFiles.get(i).getName().length());

                //formats the file size
                double fileSizeInBytes = selectedFiles.get(i).length();
                double fileSizeInKB = fileSizeInBytes / 1024;

                fileSizeInKB = fileSizeInKB * 100;
                fileSizeInKB = Math.round(fileSizeInKB);
                fileSizeInKB = fileSizeInKB / 100;
                String path = selectedFiles.get(i).getAbsolutePath();

                Document files = new Document();
                files.setName(name);
                files.setType(fileType);
                files.setSize(fileSizeInKB);
                files.setPath(path);
                //tags list hinders nullpointer exception
                List tags = new ArrayList<Tag>();
                files.setTags(tags);

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
        stage.initOwner(addFileButton.getScene().getWindow());
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
