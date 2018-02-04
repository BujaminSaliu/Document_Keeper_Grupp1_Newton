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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import models.Document;
import models.Tag;
import repository.DBConnection;

/**
 * FXML Controller class
 *
 * @author louiseahokas
 */
public class FolderContentPageController implements Initializable {

    @FXML

    private Button addFileButton;
    
    List <File> selectedFiles;
    
    private GridPane gridPane;
    
    @FXML
    private VBox newFileBox;
    
    @FXML
    private ScrollPane scrollPaneStartPage;

    public static ObservableList<Document> oList = FXCollections.observableArrayList();
    

    @FXML

    private void addFileButtonAction(ActionEvent event) throws IOException {
       
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose your files");
        selectedFiles = fileChooser.showOpenMultipleDialog(null);
        fileChooser.getExtensionFilters().addAll();
        try {
            for (int i = 0; i < selectedFiles.size(); i++) {
                String file = selectedFiles.get(i).getCanonicalFile().getName();
                String name = file.substring(0, file.indexOf("."));
                String fileType = file.substring(file.indexOf(".") + 1, selectedFiles.get(i).getCanonicalFile().getName().length());

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
        displayChosenFiles();
    }
    
    private void displayChosenFiles() {
        gridPane.getChildren().removeAll(gridPane.getChildren());
        gridPane.add(newFileBox, 0, 0);
        int columnCounter = 1;
        int rowCounter = 0;
       for(Document file : oList){
         VBox vBox = new VBox();
         vBox.setAlignment(Pos.TOP_CENTER);
         Label fileName = new Label(file.getName() + "."+ file.getType());
         fileName.setAlignment(Pos.CENTER);
         fileName.setTextAlignment(TextAlignment.CENTER);
         fileName.setWrapText(true);
         ImageView fileImg = new ImageView();
         fileImg.setImage(new Image("/fxml/fileImage.png"));
         fileImg.setFitHeight(78);
         fileImg.setFitWidth(63);
         vBox.getChildren().addAll(fileImg,fileName);
       
         fileName.setMaxWidth(120);
         if(columnCounter < 4){
             gridPane.add(vBox, columnCounter, rowCounter);
         }else{
             rowCounter++;
             columnCounter = 0;
             gridPane.add(vBox, columnCounter, rowCounter);
             RowConstraints row = new RowConstraints();
             row.setMinHeight(134);
             gridPane.setPadding(new Insets(5, 0, 0, 0));
             gridPane.getRowConstraints().add(row);
             scrollPaneStartPage.setContent(gridPane);
         }
         columnCounter++;
     }
    }

    
    @FXML
    private void addTag() throws IOException { 

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
        DBConnection.createConnection();
        DBConnection.selectFromFiles();
        
        gridPane = new GridPane();
        gridPane.setMinWidth(527);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(newFileBox, 0, 0);
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(25);
        gridPane.getColumnConstraints().addAll(col, col, col, col);
        scrollPaneStartPage.setContent(gridPane);
    }

}
