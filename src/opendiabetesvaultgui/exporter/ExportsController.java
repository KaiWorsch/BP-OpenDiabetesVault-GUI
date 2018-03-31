/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.exporter;

import de.opendiabetes.vault.plugin.exporter.Exporter;
import de.opendiabetes.vault.plugin.management.OpenDiabetesPluginManager;
import de.opendiabetes.vault.plugin.util.HelpLanguage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import opendiabetesvaultgui.importer.ImportsController;
import javax.swing.filechooser.FileSystemView;
import opendiabetesvaultgui.importer.PluginHelpController;
import opendiabetesvaultgui.launcher.FatherController;
import static opendiabetesvaultgui.launcher.FatherController.PLUGIN_CONTROL_PAGE;
import static opendiabetesvaultgui.launcher.FatherController.PLUGIN_HELP_PAGE;

/**
 * FXML Controller class
 *
 * @author Daniel Schäfer, Julian Schwind, Martin Steil, Kai Worsch
 */
public class ExportsController extends FatherController implements Initializable {

    /**
     * Display the export plugins as TitledPane.
     */
    @FXML
    private VBox exportDisplay;
    private OpenDiabetesPluginManager pluginManager;

    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;

    /**
     * The passed ResourceBundle.
     */
    private ResourceBundle myResource;

    private final Preferences prefs = Preferences.userNodeForPackage(ImportsController.class);
    double importBoxPositionY = 10;

    double interpreterBoxPositiony = 10;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myResource = resources;
        endDate.setValue(LocalDate.now());
        startDate.setValue(endDate.getValue().withDayOfYear(LocalDate.now().
                getDayOfYear() - 31));
        exportDisplay.setTranslateX(0);
        exportDisplay.setTranslateY(0);

        pluginManager = OpenDiabetesPluginManager.getInstance();

        List<String> exportPlugins = pluginManager.
                getPluginIDsOfType(Exporter.class);

