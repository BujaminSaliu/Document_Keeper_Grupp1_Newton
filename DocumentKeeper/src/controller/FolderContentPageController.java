/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static controller.TagPopUpController.filesAdded;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.file.Files;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
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
import javax.imageio.ImageIO;
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

    List<File> selectedFiles;
   public static List<Document> filesToAdd = new ArrayList<Document>();

    private GridPane gridPane;

    @FXML
    private VBox newFileBox;

    @FXML
    private ScrollPane scrollPaneStartPage;
    
    @FXML
    private Button exportButton;
    @FXML
    private Label nameLabel, typeLabel, sizeLabel, dateLabel;


    public static ObservableList<Document> oList = FXCollections.observableArrayList();
    private String name = null;
    private String type = null;

    @FXML
    private void addFileButtonAction(ActionEvent event) throws IOException {
        filesAdded = false;
        oList.clear();
        filesToAdd.clear();

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

                filesToAdd.add(files);

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        if (selectedFiles != null) {
            addTag();
        }
        if (filesAdded) {

            for (Document doc : filesToAdd) {
                
                ClassLoader classLoader = getClass().getClassLoader();
                final String dir = System.getProperty("user.dir");
                File source = new File(doc.getPath());
                File dest = new File(dir + "/src/savedFiles/" + doc.getName() + "." + doc.getType());
                copyFileUsingJava7Files(source, dest);
                oList.add(doc);
            }
            
            displayChosenFiles();

        }

    }
    
    @FXML
    private void exportFileButtonAction() throws IOException {
        final String dir = System.getProperty("user.dir");
        
        
            File dest = new File(dir + "/src/savedFiles/" + name + "." + type);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save file");
            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            File newFile = fileChooser.showSaveDialog(null);
        
            if(dest != null) {
               copyFileUsingJava7Files(dest, newFile.getAbsoluteFile());
               System.out.println(dest);
             }

          
        
    }
    
    private void saveFileInfo(String fileName, String fileType){
        name = fileName;
        type = fileType;
    }
    private void displayChosenFiles() {
        
        gridPane.getChildren().removeAll(gridPane.getChildren());
        gridPane.add(newFileBox, 0, 0);
        int columnCounter = 1;
        int rowCounter = 0;
        ArrayList<Document> files = DBConnection.selectFromFiles();
        oList = FXCollections.observableArrayList(files);
        for (Document file : oList) {
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.TOP_CENTER);
            Label fileName = new Label(file.getName() + "." + file.getType());
            fileName.setAlignment(Pos.CENTER);
            fileName.setTextAlignment(TextAlignment.CENTER);
            fileName.setWrapText(true);
            
            ImageView fileImg = new ImageView();
            fileImg.setImage(new Image("/fxml/fileImage.png"));
            fileImg.setFitHeight(78);
            fileImg.setFitWidth(63);
            
            vBox.getChildren().addAll(fileImg, fileName);
            showInfo(vBox, file);
            fileName.setMaxWidth(120);
            if (columnCounter < 4) {
                gridPane.add(vBox, columnCounter, rowCounter);
            } else {
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
    
    private void showInfo(VBox box, Document file) {
     box.setOnMouseClicked((event) -> {
                nameLabel.setText(file.getName());
                nameLabel.setWrapText(true);
                typeLabel.setText(file.getType()+" "+ "fil");
                sizeLabel.setText(String.valueOf(file.getSize()));
                Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                String fileDate = formatter.format(file.getDate());
                dateLabel.setText(fileDate);
                
               saveFileInfo(file.getName(), file.getType());
            });
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

        ArrayList<Document> files = DBConnection.selectFromFiles();
        oList = FXCollections.observableArrayList(files);
        
        gridPane = new GridPane();
        gridPane.setMinWidth(527);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(newFileBox, 0, 0);
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(25);
        gridPane.getColumnConstraints().addAll(col, col, col, col);
        scrollPaneStartPage.setContent(gridPane);
        
        displayChosenFiles();
    }

    private void copyFileUsingJava7Files(File sourceFile, File destinationFile) throws IOException {
        Files.copy(sourceFile.toPath(), destinationFile.toPath());
    }

}
