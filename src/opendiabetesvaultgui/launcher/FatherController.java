/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.launcher;

import com.sun.javafx.stage.StageHelper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Screen;

/**
 *
 * @author Schwind Laptop
 */
public class FatherController {

    /**
     * The stage of the main window.
     */
    public static final Stage MAIN_STAGE = new Stage();

    /**
     * Java Preference for saved values.
     */
    public static final Preferences PREFS_FOR_ALL
            = Preferences.userNodeForPackage(OptionsWindowController.class);
    /**
     * Path to the resources of this application.
     */
    public static final String RESOURCE_PATH
             = "opendiabetesvaultgui/languages/Languages";
    /**
     * Path to the main page: MainWindow.fxml.
     */
    public static final String MAIN_PAGE
            = "fxmlfiles/MainWindow.fxml";
    /**
     * Path to the help page: HelpPage.fxml.
     */
    public static final String MAIN_HELP_PAGE
            = "fxmlfiles/HelpPage.fxml";
    /**
     * Path to the option page: OptionWindow.fxml.
     */
    public static final String MAIN_OPTIONS_WINDOW
            = "fxmlfiles/OptionsWindow.fxml";
    /**
     * Key for java preferences for the language label (ISO 639 language code).
     */
    public static final String LANGUAGE_DISPLAY = "languageLabel";
    /**
     * Key for java preferences for the language name.
     */
    public static final String LANGUAGE_NAME = "currentLanguage";
    /**
     * will be removed.
     */
    public static final String REMOVE_PAGE
            = "fxmlfiles/RemoveWindow.fxml";
    /**
     * Path to the edit page: EditWindow.fxml.
     */
    public static final String EDIT_PAGE
            = "fxmlfiles/EditWindow.fxml";
    /**
     * Path to the import page: Imports.fmxl.
     */
    public static final String IMPORT_PAGE
            = "fxmlfiles/Imports.fxml";
    /**
     * Path to the import plugin control page: PluginControl.fmxl.
     */
    public static final String IMPORT_PLUGIN_CONTROL_PAGE
            = "fxmlfiles/PluginControl.fxml";
    /**
     * Path to the import plugin help page: PluginHelp.fmxl.
     */
    public static final String IMPORT_PLUGIN_HELP_PAGE
            = "fxmlfiles/PluginHelp.fxml";
    /**
     * Path to the export page: Exports.fxml.
     */
    public static final String EXPORT_PAGE
            = "fxmlfiles/Exports.fxml";
    /**
     * Path to the patient selection page: PatientSelection.fxml.
     */
    public static final String PATIENT_SELECTION_PAGE
            = "fxmlfiles/PatientSelection.fxml";

    /**
     * Path to the process page: Process.fxml.
     */
    public static final String PROCESS_PAGE
            = "fxmlfiles/Process.fxml";
    /**
     * Path to the login page: Login.fxml.
     */
    public static final String LOGIN_PAGE
            = "fxmlfiles/Login.fxml";
    /**
     * Path to the database path chooser page: databasePathChooser.fxml.
     */
    public static final String DATABASE_PATH_CHOOSER
            = "fxmlfiles/databasePathChooser.fxml";
    /**
     * Path to the password input page: InputPassword.fxml.
     */
    public static final String PASSWORD_INPUT_PAGE
            = "fxmlfiles/InputPassword.fxml";
    /**
     * Path to the slice page: Slice.fxml.
     */
    public static final String SLICE_PAGE
            = "fxmlfiles/Slice.fxml";
    /**
     * Path to the about page: AboutPage.fxml.
     */
    public static final String ABOUT_PAGE
            = "fxmlfiles/AboutPage.fxml";
    /**
     * Path to the OpenDiabetesVault logo: logo_nude.png.
     */
    public static final String ICON
            = "./src/opendiabetesvaultgui/shapes/logo_nude.png";

    /**
     * HashMap which saves the name of the language as key and the label as
     * value. New languages must be added here.
     */
    public static final Map<String, String> ALL_LANGUAGES
            = new HashMap<String, String>() {
        {
            put("Deutsch", "de");
            put("English", "");
        }
    };
    /**
     * HashMap which saves the label of the language as key and the name as
     * value. New languages must be added here.
     */

    public static final Map<String, String> DATE_FORMATS
            = new HashMap<String, String>() {
                {
                    put("dd.MM.yyyy", "de");
                    put("yyyy-MM-dd", "");
                }
            };

    /**
     * HashMap which saves the name of the language as value and the label as
     * key. New languages must be added here.
     */
    public static final Map<String, String> FOR_DEFAULT_LANGUAGE
            = new HashMap<String, String>() {
        {
            put("de", "Deutsch");
            put("", "English");
        }
    };

    /**
     * The controller of the mainPage.
     */
    private static MainWindowController mainController;

    /**
     * Return the controller of the MainWindow.
     *
     * @return mainController
     */
    public static MainWindowController getMainWindowController() {
        return mainController;
    }

