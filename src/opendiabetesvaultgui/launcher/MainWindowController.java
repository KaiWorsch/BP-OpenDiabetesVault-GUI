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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.filechooser.FileSystemView;
import static opendiabetesvaultgui.launcher.FatherController.LANGUAGE_DISPLAY;
import static opendiabetesvaultgui.launcher.FatherController.PREFS_FOR_ALL;
import static opendiabetesvaultgui.launcher.FatherController.RESOURCE_PATH;
import opendiabetesvaultgui.patientselection.DBConnection;

/**
 * FXML Controller classe, manages the main window.
 *
 * @author Daniel Schäfer, Martin Steil, Julian Schwind, Kai Worsch
 */
public class MainWindowController extends FatherController
        implements Initializable {

    /**
     * the pane were everything is arranged. espacially the workflowbar
     * elements, the menu bar and the functionPane
     */
    @FXML
    private AnchorPane fatherPane;
    /**
     * the pane in which the different pages are loaded and displayed.
     */
    @FXML
    private AnchorPane functionPane;

    /**
     * SVGPath of the patient selection page workflowbar element.
     */
    @FXML
    private SVGPath patientNavigation;
    /**
     * SVGPath of the import page workflowbar element.
     */
    @FXML
    private SVGPath importNavigation;
    /**
     * SVGPath of the slice page workflowbar element.
     */
    @FXML
    private SVGPath sliceNavigation;
    /**
     * SVGPath of the the process page workflowbar element.
     */
    @FXML
    private SVGPath processNavigation;
    /**
     * SVGPath of the export page workflowbar element.
     */
    @FXML
    private SVGPath exportNavigation;

    /**
     * SVGPath of the patient selection page workflowbar check.
     */
    @FXML
    private SVGPath patientCheck;
    /**
     * SVGPath of the import page workflowbar check.
     */
    @FXML
    private SVGPath importCheck;
    /**
     * SVGPath of the slice page workflowbar check.
     */
    @FXML
    private SVGPath sliceCheck;
    /**
     * SVGPath of the process page workflowbar check.
     */
    @FXML
    private SVGPath processCheck;
    /**
     * SVGPath of the export page workflowbar check.
     */
    @FXML
    private SVGPath exportCheck;

    /**
     * workflowbar Label of the patient selection page.
     */
    @FXML
    private Label patientLabel;
    /**
     * workflowbar Label of the import page.
     */
    @FXML
    private Label importLabel;
    /**
     * workflowbar Label of the slice page.
     */
    @FXML
    private Label sliceLabel;
    /**
     * workflowbar Label of the process page.
     */
    @FXML
    private Label processLabel;
    /**
     * workflowbar Label of the export page.
     */
    @FXML
    private Label exportLabel;

    /**
     * MenuItem of MainWindow MenuBar FullScreen.
     */
    @FXML
    private MenuItem fullScreen;

    /**
     * MenuItem of MainWindow MenuBar FullScreen.
     */
    @FXML
    private MenuItem defaultSize;

    /**
     * MenuItem of MainWindow MenuBar Help.
     */
    @FXML
    private MenuItem menuItemHelp;

    /**
     * MenuItem of MainWindow MenuBar About.
     */
    @FXML
    private MenuItem menuItemAbout;

    /**
     * MenuItem of MainWindow MenuBar Option.
     */
    @FXML
    private MenuItem menuItemOption;

    /**
     * a rectangle to seperate the active page from the workflowbar.
     */
    @FXML
    private Rectangle seperatorBar;
    /**
     * the hashmap containing the workflowbar element for every page. the key is
     * the correlated page number: 0 for patient selection 1 for import 2 for
     * slice 3 for process 4 for export
     */
    private Map<Integer, WorkflowbarElement> navigationbarElements;

    /**
     * integer value page to show the current page. 0 for patient selection 1
     * for import 2 for slice 3 for process 4 for export
     */
    private static int page = 0;
    /**
     * The used ResourceBundle in MainWindowController. It will be set in
     * initialize.
     */
    private ResourceBundle usedResource;

    /**
     * boolean value to check if an patient has been selected.
     */
    private static boolean selected;
    /**
     * boolean value to check if files has been imported.
     */
    private static boolean imported;

    /**
     * The height of the navigationBar.
     */
    private final double navigationBarHeight = 44;
    /**
     * The padding used to relocate the checks.
     */
    private final double checksPaddingX = 10;
    /**
     * The padding used to relocate the checks.
     */
    private final double checksPaddingY = 3;
    /**
     * The padding used to relocate the svgPaths.
     */
    private final double svgPathPaddingX = 60;
    /**
     * the default window size. given as percentage displaysize
     */
    private final double defaultWindowSize = 0.7;

    /**
     * handles page(pane) switching while in mainwindow.
     * stackoverflow.com/questions/33748127/how-to-load-fxml-file-inside-pane
     *
     * @param targetPage the page to switch to, as an integer value 0 for
     * patient selection 1 for import 2 for slice 3 for process 4 for export
     * @throws MalformedURLException if a string path coulnt be parsed
     * @throws IOException if a fxml file wasnt found
     * @throws java.net.URISyntaxException
     */
    public final void switchPane(final int targetPage)
            throws MalformedURLException, IOException, URISyntaxException {
        resizeNavigationbar();
        /**
         * switching is only possible: between patientSelection and import (back
         * and forth) from/to every page after the import was completed This
         * resets, when a new patient is selected
         */
        if (((targetPage == 0 && page == 1) || (targetPage == 1 && page == 0)
                || imported) || (targetPage != page)) {

            if (page == 0 && !selected && targetPage == 1 && !imported) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("OpenDiabetesVault");
                alert.setHeaderText(usedResource.
                        getString("main.continueAlertHeader"));
                alert.setContentText(usedResource.getString(
                        "main.continueAlertContent"));
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource(
                        "/opendiabetesvaultgui/stylesheets/alertStyle.css").
                        toExternalForm());
                //dialogPane.getStyleClass().add("alertStyle");
                Stage stage = (Stage) alert.getDialogPane().getScene().
                        getWindow();
                /**
                 * stage.getIcons(). add(new
                 * Image(this.getClass().getResource(ICON).toString()));
                 */
                stage.getIcons().add(new Image(createURL(ICON).toString()));
                alert.showAndWait();

            } else {
                changePane(navigationbarElements.get(targetPage).getPagePath(), functionPane, usedResource);
                setSVGPathColor("seen", navigationbarElements.get(page).
                        getNavigationElement());
                setLabelColor("seen", navigationbarElements.get(page).
                        getLabelElement());
                setSVGPathColor("current", navigationbarElements.
                        get(targetPage).getNavigationElement());
                setLabelColor("current", navigationbarElements.get(targetPage).
                        getLabelElement());
                navigationbarElements.get(page).getCheckElement().
                        setVisible(true);
                for (int i = targetPage; i < navigationbarElements.size();
                        i++) {
                    navigationbarElements.get(i).getCheckElement().
                            setVisible(false);
                }
                setPage(targetPage);

                if (selected) {
                    setSelected(false);
                }
            }

        }
    }

    /**
     * Method to call page switching to Patient selection page.
     *
     *
     * @param e Event: mouse click on patientNavigation/patientLabel
     * @throws java.io.IOException if the fxml file on position 1 in the
     * workflow wasnt found
     * @throws java.net.MalformedURLException
     * @throws java.net.URISyntaxException
     */
    @FXML
    public final void clickPatientSelection(final MouseEvent e)
            throws IOException, MalformedURLException, URISyntaxException {
        if (e.getButton() == MouseButton.PRIMARY) {
            switchPane(0);
        }
    }

    /**
     * method to call page switching to import page.
     *
     * @param e Event: mouse click on importNavigation/importLabel
     * @throws Exception if the fxml file on position 2 in the workflow wasnt
     * found
     */
    @FXML
    public final void clickImport(final MouseEvent e) throws Exception {
        if (e.getButton() == MouseButton.PRIMARY) {
            switchPane(1);
        }
    }

    /**
     * method to call page switching to slice page.
     *
     * @param e Event: mouse click on sliceNavigation/sliceLabel
     * @throws java.io.IOException Exception if the fxml file on position 3 in
     * the workflow wasnt found
     * @throws java.net.MalformedURLException
     * @throws java.net.URISyntaxException
     */
    @FXML
    public final void clickSlice(final MouseEvent e) throws IOException, MalformedURLException, URISyntaxException {
        if (e.getButton() == MouseButton.PRIMARY) {
            switchPane(2);
        }
    }

    /**
     * method to call page switching to process page.
     *
     * @param e Event: mouse click on processNavigation/processLabel
     * @throws java.io.IOException Exception if the fxml file on position 4 in
     * the workflow wasnt found
     * @throws java.net.MalformedURLException
     * @throws java.net.URISyntaxException
     */
    @FXML
    public final void clickProcess(final MouseEvent e) throws IOException, MalformedURLException, URISyntaxException {
        if (e.getButton() == MouseButton.PRIMARY) {
            switchPane(3);
        }
    }

    /**
     * method to call page switching to export page.
     *
     * @param e Event: mouse click on exportNavigation/exportLabel
     * @throws java.io.IOException if the fxml file on position 5 in the
     * workflow wasnt found
     * @throws java.net.MalformedURLException
     * @throws java.net.URISyntaxException
     */
    @FXML
    public final void clickExport(final MouseEvent e) throws IOException, MalformedURLException, URISyntaxException {
        if (e.getButton() == MouseButton.PRIMARY) {
            switchPane(4);
        }
    }

    /**
     * Not used right now. resets the pages back to the Patient selection page.
     *
     * @param action the action to trigger it
     * @throws MalformedURLException if the path to fxml couldnt be parsed
     * @throws IOException Exception if patientSelection.fxml is not found
     * @throws java.net.URISyntaxException
     */
    @FXML
    public final void reset(final ActionEvent action)
            throws MalformedURLException, IOException, URISyntaxException {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("OpenDiabetesVault");
        alert.setHeaderText("Confirmation");
        alert.setContentText("Are you sure you want to reset your progress:");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource(
                        "/opendiabetesvaultgui/stylesheets/alertStyle.css").
                        toExternalForm());
        //dialogPane.getStyleClass().add("alertStyle");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        //stage.getIcons().add(new Image(this.getClass()
        //.getResource(ICON).toString()));
        stage.getIcons().add(new Image(createURL(ICON).toString()));

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() != ButtonType.CANCEL) {
            changePane(navigationbarElements.get(0).getPagePath(), functionPane,
                    usedResource);
            for (int i = 0; i < navigationbarElements.size(); i++) {
                WorkflowbarElement element = navigationbarElements.get(i);
                setLabelColor("unseen", element.getLabelElement());
                setSVGPathColor("current", element.getNavigationElement());
                element.getCheckElement().setVisible(false);
            }
            setPage(0);
        }
    }

    /**
     * Opens the about page as a popup.
     *
     * @param action ActionEvent: when triggerd calls the method
     * @throws MalformedURLException if the path to fxml couldnt be parsed
     * @throws IOException Exception if AboutPage.fxml isnt found
     * @throws java.net.URISyntaxException if path couldn't be parsed as a URI
     */
    @FXML
    public final void openAboutPage(final ActionEvent action)
            throws MalformedURLException,
            IOException,
            URISyntaxException {
        FXMLLoader loader;
        Class fc = getFatherControllerClass();
        URL url = fc.getResource(ABOUT_PAGE).toURI().toURL();
        openPageAndCloseSameWindows(url,
        usedResource.getString("about.title"), false, usedResource);
    }

    /**
     * Opens the option page as a popup.
     *
     * @param action ActionEvent : when triggered calls the method
     * @throws MalformedURLException if the path to fxml couldnt be parsed
     * @throws IOException Exception if OptionsWindow.fxml isnt found
     * @throws java.net.URISyntaxException if path couldn't be parsed as a URI
     */
    @FXML
    public final void openOptionWindow(final ActionEvent action)
            throws MalformedURLException, IOException, URISyntaxException {
        String oldLanguage = PREFS_FOR_ALL.get(LANGUAGE_DISPLAY, "");
        String oldDateFormat = PREFS_FOR_ALL.get("dateFormat", "");

        openPage(MAIN_OPTIONS_WINDOW, usedResource
                .getString("option.title"), false, usedResource);

        getWindowStage().setOnHiding((WindowEvent e) -> {
            if (!oldLanguage.equals(PREFS_FOR_ALL.get(LANGUAGE_DISPLAY, "")) || !oldDateFormat.equals(PREFS_FOR_ALL.get("dateFormat", ""))) {
                
                FXMLLoader loader;
                try {
                    try {
                        DBConnection.closeConnection();
                    } catch (SQLException ex) {
                        Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Class fc = getFatherControllerClass();
                    URL url = fc.getResource(MAIN_PAGE).toURI().toURL();
                    loader = new FXMLLoader(url,
                            ResourceBundle.getBundle(RESOURCE_PATH,
                                    new Locale(PREFS_FOR_ALL.
                                            get(LANGUAGE_DISPLAY, ""))));
                    fatherPane.getChildren().clear();
                    Pane languageChange = loader.load();
                    setMainWindowController((MainWindowController) loader.
                            getController());
                    fatherPane.getChildren().add(languageChange);
                    AnchorPane pane = (AnchorPane) fatherPane.getChildren().
                            get(0);
                    AnchorPane.setBottomAnchor(pane, 0.0);
                    AnchorPane.setLeftAnchor(pane, 0.0);
                    AnchorPane.setRightAnchor(pane, 0.0);
                    AnchorPane.setTopAnchor(pane, 0.0);
                    getMainWindowController().resizeNavigationbar();
                    
                } catch (MalformedURLException ex) {
                    Logger.getLogger(MainWindowController.class.getName()).
                            log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainWindowController.class.getName()).
                            log(Level.SEVERE, null, ex);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });

    }

    /**
     * Opens the help page as a popup.
     *
     * @param event Action event : when triggered starts the routine
     * @throws MalformedURLException if the path to fxml couldnt be parsed
     * @throws IOException Exception if help.fxml is not found
     * @throws java.net.URISyntaxException if path couldn't be parsed as a URI
     */
    public final void openHelpPage(final Event event)
            throws MalformedURLException, IOException, URISyntaxException {
        FXMLLoader loader;
        Class fc = getFatherControllerClass();
        URL url = fc.getResource(MAIN_HELP_PAGE).toURI().toURL();
        openPageAndCloseSameWindows(url,
        usedResource.getString("help.title"), true, usedResource);
    }

    /**
     * This method sets the MainWindow to fullscreen. If the window is already
     * in fullscreen, it will be set back to windowed mode.
     *
     * @param e when triggered calls this method
     */
    @FXML
    public final void doFullScreen(final ActionEvent e) {
        if (!getPrimaryStage().isFullScreen()) {
            getPrimaryStage().setFullScreen(true);
            fullScreen.setText(usedResource.
                    getString("main.menuBarViewMenuItemExitFullScreen"));

        } else {
            getPrimaryStage().setFullScreen(false);
            fullScreen.setText(usedResource.
                    getString("main.menuBarViewMenuItemFullScreen"));
        }

    }

    /**
     * Sets the default Window size.
     *
     * @param e the Event to trigger it
     */
    @FXML
    public final void defaultSize(final ActionEvent e) {
        if (getPrimaryStage().isFullScreen()) {
            getPrimaryStage().setFullScreen(false);
        }
        getPrimaryStage().
                setWidth((SCREEN_BOUNDS.getWidth()) * defaultWindowSize);
        getPrimaryStage().
                setHeight((SCREEN_BOUNDS.getHeight()) * defaultWindowSize);
    }

    @Override
    // Call at the beginning PatientSelection
    public final void initialize(final URL location,
            final ResourceBundle resources) {
        setImported(false);
        usedResource = resources;

        // Shortcut for Helppage
        this.menuItemHelp.setAccelerator(new KeyCodeCombination(KeyCode.F1));
        // Shortcut for About
        this.menuItemAbout.setAccelerator(new KeyCodeCombination(KeyCode.F1,
                KeyCombination.SHIFT_DOWN));
        // Shortcut for Options
        this.menuItemOption.setAccelerator(new KeyCodeCombination(KeyCode.C,
                KeyCombination.CONTROL_DOWN));
        // Shortcut for FullScreen
        this.fullScreen.setAccelerator(new KeyCodeCombination(KeyCode.F11));
        // Shortcut for DefaultSize
        this.defaultSize.setAccelerator(new KeyCodeCombination(KeyCode.F12));

        try {
            initializeNavigationbarElements();
        } catch (MalformedURLException ex) {
            Logger.getLogger(MainWindowController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        if (PREFS_FOR_ALL.get("pathDatabase", "").equals("")) {
            PREFS_FOR_ALL.put("pathDatabase",
                    (FileSystemView.getFileSystemView().getDefaultDirectory().getPath()
                    + File.separator
                    + "ODV" + File.separator + "database"));
        }

        setPage(0);

        Font.loadFont(MainWindowController.class.getResource(
                "/opendiabetesvaultgui/stylesheets/fonts/Roboto-Regular.ttf").
                toExternalForm(), 10);

        try {
            stageSizeChangeListener();
            setSVGPathColor("current", patientNavigation);
            changePane(PATIENT_SELECTION_PAGE, functionPane,
                    usedResource);

        } catch (MalformedURLException ex) {
            Logger.getLogger(MainWindowController.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(MainWindowController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * Changes the color of SVGPath element.
     *
     * @param color the new color to set
     * @param changeMyColor the element on which the change shall apply
     */
    public final void setSVGPathColor(final String color,
            final SVGPath changeMyColor) {
        String hex;
        switch (color) {
            case "seen":
                //hex = "#3d6fff";
                hex = "#007399";
                break;
            case "current":
                //hex = "#43ff3d";
                hex = "#0096c9";
                break;
            case "unseen":
                hex = "#c3c3c3";
                break;
            default:
                hex = "#000000";
                System.out.println("THIS COLOR ISNT SUPPORTED ");
        }

        changeMyColor.setFill(Paint.valueOf(hex));

    }

    /**
     * Changes the color of an Label.
     *
     * @param color the new color to set
     * @param changeMyColor the Label on which the change shall apply
     */
    public final void setLabelColor(final String color,
            final Label changeMyColor) {
        String hex;
        switch (color) {
            case "current":
                //hex = "#3d6fff";
                //hex = "#0096c9";
                hex = "#FFFFFF";
                break;
            case "seen":
                //hex = "#43ff3d";
                hex = "#FFFFFF";
                break;
            case "unseen":
                hex = "#000000";
                break;
            default:
                hex = "#FF69b4";
                System.out.println("THIS LANGUAGE ISNT SUPPORTED");
        }

        changeMyColor.setTextFill(Paint.valueOf(hex));

    }

    /**
     * resizes the navigation bar in order to the window size.
     */
    private void resizeNavigationbar() {
        double windowWidth = FatherController.getPrimaryStage().getWidth();
        double originalWidth = importNavigation.getLayoutBounds().getWidth();
        double requiredWidth = windowWidth * 0.2;
        double scale = (requiredWidth + 23) / originalWidth;

        //******* relocate and scale the navigationbar ************//
        patientNavigation.setScaleX(scale);
        patientNavigation.relocate(requiredWidth / 2, navigationBarHeight);

        importNavigation.setScaleX(scale);
        importNavigation.relocate(patientNavigation.getLayoutX()
                + requiredWidth, navigationBarHeight);

        sliceNavigation.setScaleX(scale);
        sliceNavigation.relocate(importNavigation.getLayoutX()
                + requiredWidth, navigationBarHeight);

        processNavigation.setScaleX(scale);
        processNavigation.relocate(sliceNavigation.getLayoutX()
                + requiredWidth, navigationBarHeight);

        exportNavigation.setScaleX(scale);
        exportNavigation.relocate(processNavigation.getLayoutX()
                + requiredWidth, navigationBarHeight);

        //********* relocate the labels from the navigationbar ********//
        exportLabel.setLayoutX(exportNavigation.getLayoutX() + svgPathPaddingX);
        processLabel.
                setLayoutX(processNavigation.getLayoutX() + svgPathPaddingX);
        importLabel.setLayoutX(importNavigation.getLayoutX() + svgPathPaddingX);
        // doesnt need padding is long enough
        patientLabel.setLayoutX(patientNavigation.getLayoutX());
        sliceLabel.setLayoutX(sliceNavigation.getLayoutX() + svgPathPaddingX);

        // relocate checks from navigationbar
        patientCheck.relocate(patientLabel.getLayoutX()
                + patientLabel.getWidth()
                + checksPaddingX, patientLabel.getLayoutY()
                + checksPaddingY);
        importCheck.relocate(importLabel.getLayoutX()
                + importLabel.getWidth()
                + checksPaddingX, importLabel.getLayoutY()
                + checksPaddingY);
        processCheck.relocate(processLabel.getLayoutX()
                + processLabel.getWidth()
                + checksPaddingX, processLabel.getLayoutY()
                + checksPaddingY);
        sliceCheck.relocate(sliceLabel.getLayoutX()
                + sliceLabel.getWidth()
                + checksPaddingX, sliceLabel.getLayoutY() + checksPaddingY);

        seperatorBar.setWidth(windowWidth);
    }

    /**
     * listens to size changes on the main stage.
     *
     * calls resizeNavigationbar if the width is changed
     *
     * https://stackoverflow.com/questions/47044623/stage-resize-event-javafx
     */
    private void stageSizeChangeListener() {
        getPrimaryStage().widthProperty().
                addListener((ObservableValue<? extends Number> observable,
                        Number oldValue, Number newValue) -> {
                    resizeNavigationbar();
                });
    }

    /**
     * Initializes Hashmap which indicates the order and the different elements
     * of each workflow pane.
     *
     * @throws MalformedURLException if the path to fxml couldnt be parsed
     */
    private void initializeNavigationbarElements()
            throws MalformedURLException {
        navigationbarElements = new HashMap<>();
        navigationbarElements.put(0, new WorkflowbarElement(patientNavigation,
                patientCheck, patientLabel, PATIENT_SELECTION_PAGE));
        navigationbarElements.put(1, new WorkflowbarElement(importNavigation,
                importCheck, importLabel, IMPORT_PAGE));
        navigationbarElements.put(2, new WorkflowbarElement(sliceNavigation,
                sliceCheck, sliceLabel, SLICE_PAGE));
        navigationbarElements.put(3, new WorkflowbarElement(processNavigation,
                processCheck, processLabel, PROCESS_PAGE));
        navigationbarElements.put(4, new WorkflowbarElement(exportNavigation,
                exportCheck, exportLabel, EXPORT_PAGE));
    }

    /**
     *
     * @return importerd
     */
    public static boolean isImported() {
        return imported;
    }

    /**
     * Sets imported.
     *
     * @param isImported the value which imported is set to.
     */
    public static void setImported(final boolean isImported) {
        MainWindowController.imported = isImported;
    }

    /**
     *
     * @return selected
     */
    public static boolean isSelected() {
        return selected;
    }

    /**
     * Sets selected.
     *
     * @param isSelected the value selected is set to.
     */
    public static void setSelected(final boolean isSelected) {
        MainWindowController.selected = isSelected;
    }

    /**
     * returns the actual page value.
     *
     * @return int
     */
    public static int getPage() {
        return page;
    }

    /**
     * Sets page.
     *
     * @param pageValue the value page is set to
     */
    public static void setPage(final int pageValue) {
        MainWindowController.page = pageValue;

    }

    /**
     * returns the actual page language.
     *
     * @return String "de" for german or " " for default english
     */
    public static String getLanguage() {
        return PREFS_FOR_ALL.get(LANGUAGE_DISPLAY, "");
    }
}
