/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static controller.TagPopUpController.filesAdded;
import documentkeeper.DesktopApi;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
    
    @FXML
    private Label infoLabel;

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
    @FXML
    private TextField searchBox;


    public static ObservableList<Document> oList = FXCollections.observableArrayList();

    private String name = null;
    private String type = null;


    
    private VBox selectedBox;
    

    @FXML
    private void addFileButtonAction(ActionEvent event) throws IOException {
        filesAdded = false;
        oList.clear();
        filesToAdd.clear();
        infoLabel.setText("");
        
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
                int fileSizeInBytes = (int) selectedFiles.get(i).length();
                
                String path = selectedFiles.get(i).getAbsolutePath();

                Document files = new Document();
                files.setName(name);
                files.setType(fileType);
                files.setSize(fileSizeInBytes);
                files.setPath(path);
                //tags list hinders nullpointer exception
                List tags = new ArrayList<Tag>();
                files.setTags(tags);

                filesToAdd.add(files);
                
            }
        } catch (Exception e) {
            System.out.println(e);
            infoLabel.setText("Åtgärden avbröts!");
        }
        if (selectedFiles != null) {
            
            addTag();
        }
        if (filesAdded) {
            infoLabel.setText("Filer läggs till..vänligen vänta...");
            for (Document doc : filesToAdd) {
                
                ClassLoader classLoader = getClass().getClassLoader();
                final String dir = System.getProperty("user.dir");
                File source = new File(doc.getPath());
                File dest = new File(dir + "/src/savedFiles/" + doc.getName() + "." + doc.getType());
                
                copyFileUsingJava7Files(source, dest);
                oList.add(doc);
                
                if (filesToAdd.size() == 1) {
                    infoLabel.setText("Filen lades till!");
                }else{
                    infoLabel.setText("Filerna lades till!");
                }
               
            }
            
            displayChosenFiles();
        }
    }
    
    @FXML
    private void exportFileButtonAction() {
        try {
        final String dir = System.getProperty("user.dir");
        
        File dest = new File(dir + "/src/savedFiles/" + name + "." + type);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export file");
        fileChooser.setInitialFileName(name + "." + type);
        
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(type.toUpperCase(), "*." + type);
        fileChooser.getExtensionFilters().add(extFilter);
        
        File newFile = fileChooser.showSaveDialog(null);
        
        if(dest != null) {
            copyFileUsingJava7Files(dest, newFile.getAbsoluteFile());
            System.out.println(dest);
        }
        } catch(Exception e){
            System.out.println(e);
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
     box.setOnMouseClicked((MouseEvent mouseEvent) -> {
         System.out.println(selectedBox);
         if(selectedBox != null){
             selectedBox.setStyle("-fx-background-color: none");
         }
               selectedBox = box; 
               box.setStyle("-fx-background-color: #e2e2e2");
        
         if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                ClassLoader classLoader = getClass().getClassLoader();
                final String dir = System.getProperty("user.dir");
                File dest = new File(dir + "/src/savedFiles/" + file.getName() + "." + file.getType());
                DesktopApi.edit(dest);
            }
            nameLabel.setText(file.getName());
            nameLabel.setWrapText(true);
                typeLabel.setText(file.getType()+" "+ "fil");
                if(file.getSize()> 1000000){
                  int fileSize = (file.getSize()/1000)/1000;
                    //double roundedFileSize = Math.round(fileSize*100.0)/100.0;
                  sizeLabel.setText(String.valueOf(fileSize)+ " MB");  
                }else {
                    sizeLabel.setText(String.valueOf(file.getSize()/1000)+ " KB");
                }
                
                Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                String fileDate = formatter.format(file.getDate());
                dateLabel.setText(fileDate);

                
               saveFileInfo(file.getName(), file.getType());

        }
              
                

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
    
    @FXML
    private void search(KeyEvent event) {
        oList.clear();
        //Method from displayChoosenFiles(). I need everything but from another dbconnection directory
        //
        if(searchBox.getText().equals("")){
            System.out.println("empty");
        }
        ArrayList<Document> files = DBConnection.search(searchBox.getText().toLowerCase() + event.getText().toLowerCase());
        System.out.println(searchBox.getText().toLowerCase() + event.getText().toLowerCase());
        for (Document doc : files) {
            oList.add(doc);
        }
        gridPane.getChildren().removeAll(gridPane.getChildren());
        gridPane.add(newFileBox, 0, 0);
        int columnCounter = 1;
        int rowCounter = 0;
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
