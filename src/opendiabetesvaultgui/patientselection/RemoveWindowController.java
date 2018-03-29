/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.patientselection;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import opendiabetesvaultgui.patientselection.PatientSelectionController;
import javafx.scene.text.Font;
import javax.swing.filechooser.FileSystemView;

/**
 * FXML Controller class
 *
 * @author schae_000
 */
public class RemoveWindowController implements Initializable {

    @FXML
    private TextField firstnameInput;
    @FXML
    private TextField lastnameInput;
    @FXML
    private TextField dayInput;
    @FXML
    private TextField monthInput;
    @FXML
    private TextField yearInput;

    public String surnameText = null;
    public String firstnameText = null;
    public String cut_date;
    public String cut_name;
    public int dayText;
    public int monthText;
    public int yearText;
    private Connection conn;
    @FXML
    private Button editButton;
    @FXML
    private AnchorPane addAnchor;

    @FXML
    private Rectangle firstnameBox;
    @FXML
    private Rectangle secondnameBox;
    @FXML
    private Rectangle dobBox;

    @FXML
    private Rectangle firstnameRectangle;
    @FXML
    private Rectangle secondnameRectangle;
    @FXML
    private Rectangle dobRectangle;
    @FXML
    private Rectangle dobRectangle2;
    @FXML
    private Rectangle dobRectangle3;

    @FXML
    private Label firstnameLabel;
    @FXML
    private Label secondnameLabel;
    @FXML
    private Label dobLabel;

    public RemoveWindowController() throws SQLException, IOException {
        this.conn = DBConnection.connect();
    }

