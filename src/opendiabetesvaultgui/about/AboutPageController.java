/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.about;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import opendiabetesvaultgui.launcher.MainWindowController;

/**
 * FXML Controller class.
 *
 * @author Daniel Schäfer, Julian Schwind, Martin Steil, Kai Worsch
 */
public class AboutPageController implements Initializable {

    /**
     * Hyperlink Element for License Information.
     */
    @FXML
    private Hyperlink licenseInformationLink;

    /**
     * Hyperlink Element for List of Contributers.
     */
    @FXML
    private Hyperlink listOfContributersLink;

    /**
     * Hyperlink Element for How to Contribute.
     */
    @FXML
    private Hyperlink contributionLink;

    /**
     * Hyperlink Element for Redistribution conditions.
     */
    @FXML
    private Hyperlink redistributionConditionsLink;

    /**
     * Hyperlink Element for Donate.
     */
    @FXML
    private Hyperlink donationLink;

    /**
     * Resource Bundle.
     */
    @FXML
    private ResourceBundle myResource;

    /**
     * Textfield, not visible.
     */
    @FXML
    private TextArea text;


    /**
     * Initializes the controller class.
     *
     * @param url Url
     * @param rb ResourceBundle
     */
    @Override
    public final void initialize(final URL url, final ResourceBundle rb) {


        Font.loadFont(MainWindowController.class.getResource(
                "/opendiabetesvaultgui/stylesheets/fonts/Roboto-Regular.ttf").
                toExternalForm(), 10);
        text.setPadding(new Insets(2));

        // TODO
    }

    /**
     * This method will be executed as soon as a link is clicked.
     * Not finished yet.
     *
     * @param event an ActionEvent
     */
    @FXML
    private void handleLinkAction(final ActionEvent event) {
        //TODO implement basic Link opening.
    }
}
