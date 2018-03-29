/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.exporter;

import de.opendiabetes.vault.plugin.exporter.Exporter;
import de.opendiabetes.vault.plugin.management.OpenDiabetesPluginManager;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
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
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
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
import static opendiabetesvaultgui.launcher.FatherController.IMPORT_PLUGIN_CONTROL_PAGE;
import static opendiabetesvaultgui.launcher.FatherController.IMPORT_PLUGIN_HELP_PAGE;

/**
 * FXML Controller class
 *
 * @author Kai
 */
public class ExportsController extends FatherController implements Initializable {

    @FXML
    private VBox exportDisplay;
    private OpenDiabetesPluginManager pluginManager;

    private ResourceBundle usedResource;

    private final Preferences prefs = Preferences.userNodeForPackage(ImportsController.class);
    double importBoxPositionY = 10;

    double interpreterBoxPositiony = 10;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usedResource = resources;

        exportDisplay.setTranslateX(0);
        exportDisplay.setTranslateY(0);

        pluginManager = OpenDiabetesPluginManager.getInstance();

        List<String> importPlugins = pluginManager.
                getPluginIDsOfType(Exporter.class);

        for (int i = 0; i < importPlugins.size(); i++) {
            Exporter plugin = pluginManager.
                    getPluginFromString(Exporter.class, importPlugins.
                            get(i));
            exportDisplay.getChildren().add(createExporter(importPlugins.
                    get(i), plugin));
        }

