/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.importer;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.plugin.common.OpenDiabetesPlugin.StatusListener;
import de.opendiabetes.vault.plugin.fileimporter.FileImporter;
import de.opendiabetes.vault.plugin.importer.Importer;
import de.opendiabetes.vault.plugin.interpreter.Interpreter;
import de.opendiabetes.vault.plugin.management.OpenDiabetesPluginManager;
import de.opendiabetes.vault.plugin.util.HelpLanguage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.filechooser.FileSystemView;
import opendiabetesvaultgui.launcher.FatherController;
import opendiabetesvaultgui.patientselection.PatientSelectionController;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import opendiabetesvaultgui.launcher.MainWindowController;

/**
 * FXML Controller class.
 *
 * @author kai
 */
public class ImportsController extends FatherController
        implements Initializable {

    private final Preferences prefs = Preferences.
            userNodeForPackage(ImportsController.class);

    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private AnchorPane greatFatherPane;
    @FXML
    private VBox importDisplay;
    @FXML
    private CheckBox importAllCheckBox;
    @FXML
    private Button importAllButton;
    @FXML
    private Accordion accord;

    public String buttonPath = "./src/opendiabetesvaultgui/shapes/test.css";

    private OpenDiabetesPluginManager pluginManager;

    private ResourceBundle myResource;

    private final ObservableList inputList = PatientSelectionController.
            getNameList();
    private String controls;
    private final Boolean pluginCheck[] = new Boolean[4];
    private final int pluginCount = 3;
    //private  PluginHelpController pluginHelpController;

    /**
     * initializes the controller class.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /*
        DATABASE
         */
        Connection c;
//        controls = (String) inputList.get(4);
        /*if (controls == null) {
            for (int i = 0; i < pluginCount; i++) {
                pluginCheck[i] = false;
            }
        }*/
        System.out.println(controls);

        myResource = resources;
        // default value for the datepicker
        endDate.setValue(LocalDate.now());
        startDate.setValue(endDate.getValue().withDayOfYear(LocalDate.now().
                getDayOfYear() - 31));

        pluginManager = OpenDiabetesPluginManager.getInstance();

        // the interpreter and import plugin lists
        List<String> importPlugins = pluginManager.
                getPluginIDsOfType(FileImporter.class);
        List<String> interpreterPlugins = pluginManager.
                getPluginIDsOfType(Interpreter.class);

        for (int i = 0; i < importPlugins.size(); i++) {
            Importer plugin = pluginManager.
                    getPluginFromString(Importer.class, importPlugins.
                            get(i));

            try {
                importDisplay.getChildren().add(createImportPlugin(importPlugins.
                        get(i), (FileImporter) plugin));
            } catch (MalformedURLException ex) {
            } catch (Exception ex) {
                Logger.getLogger(ImportsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        importDisplay.setTranslateX(0);
        importDisplay.setTranslateY(0);

        importAllCheckBox.selectedProperty().addListener((
                ObservableValue<? extends Boolean> observable,
                Boolean oldValue, Boolean newValue)
                -> {
            if (!oldValue) {
                importAllButton.setDisable(false);
            } else {
                importAllButton.setDisable(true);
            }
        });
    }

    private <E> boolean containsInstance(List<E> list, Class<? extends E> clazz) {
        return list.stream().anyMatch(e -> clazz.isInstance(e));
    }

    private TitledPane createImportPlugin(String name, FileImporter plugin)
            throws MalformedURLException, Exception {

        AnchorPane content = new AnchorPane();
        ProgressBar progress = new ProgressBar();

        StatusListener st = (int i, String string) -> {
            progress.setProgress(i / 100);
        };
        plugin.registerStatusCallback(st);

        Path helpPath = pluginManager.getHelpFilePath(plugin, HelpLanguage.LANG_EN);

        String file = "export/" + pluginManager.pluginToString(plugin) + "-0.0.1/"
                + pluginManager.pluginToString(plugin) + ".properties";

        Properties pro = new Properties();
        pro.load(new FileInputStream(file));
        plugin.loadConfiguration(pro);
        List<VaultEntry> importedData = new ArrayList<>();
        List<VaultEntry> interpretedData = new ArrayList<>();

        List<String> compatiblePlugins = plugin.getListOfCompatiblePluginIDs();

        List<Node> list = new ArrayList();
        final int i;
        Button interpreterButton = new Button(myResource.getString("import.interpreterButton"));

        if (!compatiblePlugins.isEmpty()) {
            pluginManager.pluginsFromStringList(compatiblePlugins);
            //     pluginManager.getPluginFromString(, file) 
            if (containsInstance(pluginManager.pluginsFromStringList(compatiblePlugins), Interpreter.class)) {
                FilteredList<String> filtered;
                filtered = new FilteredList<>(FXCollections.observableList(compatiblePlugins), (String p)
                        -> pluginManager.getPluginFromString(Interpreter.class, p) != null);
                ComboBox selectInterpreter = new ComboBox(FXCollections.observableArrayList(filtered));

                selectInterpreter.relocate(510, 80);
                list.add(selectInterpreter);
                interpreterButton.setDisable(true);
                interpreterButton.relocate(380, 80);
                list.add(interpreterButton);

                interpreterButton.setOnAction((ActionEvent action) -> {
                    Interpreter interpreter
                            = pluginManager.getPluginFromString(Interpreter.class,
                                    // oder einfach alle interpreter anbieten?!?!?!?
                                    (String) selectInterpreter.getSelectionModel().getSelectedItem());
                    interpretedData.clear();
                    interpretedData.addAll(interpreter.interpret(importedData));
                });
            }
        }

        pluginManager.getCompatiblePluginIDs(plugin);
        Properties test = new Properties();
        //  test.

        Button importButton = new Button(myResource.
                getString("import.importButton"));
        importButton.setPrefWidth(100);
        SVGPath gear = new SVGPath();
        gear.setContent("M19.43 12.98c.04-.32.07-.64.07-.98s-.03-.66-.07-.98l2."
                + "11-1.65c.19-.15.24-.42.12-.64l-2-3.46c-.12-.22-.39-.3-.61-."
                + "22l-2.49 1c-.52-.4-1.08-.73-1.69-.98l-.38-2.65C14.46 2."
                + "18 14.25 2 14 2h-4c-.25 0-.46.18-.49.42l-.38 2.65c-.61."
                + "25-1.17.59-1.69.98l-2.49-1c-.23-.09-.49 0-.61.22l-2 3."
                + "46c-.13.22-.07.49.12.64l2.11 1.65c-.04.32-.07.65-.07.98s."
                + "03.66.07.98l-2.11 1.65c-.19.15-.24.42-.12.64l2 3.46c.12.22."
                + "39.3.61.22l2.49-1c.52.4 1.08.73 1.69.98l.38 2.65c.03.24.24."
                + "42.49.42h4c.25 0 .46-.18.49-.42l.38-2.65c.61-.25 1.17-.59 1."
                + "69-.98l2.49 1c.23.09.49 0 .61-.22l2-3.46c.12-.22.07-.49-."
                + "12-.64l-2.11-1.65zM12 15.5c-1.93 0-3.5-1.57-3.5-3.5s1.57-3."
                + "5 3.5-3.5 3.5 1.57 3.5 3.5-1.57 3.5-3.5 3.5z");
        gear.setStyle("-fx-fill: #007399");
        Circle hitBoxGear = new Circle(10);
        hitBoxGear.setOpacity(0);
        hitBoxGear.setCursor(Cursor.HAND);

        hitBoxGear.setOnMouseClicked(event -> {
            try {
                openPluginControll(name + " " + myResource.
                        getString("import.pluginControlTitle"));
            } catch (IOException | URISyntaxException ex) {
                Logger.getLogger(ImportsController.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        });

        Rectangle hitBoxHelpSign = new Rectangle(16, 25);
        hitBoxHelpSign.setCursor(Cursor.HAND);
        hitBoxHelpSign.setOpacity(0);
        hitBoxHelpSign.setOnMouseClicked((MouseEvent event) -> {
            try {
                openPluginHelp(helpPath.toString(), name + " " + myResource.
                        getString("import.pluginHelpTitle"));
            } catch (IOException | URISyntaxException ex) {
                Logger.getLogger(ImportsController.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        });
        SVGPath helpSign = new SVGPath();
        helpSign.setContent("M11 18h2v-2h-2v2zm1-16C6.48 2 2 6.48 2 12s4."
                + "48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3."
                + "59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm0-14c-2.21 0-4 1."
                + "79-4 4h2c0-1.1.9-2 2-2s2 .9 2 2c0 2-3 1.75-3 5h2c0-2.25 3-2."
                + "5 3-5 0-2.21-1.79-4-4-4z");
        /*helpSign.setRotationAxis(Rotate.X_AXIS);
        helpSign.setRotate(180);*/
        helpSign.setStyle("-fx-fill: #007399;");
        importButton.relocate(18, 80);
        hitBoxGear.relocate(540, 11);
        hitBoxHelpSign.relocate(580, 10);
        helpSign.relocate(hitBoxHelpSign.getLayoutX(), hitBoxHelpSign.
                getLayoutY());
        gear.relocate(hitBoxGear.getLayoutX() - 11, hitBoxGear.
                getLayoutY() - 11);
        Button browseButton = new Button(myResource.
                getString("import.browseButton"));
        TextField field = new TextField();

        browseButton.setOnAction((ActionEvent action) -> {
            chooseFiles(action, field);
        });

        browseButton.relocate(510, 40);
        field.relocate(20, 40);
        field.setPrefWidth(465);
        field.setPrefHeight(25);
        field.setId("importPfad");
        field.setText(FileSystemView.getFileSystemView().getDefaultDirectory().
                getPath());
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
        list.addAll(Arrays.asList(fieldLabel, fieldRectangle, field, browseButton,
                helpSign, gear, hitBoxGear, hitBoxHelpSign, importButton));

        ScrollPane teste = new ScrollPane();
        /////////////////////////

        teste.setFitToHeight(true);
        TitledPane pane = new TitledPane("", content);
        AnchorPane.setBottomAnchor(teste, 0.0);
        AnchorPane.setLeftAnchor(teste, 0.0);
        AnchorPane.setRightAnchor(teste, 0.0);
        AnchorPane.setTopAnchor(teste, 0.0);

        content.getChildren().addAll(list);

        teste.setContent(content);
        teste.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        teste.setVbarPolicy(ScrollBarPolicy.NEVER);
        teste.fitToHeightProperty();

        pane.setGraphic(createHbox(name, pane, progress));

        pane.setAnimated(true);

        importButton.setOnAction(e -> {
            try {
                importedData.addAll(plugin.importData(field.getText()));
                MainWindowController.setImported(true);
                interpreterButton.setDisable(false);

            } catch (Exception ex) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("OpenDiabetesVault");
                alert.setHeaderText(myResource.getString("import.importWentWrongAlertHeader"));
                alert.setContentText(myResource.getString("import.importWentWrongAlertContent"));
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource(
                        "/opendiabetesvaultgui/stylesheets/alertStyle.css").
                        toExternalForm());
                alert.showAndWait();
            }

        });
        return pane;
    }

    private HBox createHbox(String name, TitledPane pane, ProgressBar progress) {
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        CheckBox disBox = new CheckBox("");
        if (prefs.getBoolean(name, false)) {
            //   pane.setDisable(true);
            pane.setCollapsible(true);
            pane.setCollapsible(false);
            pane.setExpanded(false);
        } else {
            disBox.selectedProperty().set(true);
        }
        Label title = new Label(name);
        progress.setProgress(0);
        progress.setPrefWidth(300);
        progress.setPrefHeight(20);
        Region filler1 = new Region();
        HBox.setHgrow(filler1, Priority.ALWAYS);
        Region filler2 = new Region();
        HBox.setHgrow(filler2, Priority.ALWAYS);
        hbox.getChildren().addAll(title, filler1, disBox, filler2, progress);
        checkBoxListener(disBox, pane, name, hbox);

        return hbox;
    }

    private void checkBoxListener(CheckBox box, TitledPane target, String name,
            final HBox hbox) {
        box.selectedProperty().addListener((ObservableValue<? extends Boolean> observable,
                Boolean oldValue, Boolean newValue)
                -> {

            target.setExpanded(false);
            target.setCollapsible(newValue);
            //  prefs.putBoolean(name, !newValue);
        });
    }

    /**
     * opens the control page as a popup.
     *
     * @author Kai, Julian
     * @param name
     * @throws java.net.MalformedURLException
     */
    private void openPluginControll(String name)
            throws MalformedURLException, IOException, URISyntaxException {
        openPage(IMPORT_PLUGIN_CONTROL_PAGE, name, false,
                myResource);
    }

    /**
     * Opens the help page as a popup.
     *
     * @author Kai, Julian
     * @param path the path to the help page
     * @param title the title of the help page
     * @throws java.net.MalformedURLException if the created URL could not be
     * parsed.
     */
    private void openPluginHelp(String path, String title)
            throws MalformedURLException, IOException, URISyntaxException {
        Class fc = FatherController.getFatherControllerClass();
        URL url = fc.getResource(IMPORT_PLUGIN_HELP_PAGE).toURI().toURL();
        FXMLLoader loader = new FXMLLoader(url,
                myResource);
        Parent root = loader.load();
        PluginHelpController pluginHelpController = loader.getController();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        pluginHelpController.loadHelpPage(path);
        stage.show();

    }

    /**
     * Saves the path of the chosen file inside a textfield and is assigend to
     * the {@link this#importButton}.
     *
     * @see {@link this#getFileString(Stage stage))}
     *
     * @param event calls method when triggered.
     * @param pathField the textfield in which the path is saved
     *
     */
    @FXML
    private void chooseFiles(final ActionEvent event, final TextField pathField) {
        Stage stage = (Stage) ((Node) (event.getSource())).getScene().
                getWindow();
        pathField.setText(getFileString(stage));

    }

    /**
     * Opens a fileChooser.
     *
     * @param stage stage which displays the filechooser
     * @return The path of the chosen file
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
