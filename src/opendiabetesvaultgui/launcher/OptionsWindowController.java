/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.launcher;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import static opendiabetesvaultgui.launcher.FatherController.ICON;
import static opendiabetesvaultgui.launcher.FatherController.LANGUAGE_DISPLAY;
import static opendiabetesvaultgui.launcher.FatherController.LANGUAGE_NAME;
import static opendiabetesvaultgui.launcher.FatherController.PREFS_FOR_ALL;

/**
 * FXML Controller class
 *
 * @author Daniel Schäfer, Martin Steil, Julian Schwind, Kai Worsch
 */
public class OptionsWindowController extends FatherController implements Initializable {

    @FXML
    private ComboBox selectLanguage;
    @FXML
    private AnchorPane fatherPane;
    @FXML
    private Button applyButton;
    @FXML
    private Button cancelButton;

    @FXML
    private TextField dataBasePath;

    private String currentLanguage;
    private String currentDateFormat;
    private String path;
    private ResourceBundle myResource;

    @FXML
    private TabPane tabs;

    @FXML
    private Tab database;
    @FXML
    private Tab languages;
    @FXML
    private CheckBox isInMemoryCheckBox;
    @FXML
    private ComboBox selectDateFormat;

    private Boolean newLanguage = false;
    private Boolean newPath = false;
    private Boolean newType = false;
    private Boolean newDateFormat = false;

    private String currentPath;

    /**
     * Initializes the controller class.
     *
     * @param location the url of OptionsWindow.fxml
     * @param resources the used resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        myResource = resources;
        // sets the current database path
        currentPath = PREFS_FOR_ALL.get("pathDatabase", "");
        isInMemoryCheckBox.selectedProperty().set(PREFS_FOR_ALL.getBoolean("databaseInMemory", false));

        dataBasePath.setText(currentPath);
        // sets the current supported languages
        ALL_LANGUAGES.keySet().forEach((String e) -> {

            selectLanguage.getItems().add(e);
        });
        
        DATE_FORMATS.keySet().forEach((String e) -> {
            selectDateFormat.getItems().add(e);
        });

        // if the saved language isnt supported       
        // only relevant after adding a new language
        currentLanguage = PREFS_FOR_ALL.get(LANGUAGE_NAME, "English");
        currentDateFormat = PREFS_FOR_ALL.get("dateFormat", "yyyy-MM-dd");

        selectLanguage.setValue(currentLanguage);
        selectDateFormat.setValue(currentDateFormat);
        languageListener();
        checkBoxListener();
        dateFormatListener();

    }

    private void checkBoxListener() {
        isInMemoryCheckBox.selectedProperty().addListener((ObservableValue<? extends Boolean> observable,
                Boolean oldValue, Boolean newValue) -> {

            newType = !newType;
            if(!newLanguage && !newPath && !newDateFormat)
                {
                   applyButton.setDisable(!newType);
                }

        });
    }

    /**
     * This method lets the user choose a new directory for the database. After
     * the user chose a directory, the path will be saved in the java
     * preferences.
     *
     * @param action when triggered call method
     */
    @FXML
    private void closeWindow(ActionEvent action) {
        ((Stage) ((Node) (action.getSource())).getScene().getWindow()).hide();
    }

    /**
     * This method lets the user choose a new directory for the database. After
     * the user chose a directory, the path will be saved in the java
     * preferences.
     *
     * @param action when triggered call method
     */
    @FXML
    private void choosePath(ActionEvent action) {
        Stage stage = (Stage) ((Node) (action.getSource())).getScene().getWindow();

        DirectoryChooser chooser = new DirectoryChooser();
        File selectedDirectory = chooser.showDialog(stage);

        if (selectedDirectory != null) {

            path = selectedDirectory.getAbsolutePath();
            dataBasePath.setText(path);

            if (!path.equals(currentPath)) {
                applyButton.setDisable(false);
                newPath = true;
            } else {
                applyButton.setDisable(!newLanguage && !newType && !newDateFormat);
                newPath = false;
            }
        }
    }

    /**
     * This method changes the used language and is assigned to the applyButton.
     * At first a alert window will open, which asks for the users confirmation.
     * If the users confirms,the java preferences values assigned to
     * LANGUAGE_DISPLAY and LANGUAGE_NAME will be updated and the option window
     * closes.
     *
     * @param event when triggered calls method
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     *
     */
    @FXML
    private void applyChanges(ActionEvent event) throws MalformedURLException, IOException {

        // create and show alert window
        ButtonType declineButton = new ButtonType(myResource.getString("option.alertCancelButton"), ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType okButton = new ButtonType(myResource.getString("option.alertOKButton"), ButtonBar.ButtonData.OK_DONE);

        Alert alert = new Alert(Alert.AlertType.WARNING, "", declineButton, okButton);
        alert.setTitle("OpenDiabetesVault");
        alert.setHeaderText(myResource.getString("option.alertHeader"));

        if (newPath || newType) {
            alert.setContentText(myResource.getString("option.databaseAlertContent"));
        } else {
            alert.setContentText(myResource.getString("option.languageAlertContent"));
        }
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/opendiabetesvaultgui/stylesheets/alertStyle.css").toExternalForm());
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.initOwner((Stage) (((Node) event.getSource())).getScene().getWindow());
        stage.getIcons().add(new Image(createURL(ICON).toString()));
        Optional<ButtonType> result = alert.showAndWait();

        // if user confirms
        if (result.get() == okButton) {
            // saves the new language as ISO-639 language code
            PREFS_FOR_ALL.put(LANGUAGE_DISPLAY, ALL_LANGUAGES.getOrDefault(selectLanguage.getValue(), ""));
            // saves the name of the new language
            PREFS_FOR_ALL.put(LANGUAGE_NAME, (String) selectLanguage.getValue());
            PREFS_FOR_ALL.putBoolean("databaseInMemory", isInMemoryCheckBox.isSelected());

            PREFS_FOR_ALL.put("pathDatabase", (String) dataBasePath.getText());
            
            PREFS_FOR_ALL.put("dateFormat", (String) selectDateFormat.getValue());
            // close window
            if (newPath  || newType) {
                                getPrimaryStage().close();

            } else {
                fatherPane.getScene().getWindow().hide();
            }
        }
    }

    /**
     * This method enables the applyButton when the language was changed.
     */
    private void languageListener() {
        selectLanguage.valueProperty().addListener((ObservableValue observableValue, Object oldValue, Object newValue) -> {

            if (currentLanguage.equals(newValue)) {
                applyButton.setDisable(!newPath && !newType && !newDateFormat);
                newLanguage = false;
            } else {
                applyButton.setDisable(false);
                newLanguage = true;
            }

        });

    }
    
    private void dateFormatListener() {
        selectDateFormat.valueProperty().addListener((ObservableValue observableValue, Object oldValue, Object newValue) -> {

            if (currentDateFormat.equals(newValue)) {
                applyButton.setDisable(!newPath && !newType && !newLanguage);
                newDateFormat = false;
            } else {
                applyButton.setDisable(false);
                newDateFormat = true;
            }

        });

    }
}