        /* ((TitledPane) (exportGroup.getChildren().get(0))).setExpanded(true);
        ((TitledPane) (exportGroup.getChildren().get(1))).setExpanded(true);
        ((TitledPane) (groupTwo.getChildren().get(0))).setExpanded(true); */
        // greatFatherPane.widthProperty().add(ObersvableValue)
        /*importAllCheckBox.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
                -> {
            System.out.print("beinah");
            if (oldValue == false && newValue == true) {
                importAllButton.setDisable(false);
            } else {
                importAllButton.setDisable(true);
            }
        });*/
 /*  group_two.getChildren().setAll(
                createTitledPane("Export Plugin", true),
                createTitledPane("irgendein Plugin", true)


        ); 
        
        group_one.getChildren().setAll(
                createTitledPane("TestPlugin", true),
                createTitledPane("Plugger", true),
                createTitledPane("Egal eigentlich", false)
        );
       ((TitledPane) (group_one.getChildren().get(0))).setExpanded(true);
       ((TitledPane) (group_two.getChildren().get(0))).setExpanded(true);

        Scene testScene = getPrimaryStage().getScene();
        
        one.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        two.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        // one.widthProperty().
      
   // To change body of generated methods, choose Tools | Templates.
   // throw new UnsupportedOperationException("Not supported yet.");
    }
   
     public TitledPane createTitledPane(String name, Boolean need_path) {

        AnchorPane content = new AnchorPane();
 // -------------------- IMPORT BUTTON -----------------------------------//
        Button import_button = new Button("Import");

        import_button.setLayoutX(50);
        import_button.setLayoutY(80);
        import_button.setPrefWidth(100);
 // -------------------- PROGRESSBAR -----------------------------------//

        ProgressBar progress = new ProgressBar();

        progress.setLayoutX(180);
        progress.setLayoutY(85);
        progress.setPrefWidth(150);
        progress.setPrefHeight(20);
       
// list for all added elements
        List<Node> list = new ArrayList<>(
                Arrays.asList(import_button, progress));
// if the plugin needs a path
        if (need_path) {
            Button browse_import = new Button("Browse");
            TextField field = new TextField();

      /*      browse_import.setOnAction((ActionEvent action) -> {
                choose_files_and_get_paths(action, field);
            }); 
            browse_import.setLayoutX(260);
            browse_import.setLayoutY(20);
            field.setLayoutX(30);
            field.setLayoutY(20);
            field.setPrefWidth(180);
            browse_import.setPrefWidth(120);

            list.add(field);
            list.add(browse_import);
        }
   // add all elements to the pane
        content.getChildren().addAll(list);
        TitledPane pane = new TitledPane(name, content);
        pane.setExpanded(false);

        return pane; */
    }

    public TitledPane createExporter(String name, Exporter plugin) {

        AnchorPane content = new AnchorPane();

        //Path helpPath = pluginManager.getHelpFilePath(plugin);

        Button exportButton = new Button(usedResource.getString("export.exportButton"));
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
                openPluginControll(name + " " + usedResource.getString("import.pluginControlTitle"));
            } catch (IOException ex) {
                Logger.getLogger(ImportsController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(ExportsController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        Rectangle hitBoxHepSign = new Rectangle(16, 25);
        hitBoxHepSign.setCursor(Cursor.HAND);
        hitBoxHepSign.setOpacity(0);
        /*hitBoxHepSign.setOnMouseClicked((MouseEvent event) -> {
            try {
                openPluginHelp(helpPath.toString(), name + "  " + usedResource.getString("import.pluginHelpTitle"));
            } catch (IOException ex) {
                Logger.getLogger(ImportsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });*/
        SVGPath helpSign = new SVGPath();
        helpSign.setContent("M11 18h2v-2h-2v2zm1-16C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm0-14c-2.21 0-4 1.79-4 4h2c0-1.1.9-2 2-2s2 .9 2 2c0 2-3 1.75-3 5h2c0-2.25 3-2.5 3-5 0-2.21-1.79-4-4-4z");
        /*helpSign.setRotationAxis(Rotate.X_AXIS);
        helpSign.setRotate(180);*/
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
        Button browseButton = new Button(usedResource.getString("import.browseButton"));
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

        Label fieldLabel = new Label(usedResource.getString("import.fieldLabel"));
        fieldLabel.setId("fieldLabel");

        list.add(fieldBox);
        list.add(fieldLabel);
        list.add(fieldRectangle);
        list.add(field);
        list.add(browseButton);

        TitledPane pane = new TitledPane("", content);

        content.getChildren().addAll(list);
        BorderPane bPane = new BorderPane();
        HBox hbox = new HBox();
        // hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        CheckBox disBox = new CheckBox("");
        Label title = new Label(name);
        progress.setDisable(true);
        hbox.getChildren().addAll(title, disBox, progress);
        pane.graphicTextGapProperty().bind(pane.widthProperty());
        pane.setGraphic(hbox);
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

        checkBoxListener(disBox, pane, name, hbox);

        pane.setAnimated(false);

        return pane;
    }

    public void openPluginHelp(String path, String title) throws MalformedURLException, IOException {

        FXMLLoader loader = new FXMLLoader(createURL(IMPORT_PLUGIN_HELP_PAGE), usedResource);
        Parent root = loader.load();

        PluginHelpController pluginHelpController = loader.getController();
        Stage stage = new Stage();
        stage.setTitle(title);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        pluginHelpController.loadHelpPage(path);
        stage.show();

        //pluginHelpController.loadHelpPage(path);
    }

    private void checkBoxListener(CheckBox box, TitledPane target, String name, HBox hbox) {
        box.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
                -> {
            target.setExpanded(newValue);
            target.setCollapsible(newValue);
            prefs.putBoolean(name, !newValue);
        });
    }

    /**
     * opens the control page as a popup.
     *
     * @author Kai, Julian
     * @param name
     * @throws java.net.MalformedURLException
     * @throws java.net.URISyntaxException
     */
    public void openPluginControll(String name) throws MalformedURLException, IOException, URISyntaxException {
        openPage(IMPORT_PLUGIN_CONTROL_PAGE, name, false, usedResource);
    }

    /**
     * opens the help page as a popup
     *
     * @author Kai, Julian
     * @param name
     * @throws java.net.MalformedURLException
     */
    public void openPluginHelp(String name) throws MalformedURLException, IOException {
        openPageAndCloseSameWindows(createURL(IMPORT_PLUGIN_HELP_PAGE), name, false, usedResource);
    }

    @FXML
    private void chooseFiles(ActionEvent event, TextField test) {
        Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
        test.setText(getFileString(stage));

    }

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
