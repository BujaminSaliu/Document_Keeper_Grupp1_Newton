/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static controller.TagPopUpController.filesAdded;
import documentkeeper.DesktopApi;
import java.awt.Component;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
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
    private Button addFileButton, buttonLink;

    @FXML
    private Label infoLabel, fileTypeLabel, fileSizeLabel, fileDateLabel, fileTagLabel,linkedFilesInfoLabel, cleanSearchLabel;

    List<File> selectedFiles;
    public static List<Document> filesToAdd = new ArrayList<Document>();

    private GridPane gridPane;

    @FXML
    private GridPane tagBox;

    @FXML
    private VBox newFileBox;
    @FXML
    private HBox nameHBox;

    @FXML
    private ScrollPane scrollPaneStartPage;

    @FXML
    private Button exportButton, linkedButton;

    @FXML
    private TextField searchBox;
    @FXML
    private Label nameLabel, typeLabel, sizeLabel, dateLabel, tagLabel;
    @FXML
    private ListView<String> listToLink;

    public static ObservableList<Document> oList = FXCollections.observableArrayList();
    public static ObservableList<String> linkedFilesObs = FXCollections.observableArrayList();

    private String name = null;
    private String type = null;

    String key = "This is a secret";

    private ArrayList<VBox> selectedBoxes = new ArrayList<VBox>();
    private ArrayList<Document> filesToLink = new ArrayList<>();

    @FXML
    private void linkFiles(Document fileA) {
        ArrayList<Document> files = DBConnection.selectFromFiles();
        oList = FXCollections.observableArrayList(files);
        //listToLink.setItems(oList);
        buttonLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                pushTheButton(fileA);
            }
        });
    }

    @FXML
    private void pushTheButton(Document fileA) {
        // int fileB = listToLink.getSelectionModel().getSelectedItem().getId();
        // System.out.println(" fileA.id " + fileA.getId() + " fileB.id " + fileB);
        // DBConnection.linkFiles(fileA.getId(), fileB);
    }

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
            Task<Void> longRunningTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    for (Document doc : filesToAdd) {

                        ClassLoader classLoader = getClass().getClassLoader();
                        final String dir = System.getProperty("user.dir");

                        File source = new File(doc.getPath());
                        File dest = new File(dir + "/src/savedFiles/" + doc.getName() + "." + doc.getType());

                        oList.add(doc);
                        //Here we encrypt original the file  
                        //creating a path to the encrypted file
                        File encryptedFile = new File(dir + "/src/savedFiles/" + doc.getName() + ".encrypted");

                        try {

                            //Call to encryption method file processor whitch uses "AES" algorithm
                            fileProcessor(Cipher.ENCRYPT_MODE, key, source, encryptedFile);

                            //Delete the original one
                            //source.delete();

                            //just to check if the encryption done!
                            System.out.println("Encrypted Successfully!");
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                        oList.add(doc);

                        if (filesToAdd.size() == 1) {
                            Platform.runLater(() -> infoLabel.setText("Filen lades till!"));

                        } else {
                            Platform.runLater(() -> infoLabel.setText("Filerna lades till!"));
                        }

                    }
                    Platform.runLater(() -> displayChosenFiles());
                    return null;
                }
            };
            infoLabel.setText("Filer läggs till..vänligen vänta...");
            new Thread(longRunningTask).start();

        }
    }

    @FXML
    private void linkFiles(ActionEvent event) throws IOException {
        for (int i = 1; i < filesToLink.size(); i++) {
            DBConnection.linkFiles(filesToLink.get(0).getId(), filesToLink.get(i).getId());
        }
        updateLinkedFilesList(filesToLink.get(0).getId());
    }

    @FXML
    private void exportFileButtonAction() {
        try {
            final String dir = System.getProperty("user.dir");

            File encryptedFile = new File(dir + "/src/savedFiles/" + name + ".encrypted");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export file");
            fileChooser.setInitialFileName(name + "." + type);

            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(type.toUpperCase(), "*." + type);
            fileChooser.getExtensionFilters().add(extFilter);

            File newFile = fileChooser.showSaveDialog(null);

            File decryptedFile = new File(newFile.getAbsolutePath());
            System.out.println(newFile.getAbsolutePath());

            fileProcessor(Cipher.DECRYPT_MODE, key, encryptedFile, decryptedFile);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void saveFileInfo(String fileName, String fileType) {
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
            tagBox.getChildren().clear();
            linkedFilesObs.clear();
            if (!mouseEvent.isControlDown()) {
                filesToLink.clear();
                for (VBox vbox : selectedBoxes) {
                    vbox.setStyle("fx-background-color: none");
                }
                selectedBoxes.clear();
                box.setStyle("-fx-background-color: #e2e2e2");
                selectedBoxes.add(box);
                filesToLink.add(file);
                updateLinkedFilesList(filesToLink.get(0).getId());

            } else if (selectedBoxes.contains(box)) {
                selectedBoxes.remove(box);
                box.setStyle("fx-background-color: none");
            } else {
                box.setStyle("-fx-background-color: #e2e2e2");
                selectedBoxes.add(box);
                filesToLink.add(file);
            }

            fileTypeLabel.setVisible(true);
            fileSizeLabel.setVisible(true);
            fileDateLabel.setVisible(true);
            linkedFilesInfoLabel.setVisible(true);
            exportButton.setVisible(true);
            listToLink.setVisible(true);
            linkedButton.setVisible(true);
            fileTagLabel.setVisible(true);
            nameHBox.setVisible(true);
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    ClassLoader classLoader = getClass().getClassLoader();

                    final String dir = System.getProperty("user.dir");
                    File encryptedFile = new File(dir + "/src/savedFiles/" + file.getName() + ".encrypted");
                    File decryptedFile = new File(dir + "/src/tempFiles/" + file.getName() + "." + file.getType());

                    try {
                        fileProcessor(Cipher.DECRYPT_MODE, key, encryptedFile, decryptedFile);
                    } catch (InvalidKeyException ex) {
                        Logger.getLogger(FolderContentPageController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    DesktopApi.open(decryptedFile);

                }
                //linkFiles(file);

                nameLabel.setText(filesToLink.get(0).getName());
                nameLabel.setWrapText(true);
                typeLabel.setText(filesToLink.get(0).getType() + " " + "fil");
                if (filesToLink.get(0).getSize() > 1000000) {
                    int fileSize = (filesToLink.get(0).getSize() / 1000) / 1000;
                    //double roundedFileSize = Math.round(fileSize*100.0)/100.0;
                    sizeLabel.setText(String.valueOf(fileSize) + " MB");
                } else {
                    sizeLabel.setText(String.valueOf(filesToLink.get(0).getSize() / 1000) + " KB");
                }
                Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                String fileDate = formatter.format(filesToLink.get(0).getDate());
                dateLabel.setText(fileDate);
                if (!filesToLink.get(0).getTags().isEmpty()) {
                    tagBox.getChildren().removeAll(tagBox.getChildren());
                    tagBox.getRowConstraints().clear();
                    int columnCounter = 0;
                    int rowCounter = 0;
                    for (Tag tag : filesToLink.get(0).getTags()) {
                        Label newLabel = new Label();
                        newLabel.setText(tag.toString());
                        newLabel.setCursor(Cursor.HAND);
                        newLabel.setFont(Font.font("Arial", 12));
                        newLabel.setMinHeight(25);
                         newLabel.setStyle("-fx-background-color:#c1c1c1; -fx-border-width:3; -fx-border-color: #e2e2e2");
                        newLabel.hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {
                            if (show) {
                                newLabel.setStyle("-fx-background-color:#fffc51; -fx-border-width:3; -fx-border-color: #e2e2e2");
                                newLabel.setTextFill(Color.CORNFLOWERBLUE);
                            } else {
                                newLabel.setStyle("-fx-background-color:#c1c1c1; -fx-border-width:3; -fx-border-color: #e2e2e2");
                                newLabel.setTextFill(Color.BLACK);
                               
                            }
                        });
                        newLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                System.out.println(newLabel.getText().substring(1));
                                searchBox.setText(newLabel.getText().substring(1));
                                //searchBox.getEventDispatcher();
                                searchBox.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER, true, true, true, true));
                            }
                        });

                        if (columnCounter < 2) {
                            tagBox.add(newLabel, columnCounter, rowCounter);
                        } else {
                            rowCounter++;
                            columnCounter = 0;
                            tagBox.add(newLabel, columnCounter, rowCounter);
                            RowConstraints row = new RowConstraints();
                            row.setMinHeight(15);
                            tagBox.getRowConstraints().add(row);
                        }
                        columnCounter++;
                    }

                    //fileTagLabel.setVisible(true);
                    //tagLabel.setText("" + file.getTags());
                } else {
                    fileTagLabel.setVisible(false);
                    tagLabel.setText("");
                }

                saveFileInfo(filesToLink.get(0).getName(), filesToLink.get(0).getType());

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

        ArrayList<Document> files = DBConnection.search(searchBox.getText().toLowerCase() + event.getText().toLowerCase());
        if (searchBox.getText().equals("")) {
            files = DBConnection.selectFromFiles();
        }
        for (Document doc : files) {
            oList.add(doc);
        }

        gridPane.getChildren().removeAll(gridPane.getChildren());
        gridPane.getRowConstraints().clear();
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
        listToLink.setItems(linkedFilesObs);
        gridPane = new GridPane();
        gridPane.setMinWidth(527);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(newFileBox, 0, 0);
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(25);
        gridPane.getColumnConstraints().addAll(col, col, col, col);
        scrollPaneStartPage.setContent(gridPane);

        displayChosenFiles();
        cleanSearchLabel.hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {
                            if (show) {
                                //cleanSearchLabel.setStyle("-fx-background-color:#fffc51; -fx-border-width:3; -fx-border-color: #e2e2e2");
                                cleanSearchLabel.setTextFill(Color.CADETBLUE);
                            } else {
                                //cleanSearchLabel.setStyle("-fx-background-color:#c1c1c1; -fx-border-width:3; -fx-border-color: #e2e2e2");
                                cleanSearchLabel.setTextFill(Color.BLACK);
                               
                            }
                        });
                        cleanSearchLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                displayChosenFiles();
                                searchBox.setText("");
                            }
                        });

    }

    static void fileProcessor(int cipherMode, String key, File inputFile, File outputFile) throws InvalidKeyException {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | BadPaddingException
                | IllegalBlockSizeException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateLinkedFilesList(int a) {
        ArrayList<Document> files = DBConnection.getLinkedFiles(a);
        for (Document doc : files) {
            if (doc.getId() != filesToLink.get(0).getId()) {
                linkedFilesObs.add(doc.getName());
            }

        }
        //linkedFilesObs.addAll(files);
    }
}