    /**
     * Sets mainController.
     *
     * @param controller the passed controller
     */
    public static void setMainWindowController(
            final MainWindowController controller) {
        FatherController.mainController = controller;
    }
    /**
     * The curren stage openend with openPageAndCloseSameWindows.
     */
    private static Stage windowStage;

    /**
     * Sets windowStage.
     *
     * @param windowStageValue the passed stage
     */
    public static void setWindowStage(final Stage windowStageValue) {
        FatherController.windowStage = windowStageValue;
    }

    /**
     * The curren stage openend with openPageAndCloseSameWindows.
     *
     * @return windowStage
     */
    public static Stage getWindowStage() {
        return windowStage;
    }

    /**
     * The bounds of the used screen.
     */
    public static final Rectangle2D SCREEN_BOUNDS
            = Screen.getPrimary().getVisualBounds();

    /**
     * Returns the MAIN_STAGE of the application.
     *
     * @return the MAIN_STAGE
     */
    public static Stage getPrimaryStage() {
        return FatherController.MAIN_STAGE;
    }

    /**
     * Create a URL from a string.
     *
     * @param path the string which will be convertsed to a URL
     * @return the url of a path
     * @throws java.net.MalformedURLException if path could not be parsed.
     *
     */
    public final URL createURL(final String path)
            throws MalformedURLException {

        URL url = Paths.get(path).toUri().toURL();
        return url;
    }

    /**
     * Opens a page The modality of this window is WINDOW_MOADl and
     * the owner is the MainStage.
     *
     * @param path the specific path of a fmxl file
     * @param name the name of the page
     * @param resizable indicates if the page should be resizable
     * @param bundle the used ResourceBundle for localization
     *
     * @throws java.io.IOException if fxml file or ResourceBundle wasnt found.
     * @throws java.net.URISyntaxException
     */
    public final void openPage(final String path,
            final String name, final Boolean resizable,
            final ResourceBundle bundle) throws IOException, URISyntaxException {

        Class fc = getFatherControllerClass();
        URL url = fc.getResource(path).toURI().toURL();
        FXMLLoader loader = new FXMLLoader(url,
                bundle);

        
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.getIcons().add(new Image(createURL(ICON).toString()));
        Scene scene = new Scene(root);
        stage.setTitle(name);
        stage.setScene(scene);
        stage.setResizable(resizable);
        setWindowStage(stage);
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(MAIN_STAGE);
        stage.show();
    }

    /**
     * Opens a fxml file. Each Page can only be active once at the
     * same time. and is identified by its name.
     *
     * @param path the specific path of a fmxl file
     * @param name the name of the page
     * @param resizable indicates if the page should be resizable
     * @param bundle the used ResourceBundle for localization
     * @throws java.io.IOException if fxml file or ResourceBundle wasnt found.
     */
    public final void openPageAndCloseSameWindows(final URL path,
            final String name, final Boolean resizable,
            final ResourceBundle bundle) throws IOException {
        ObservableList<Stage> stagesList
                = StageHelper.getStages().filtered((Stage t)
                        -> t.getTitle().equals(name));

        if (!stagesList.isEmpty()) {
            stagesList.get(0).toFront();
        } else {
            FXMLLoader loader = new FXMLLoader(path,
                    bundle);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setResizable(resizable);
            Scene scene = new Scene(root);
            stage.setTitle(name);
            stage.setScene(scene);
            stage.show();
            if (path.equals(createURL(MAIN_HELP_PAGE))) {
                HelpController controller
                        = (HelpController) loader.getController();
                stage.setOnCloseRequest(e -> {
                    controller.getWebview().getEngine().load(null);
                });
            }
        }
    }

    /**
     * Deletes all children of the father_pane and inserts a pane
     * into the father_pane. The inserted pane is binded to the father_pane.
     *
     * @param path the specific path of a fmxl file
     * @param fatherPane the pane in which the new pane will be inserted.
     * @param bundle the used ResourceBundle for localization
     * @throws java.io.IOException if fxml file or ResourceBundle wasnt found.
     * @throws java.net.URISyntaxException
     *
     */
    public final void changePane(final String path,
            final Pane fatherPane,
            final ResourceBundle bundle) throws IOException, URISyntaxException {

        Class fc = getFatherControllerClass();
        URL url = fc.getResource(path).toURI().toURL();
        FXMLLoader loader = new FXMLLoader(url,
                bundle);
        Pane newPane = loader.load();
        fatherPane.getChildren().clear();
        fatherPane.getChildren().add(newPane);
        AnchorPane.setBottomAnchor(newPane, 0.0);
        AnchorPane.setLeftAnchor(newPane, 0.0);
        AnchorPane.setRightAnchor(newPane, 0.0);
        AnchorPane.setTopAnchor(newPane, 0.0);
    }
    
    public static Class getFatherControllerClass(){
        FatherController fathercontroller = new FatherController();
        return fathercontroller.getClass();
    }
}
