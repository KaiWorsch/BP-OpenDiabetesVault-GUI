/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import opendiabetesvaultgui.markdownparser.Parser;
import opendiabetesvaultgui.process.ProcessController;

/**
 * FXML Controller class.
 *
 * @author Daniel Schäfer, Julian Schwind, Martin Steil, Kai Worsch
 */
public class PluginHelpController implements Initializable {

    /**
     * the webview to display the .md code.
     */
    @FXML
    private WebView pluginHelpWebview;



    /**
     * Initializes the controller class.
     *
     * @param url the url of PluginHelp.fxml
     * @param rb the passed ResourceBundle
     */
    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
        // TODO
    }

    /**
     * loads the help given by a plugin.
     *
     * @param path the path to the .md file
     */
    public final void loadHelpPage(final String path) {
        try {
            String htmlCode;
            // parses markdowncode to htmlcode
            htmlCode = Parser.mdParse(path);
            writeToFile(htmlCode);
            // for loading html into the webview-browser
            WebEngine webEngine = pluginHelpWebview.getEngine();
            String pathToHtmlFile;
            pathToHtmlFile = "src/opendiabetesvaultgui/importer/tmp.html";
            File tmp = new File(pathToHtmlFile);
            // passes the local file into a url
            URL url = tmp.toURI().toURL();
            // loads the url via the webEngine
            //webEngine.load(url.toString());
            webEngine.load(url.toString());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProcessController.class.getName())
                    .log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            System.out.println(e.getMessage());
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
        fileStream = new FileOutputStream(
        new File("src/opendiabetesvaultgui/importer/tmp.html"));
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

}
