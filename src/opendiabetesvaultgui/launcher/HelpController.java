/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.filechooser.FileSystemView;
import opendiabetesvaultgui.markdownparser.Parser;
import opendiabetesvaultgui.process.ProcessController;

/**
 * FXML Controller class manages the help pages.
 *
 * @author Daniel Schäfer, Julian Schwind, Martin Steil, Kai Worsch
 */
public class HelpController extends FatherController implements Initializable {

    /**
     * to access the webview-browser.
     *
     * @see loadPage
     */
    @FXML
    private WebView webview;

    /**
     * To initialize the controller class.
     *
     * @param location used to resolve relative paths for the root object, or
     * null if the location is not known.
     * @param resources used to localize the root object, or null if the root
     * object was not localized.
     */
    @Override
    public final void initialize(final URL location,
            final ResourceBundle resources) {
        try {

            String path;
            path = getPagePath(MainWindowController.getPage(),
                    MainWindowController.getLanguage());
            loadPage(path);           
          
        } catch (IOException ex) {
            Logger.getLogger(HelpController.class.getName())
                    .log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(HelpController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Gets a path of a md file and loads the content into a helppage.
     *
     * @param path the path to the .md file
     * @throws IOException if read from file goes wrong
     * @throws FileNotFoundException if file was not found
     */
    private void loadPage( String path) throws IOException, URISyntaxException {
        try {
            String htmlCode;
            // parses markdowncode to htmlcode
            System.out.println(path);
            Parser parser = new Parser();
            //path = "/opendiabetesvaultgui/launcher/help/de/PatientSelectionHelpDE.md";
            //path = "/resources/help/de/PatientSelectionHelpDE.md";
            htmlCode = parser.mdParse(path);
            writeToFile(htmlCode);
            // for loading html into the webview-browser
            final WebEngine webEngine = webview.getEngine();
            String pathToHtmlFile = FileSystemView.getFileSystemView().
                    getDefaultDirectory().getPath() + File.separator
                    + "ODV/.pageHelpTMP.html";
 
            File tmp = new File(pathToHtmlFile);
            // passes the local file into a url
            URL url = tmp.toURI().toURL();
            // loads the url via the webEngine
            webEngine.load(url.toString());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProcessController.class.getName())
                    .log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * returns the path of the .md file relating to the given pageID.
     *
     * @param pageID a int number which represents a page of the workflow
     * @param language a String which represents the actual language
     * @return path a string of the path to the .md file
     */
    private String getPagePath(final int pageID, final String language) {
        final int zero = MainWindowController.getPATIENT_SELECTION_ID(); // constants for the switch/case-statement
        final int one = MainWindowController.getIMPORT_ID();
        final int two = MainWindowController.getSLICE_ID();
        final int three = MainWindowController.getPROCESS_ID();
        final int four = MainWindowController.getEXPORT_ID();

        String path; 
        String lang;
        if ("English".equals(PREFS_FOR_ALL.get(LANGUAGE_NAME, ""))) {
            path = "/resources/help/en/";
            lang = "EN.md";
        } else {
            path = "/resources/help/" + language + "/";
            lang = language.toUpperCase(Locale.ENGLISH) + ".md";
        }
        if (pageID == zero ){
            return path + "PatientSelectionHelp" + lang;
        }
        else if (pageID == one){
            return path + "ImportHelp" + lang;
        } 
        else if (pageID == two){
            return path + "SliceHelp" + lang;
        }
        else if (pageID == three){
            return path + "ProcessHelp" + lang;
        }
        else if (pageID == four){
            return path + "ExportHelp" + lang;
        }
        else {
                return path + "Default" + lang;
        }
    }

    /**
     * writes a string of htmlcode into the help_tmp.html file.
     *
     * @param text a string of html code
     * @throws IOException if write into the file goes wrong
     */
    public final void writeToFile(final String text) throws IOException {

        FileOutputStream fileStream = null;
        OutputStreamWriter writer;
        try {
            String path = FileSystemView.getFileSystemView().
                    getDefaultDirectory().getPath() + File.separator
                    + "ODV/.pageHelpTMP.html";
            System.out.println(path);
            fileStream = new FileOutputStream(
            new File(path));
            //new File("resources/help/helpTMP.html"));
            writer = new OutputStreamWriter(fileStream, "UTF-8");
            writer.write(text);
            writer.flush();
            writer.close();
            fileStream.flush();
            fileStream.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if ((fileStream != null)) {
                    fileStream.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * webview getter method.
     *
     * @return webview
     */
    public final WebView getWebview() {
        return webview;
    }
}
