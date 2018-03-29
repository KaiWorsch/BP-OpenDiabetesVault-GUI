/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.launcher;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.swing.filechooser.FileSystemView;
import static opendiabetesvaultgui.launcher.FatherController.LOGIN_PAGE;
import opendiabetesvaultgui.login.InputPasswordController;

/**
 *
 * @author Daniel Schäfer, Julian Schwind, Martin Steil, Kai Worsch
 *
 */
public class Launch extends Application {

    /**
     * Java preferences.
     */
    private final Preferences prefs = Preferences.
            userNodeForPackage(InputPasswordController.class);

    @Override
    public final void start(final Stage stage) throws MalformedURLException,
            BackingStoreException, IOException, URISyntaxException {

        stage.setTitle("OpenDiabetesVault - Please Log in");
        stage.setResizable(false);
        //stage.initStyle(StageStyle.UTILITY);
        stage.setAlwaysOnTop(true);

        File file = new File(FileSystemView.getFileSystemView().
                getDefaultDirectory().getPath() + File.separator
                + "ODV" + File.separator + "database");
        if (!file.exists()) {
            if (file.mkdirs()) {
                System.out.println("Directory created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
        Parent root;
        // if a password was set
                    Class fc = FatherController.getFatherControllerClass();

        if ((Arrays.toString(prefs.keys())).contains("properties")) {
            String path = LOGIN_PAGE;
            URL url = fc.getResource(path).toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url,
                    ResourceBundle.getBundle(FatherController.RESOURCE_PATH,
                            new Locale(FatherController.PREFS_FOR_ALL.
                                    get(FatherController.
                                            LANGUAGE_DISPLAY, ""))));
            root = loader.load();
            // the application was started the first time
        } else {
            URL url = fc.getResource(FatherController.PASSWORD_INPUT_PAGE).toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url,
                    ResourceBundle.getBundle(FatherController.
                            RESOURCE_PATH, new Locale(
                            FatherController.PREFS_FOR_ALL.get(FatherController.
                                    LANGUAGE_DISPLAY, java.util.Locale.
                                            getDefault().toString()))));
            root = loader.load();

        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image(
                Paths.get(FatherController.ICON).toUri().toURL().toString()));
        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        launch(args);
    }

}