    Color wrongColor = Color.web("ec4c4c");
    Color standartColor = Color.web("007399");
    Color backgroundColor = Color.web("E0E0E0");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        System.out.println(System.getProperty("user.home"));
        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("user.name"));
        System.out.println(System.getProperty("os.name"));
        System.out.println(System.getProperty("os.arch"));
        System.out.println(System.getProperty("os.version"));

        //https://stackoverflow.com/a/9677746
        /*String myDocuments = null;

        try {
            Process p = Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
            p.waitFor();

            InputStream in = p.getInputStream();
            byte[] b = new byte[in.available()];
            in.read(b);
            in.close();

            myDocuments = new String(b);
            myDocuments = myDocuments.split("\\s\\s+")[4];

        } catch (Throwable t) {
            t.printStackTrace();
        }*/

        //System.out.println(myDocuments);
        System.out.println(FileSystemView.getFileSystemView().getDefaultDirectory().getPath().toString());
        

        Font.loadFont(RemoveWindowController.class.getResource("/opendiabetesvaultgui/stylesheets/fonts/Roboto-Regular.ttf").toExternalForm(), 50);

        //Stage stage = (Stage) addAnchor.getScene().getWindow();
        //Scene addScene = addAnchor.getScene();
        //addScene.getStylesheets().add("/opendiabetesVaultGui/shapes/addentryStyle.css");
        surnameText = lastnameInput.getText();
        firstnameText = firstnameInput.getText();

        firstnameInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String temp,
                    String input) {
                if (input.length() > 50) {
                    cut_name = input.substring(0, 50);
                    firstnameInput.setText(cut_name);
                }
            }
        });

        lastnameInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String temp,
                    String input) {
                if (input.length() > 50) {
                    cut_name = input.substring(0, 50);
                    lastnameInput.setText(cut_name);
                }
            }
        });

        dayInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String temp,
                    String input) {
                if ((!input.matches("\\d"))) {
                    dayInput.setText(input.replaceAll("[^\\d]", ""));
                }

                if ((input.length() > 2)) {
                    cut_date = input.substring(0, 2);
                    dayInput.setText(cut_date);
                }
            }
        });

        monthInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String temp,
                    String input) {
                if (!input.matches("\\d*")) {
                    monthInput.setText(input.replaceAll("[^\\d]", ""));
                }
                if ((input.length() > 2)) {
                    cut_date = input.substring(0, 2);
                    monthInput.setText(cut_date);
                }

            }
        });

        yearInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String temp,
                    String input) {
                if (!input.matches("\\d*")) {
                    yearInput.setText(input.replaceAll("[^\\d]", ""));
                }

                if ((input.length() > 4)) {
                    cut_date = input.substring(0, 4);
                    yearInput.setText(cut_date);
                }
            }
        });

    }

    @FXML
    public void listenToText(ActionEvent event) {
        //System.out.println("YOU HIT ENTER!!");
        getInfo(event);
    }

    public void getInfo(ActionEvent event) {
        if ((!(firstnameInput.getText().isEmpty()))
                && (!(lastnameInput.getText().isEmpty()))
                && (!(dayInput.getText().isEmpty()))
                && (!(monthInput.getText().isEmpty()))
                && (!(yearInput.getText().isEmpty()))
                && (yearInput.getText().length() == 4)) {
            try {

                if (dayInput.getText().length() == 1) {
                    dayInput.setText("0" + dayInput.getText());
                }
                if (monthInput.getText().length() == 1) {
                    monthInput.setText("0" + monthInput.getText());
                }
                Statement stmt = conn.createStatement();
                String query = "INSERT INTO PATIENT (surname, firstname, dob)" + System.lineSeparator()
                        + "VALUES('" + lastnameInput.getText() + "', '" + firstnameInput.getText() + "', '" + yearInput.getText() + "-" + monthInput.getText() + "-" + dayInput.getText() + "');";
                //System.out.print(query);

                ResultSet rs = stmt.executeQuery(query);

                ((Node) (event.getSource())).getScene().getWindow().hide();

            } catch (SQLException e) {
                System.out.println("Error on adding Entry");
            }
        } else {
            /*
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("OpenDiabetesVault");
            alert.setContentText("Please enter valid Info first");
            
            //Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            //stage.getIcons().add(new Image("/shapes/logo_nude.png"));
            
            alert.showAndWait();
             */

            if (firstnameInput.getText().isEmpty()) {
                //Set Color scheme to red for missing Input
                firstnameLabel.setTextFill(wrongColor);
                //firstnameLabel.setText("Please enter a first name to continue");

                firstnameRectangle.setFill(wrongColor);
                firstnameRectangle.setStroke(wrongColor);

                firstnameBox.setStroke(wrongColor);
            } else {
                // Set it back to Standart Colorscheme when Input is present
                firstnameLabel.setTextFill(standartColor);

                firstnameRectangle.setFill(standartColor);
                firstnameRectangle.setStroke(standartColor);

                firstnameBox.setStroke(backgroundColor);
            }
            if (lastnameInput.getText().isEmpty()) {
                secondnameLabel.setTextFill(wrongColor);

                secondnameRectangle.setFill(wrongColor);
                secondnameRectangle.setStroke(wrongColor);

                secondnameBox.setStroke(wrongColor);
            } else {
                secondnameLabel.setTextFill(standartColor);

                secondnameRectangle.setFill(standartColor);
                secondnameRectangle.setStroke(standartColor);

                secondnameBox.setStroke(backgroundColor);
            }
            if ((dayInput.getText().isEmpty()) || (monthInput.getText().isEmpty()) || (yearInput.getText().isEmpty())) {
                dobLabel.setTextFill(wrongColor);

                dobRectangle.setFill(wrongColor);
                dobRectangle.setStroke(wrongColor);
                dobRectangle2.setFill(wrongColor);
                dobRectangle2.setStroke(wrongColor);
                dobRectangle3.setFill(wrongColor);
                dobRectangle3.setStroke(wrongColor);

                dobBox.setStroke(wrongColor);

            } else {
                dobLabel.setTextFill(standartColor);

                dobRectangle.setFill(standartColor);
                dobRectangle.setStroke(standartColor);
                dobRectangle2.setFill(standartColor);
                dobRectangle2.setStroke(standartColor);
                dobRectangle3.setFill(standartColor);
                dobRectangle3.setStroke(standartColor);

                dobBox.setStroke(backgroundColor);
            }
        }

    }

    public Connection getConnection(Connection conn) {
        this.conn = conn;
        return conn;
    }

    public ObservableList returnData() {
        return null;
    }

}