        for (int i = 0; i < exportPlugins.size(); i++) {
            Exporter plugin = pluginManager.
                    getPluginFromString(Exporter.class, exportPlugins.
                            get(i));
            try {
                exportDisplay.getChildren().add(createExporter(exportPlugins.
                        get(i), plugin));
            } catch (Exception ex) {
                Logger.getLogger(ExportsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Creates a Exporte plugin as TitledPane. All necessary elements for the
     * plugin will be created, designed and positioned. Methods will be assigned
     * to specific elements.
     *
     * @param name the name of the plugin
     * @param plugin the passed plugin
     * @return the TitledPane
     * @throws Exception if the export couldnt be started or finished
     */
    private TitledPane createExporter(String name, Exporter plugin) throws Exception {

        AnchorPane content = new AnchorPane();
        Path helpPath = pluginManager.getHelpFilePath(plugin, HelpLanguage.LANG_EN);

        //Path helpPath = pluginManager.getHelpFilePath(plugin);
        Button exportButton = new Button(myResource.getString("export.exportButton"));
        exportButton.setPrefWidth(100);

        exportButton.setOnAction(e -> {

        });

        ProgressBar progress = new ProgressBar();
        progress.setPrefWidth(300);
        progress.setPrefHeight(20);

        SVGPath gear = new SVGPath();
        gear.setContent("M19.43 12.98c.04-.32.07-.64.07-.98s-.03-.66-.07-.98l2.11-1.65c.19-.15.24-.42.12-.64l-2-3.46c-.12-.22-.39-.3-.61-.22l-2.49 1c-.52-.4-1.08-.73-1.69-.98l-.38-2.65C14.46 2.18 14.25 2 14 2h-4c-.25 0-.46.18-.49.42l-.38 2.65c-.61.25-1.17.59-1.69.98l-2.49-1c-.23-.09-.49 0-.61.22l-2 3.46c-.13.22-.07.49.12.64l2.11 1.65c-.04.32-.07.65-.07.98s.03.66.07.98l-2.11 1.65c-.19.15-.24.42-.12.64l2 3.46c.12.22.39.3.61.22l2.49-1c.52.4 1.08.73 1.69.98l.38 2.65c.03.24.24.42.49.42h4c.25 0 .46-.18.49-.42l.38-2.65c.61-.25 1.17-.59 1.69-.98l2.49 1c.23.09.49 0 .61-.22l2-3.46c.12-.22.07-.49-.12-.64l-2.11-1.65zM12 15.5c-1.93 0-3.5-1.57-3.5-3.5s1.57-3.5 3.5-3.5 3.5 1.57 3.5 3.5-1.57 3.5-3.5 3.5z");
        gear.setStyle("-fx-fill: #007399");
        Circle hitBoxGear = new Circle(10);
        hitBoxGear.setOpacity(0);
        hitBoxGear.setCursor(Cursor.HAND);

        hitBoxGear.setOnMouseClicked(event -> {

            try {
                openPluginControll(name + " " + myResource.getString("import.pluginControlTitle"));
            } catch (IOException ex) {
                Logger.getLogger(ImportsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        Rectangle hitBoxHepSign = new Rectangle(16, 25);
        hitBoxHepSign.setCursor(Cursor.HAND);
        hitBoxHepSign.setOpacity(0);
        hitBoxHepSign.setOnMouseClicked((MouseEvent event) -> {
            try {
                openPluginHelp(helpPath.toString(), name + "  " + myResource.getString("import.pluginHelpTitle"));
            } catch (IOException ex) {
                Logger.getLogger(ImportsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        SVGPath helpSign = new SVGPath();
        helpSign.setContent("M11 18h2v-2h-2v2zm1-16C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12"
                + " 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm0-14c-2.21 0-4 1.79-4 "
                + "4h2c0-1.1.9-2 2-2s2 .9 2 2c0 2-3 1.75-3 5h2c0-2.25 3-2.5 3-5 0-2.21-1.79-4-4-4z");
        helpSign.setStyle("-fx-fill: #007399;");
        List<Node> list = new ArrayList<>(
                Arrays.asList(exportButton, gear, progress,
                        helpSign, hitBoxGear, hitBoxHepSign));

        //    setPositionInterpreter(importButton, progress, gear, helpSign);
        exportButton.relocate(18, 80);
        progress.relocate(160, 88);
        hitBoxGear.relocate(540, 11);
        hitBoxHepSign.relocate(580, 10);
        helpSign.relocate(hitBoxHepSign.getLayoutX(), hitBoxHepSign.getLayoutY());
        gear.relocate(hitBoxGear.getLayoutX() - 11, hitBoxGear.getLayoutY() - 11);
        Button browseButton = new Button(myResource.getString("import.browseButton"));
        TextField field = new TextField();

        browseButton.setOnAction((ActionEvent action) -> {
            chooseFiles(action, field);
        });

        browseButton.relocate(510, 40);
        field.relocate(20, 40);
        field.setPrefWidth(465);
        field.setPrefHeight(25);
        field.setId("importPfad");
        field.setText(FileSystemView.getFileSystemView().getDefaultDirectory().getPath());
        browseButton.setPrefWidth(120);

        Rectangle fieldBox = new Rectangle();
        fieldBox.setWidth(471);
        fieldBox.setHeight(49);
        fieldBox.setSmooth(true);
        fieldBox.setId("fieldBox");

        Rectangle fieldRectangle = new Rectangle();
        fieldRectangle.setWidth(471);
        fieldRectangle.setHeight(1.5);
        fieldRectangle.setSmooth(true);
        fieldRectangle.setId("fieldRectangle");

        Label fieldLabel = new Label(myResource.getString("import.fieldLabel"));
        fieldLabel.setId("fieldLabel");

        list.add(fieldBox);
        list.add(fieldLabel);
        list.add(fieldRectangle);
        list.add(field);
        list.add(browseButton);

        TitledPane pane = new TitledPane("", content);

        content.getChildren().addAll(list);
        BorderPane bPane = new BorderPane();
        // hbox.setPadding(new Insets(15, 12, 15, 12));
        CheckBox disBox = new CheckBox("");
        Label title = new Label(name);
        progress.setDisable(true);

        pane.graphicTextGapProperty().bind(pane.widthProperty());
        pane.setGraphic(createBorderPane(name, pane, progress));
        //  pane.setContentDisplay(ContentDisplay.RIGHT);
        //  pane.graphicTextGapProperty().bind(pane.widthProperty().subtract(100));

        pane.setExpanded(false);

        if (prefs.getBoolean(name, false)) {
            //   pane.setDisable(true);
            pane.setCollapsible(true);
            pane.setCollapsible(false);
            pane.setExpanded(false);

        } else {
            disBox.selectedProperty().set(true);
        }

        checkBoxListener(disBox, pane, name, bPane);

        pane.setAnimated(false);

        return pane;
    }

    /**
     * Creates a Borderpane consisting of a Checkbox and ProgressBar. The
     * element will be positioned using methods of the BorderPane. Calls
     * {@link #checkBoxListener(CheckBox box, TitledPane target, String name, BorderPane bp)}
     *
     * @see #getFileString(Stage stage)
     *
     * @param name the title of a TitledPane
     * @param pane the respective a TitledPane
     * @param progress a Progressbar
     * @return the BorderPane
     */
    private BorderPane createBorderPane(String name, TitledPane pane, ProgressBar progress) {
        BorderPane bPane = new BorderPane();
        CheckBox cBox = new CheckBox("");
        if (prefs.getBoolean(name, false)) {
            pane.setCollapsible(true);
            pane.setCollapsible(false);
            pane.setExpanded(false);
        } else {
            cBox.selectedProperty().set(true);
        }
        Label title = new Label(name);
        progress.setProgress(0);
        progress.setPrefWidth(300);
        progress.setPrefHeight(20);
        bPane.setPadding(new Insets(5, 10, 5, 10));
        bPane.autosize();
        bPane.setLeft(cBox);
        BorderPane centerPane = new BorderPane();
        centerPane.setPadding(new Insets(0, 0, 0, 10));
        centerPane.setLeft(title);
        bPane.setCenter(centerPane);
        bPane.setRight(progress);
        bPane.prefWidthProperty().bind(pane.widthProperty().subtract(200));
        checkBoxListener(cBox, pane, name, bPane);
        return bPane;
    }

    /**
     * Opens page, which displays a help page.
     *
     * @param path path to the help page.
     * @param title title of the window.
     * @throws java.net.MalformedURLException if the path couldnt be parsed as
     * URL
     *
     * @throws java.io.IOException if fxml file or ResourceBundle wasnt found.
     */
    private void openPluginHelp(String path, String title) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(PLUGIN_HELP_PAGE), myResource);
        Parent root = loader.load();

        PluginHelpController pluginHelpController = loader.getController();
        Stage stage = new Stage();
        stage.setTitle(title);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        pluginHelpController.loadHelpPage(path);
        stage.show();

    }

    /**
     * Adds listener to CheckBoxe's selectedProperty(). CheckBox state
     * determines, if the TitledPane is collapsible.
     *
     * @param box the CheckBox from the TitledPane
     * @param target the TitledPane
     * @param name title of the TitledPane
     * @param bp the BorderPane inside of the TitledPane
     */
    private void checkBoxListener(CheckBox box, TitledPane target, String name, BorderPane bp) {
        box.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
                -> {
            target.setExpanded(newValue);
            target.setCollapsible(newValue);
            prefs.putBoolean(name, !newValue);
        });
    }

    /**
     * Opens the control page as a popup.
     *
     * @param name title of the window
     * @throws java.io.IOException if fxml file or ResourceBundle wasnt found.
     */
    private void openPluginControll(String name) throws IOException {
        openPage(PLUGIN_CONTROL_PAGE, name, false, myResource);
    }

    /**
     * Calls #openPluginHelp(String path, String title)
     *
     * @param name title of the opened window
     * @throws java.io.IOException if fxml file or ResourceBundle wasnt found.
     */
    private void openPluginHelp(String name) throws IOException {
        openPageSingleInstance(getClass().getResource(PLUGIN_HELP_PAGE).toExternalForm(), name, false, myResource);
    }

    /**
     * Saves the path of the chosen file inside a textfield and is assigend to
     * the #exportButton.
     *
     * @see #getFileString(Stage stage)
     *
     * @param event calls method when triggered.
     * @param test the textfield in which the path is saved
     *
     */
    @FXML
    private void chooseFiles(ActionEvent event, TextField test) {
        Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
        test.setText(getFileString(stage));

    }

    /**
     * Opens a fileChooser. Multiple data paths will concatenated to one String
     * seperated by ;.
     *
     * @param stage stage which displays the filechooser
     * @return The path of the chosen file/files
     */
    private String getFileString(Stage stage) {

        FileChooser fileChooser = new FileChooser();

        List<File> files = fileChooser.showOpenMultipleDialog(stage);

        if (files != null && !files.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            files.forEach((item) -> {
                sb.append(item.getAbsolutePath()).append(";");
            });

            return sb.toString().substring(0, sb.length() - 1);

        }

        return "";
    }

}
