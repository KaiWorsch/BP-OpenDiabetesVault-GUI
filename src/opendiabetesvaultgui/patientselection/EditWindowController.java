/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.patientselection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import opendiabetesvaultgui.patientselection.PatientSelectionController;
import opendiabetesvaultgui.launcher.FatherController;
import static opendiabetesvaultgui.launcher.FatherController.PREFS_FOR_ALL;

/**
 * FXML Controller class
 *
 * @author schae_000
 */
public class EditWindowController implements Initializable {

    @FXML
    private TextField firstnameInput;
    @FXML
    private Button editButton;
    @FXML
    private TextField lastnameInput;
    @FXML
    private TextField dayInput;
    @FXML
    private TextField monthInput;
    @FXML
    private TextField yearInput;

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

    Color wrongColor = Color.web("ec4c4c");
    Color standartColor = Color.web("007399");
    Color backgroundColor = Color.web("E0E0E0");

    private ObservableList inputList = PatientSelectionController.getNameList();
    private Boolean editBool = PatientSelectionController.isEdit();
    private String date;
    private Connection conn;

    private String oldDay;
    private String oldMonth;
    private String oldYear;

    private String cut_name;
    private String cut_day;
    private String cut_month;
    private String cut_year;

    private ResourceBundle resource;

    public String surnameText = null;
    public String firstnameText = null;
    public String cut_date;
    public int dayText;
    public int monthText;
    public int yearText;

    public EditWindowController() throws SQLException, IOException {
        this.conn = DBConnection.connect();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        resource = rb;

        if (editBool == true) {

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
                    if (!input.matches("\\d*")) {
                        dayInput.setText(input.replaceAll("[^\\d]", ""));
                    }
                    if ((input.length() > 2)) {
                        cut_day = input.substring(0, 2);
                        dayInput.setText(cut_day);
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
                        cut_month = input.substring(0, 2);
                        dayInput.setText(cut_month);
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
                        cut_year = input.substring(0, 4);
                        yearInput.setText(cut_year);
                    }
                }
            });

            if ("yyyy-MM-dd".equals(PREFS_FOR_ALL.get("dateFormat", ""))) {
                date = (String) inputList.get(3);
            } else if ("dd.MM.yyyy".equals(PREFS_FOR_ALL.get("dateFormat", ""))) {
                Date inputDate = null;
                try {
                    inputDate = new SimpleDateFormat("dd.MM.yyyy").parse((String) inputList.get(3));
                } catch (ParseException ex) {
                    Logger.getLogger(EditWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
                date = new SimpleDateFormat("yyyy-MM-dd").format(inputDate);
            }

            StringBuilder year = new StringBuilder(3);
            for (int i = 0; i <= 3; i++) {
                if (date.charAt(i) != '-') {
                    year.append(date.charAt(i));
                }
            }

            StringBuilder month = new StringBuilder(1);
            for (int i = 4; i <= 7; i++) {
                if (date.charAt(i) != '-') {
                    month.append(date.charAt(i));
                }
            }

            StringBuilder day = new StringBuilder(1);
            for (int i = 7; i < date.length(); i++) {
                if (date.charAt(i) != '-') {
                    day.append(date.charAt(i));
                }
            }

            oldDay = day.toString();
            oldMonth = month.toString();
            oldYear = year.toString();
            firstnameInput.setText((String) inputList.get(2));
            lastnameInput.setText((String) inputList.get(1));
            dayInput.setText(day.toString());
            monthInput.setText(month.toString());
            yearInput.setText(year.toString());

            //ADDING ENTRY
        } else if (editBool == false) {

            editButton.setText(resource.getString("edit.addButton"));

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

    }

    @FXML
    private void listenToText(ActionEvent event) {
        alterInfo(event);
    }

    /**
     * Checks, wheter User Input is valid. If so, database info will be altered.
     * If not, the user gets visual feedback in gui
     *
     * @param event
     * @throws SQLException
     *
     */
    @FXML
    private void alterInfo(ActionEvent event) {
        if (editBool == true) {
            if ((!(firstnameInput.getText().isEmpty())) && (!(lastnameInput.getText().isEmpty())) && (!(dayInput.getText().isEmpty())) && (!(monthInput.getText().isEmpty())) && (!(yearInput.getText().isEmpty())) && (isDateValid(yearInput.getText() + "-" + monthInput.getText() + "-" + dayInput.getText()) == true)) {
                try {

                    if (dayInput.getText().length() == 1) {
                        dayInput.setText("0" + dayInput.getText());
                    }
                    if (monthInput.getText().length() == 1) {
                        monthInput.setText("0" + monthInput.getText());
                    }
                    Statement stmt = conn.createStatement();
                    /*String query = "UPDATE PATIENT\n"
                            + "Set surname='" + lastnameInput.getText() + "', firstname='" + firstnameInput.getText() + "', dob='" + yearInput.getText() + "-" + monthInput.getText() + "-" + dayInput.getText() + "'\n"
                            + "WHERE surname='" + inputList.get(0) + "'\n"
                            + "AND firstname='" + inputList.get(1) + "'\n"
                            + "AND dob='" + oldYear + "-" + oldMonth + "-" + oldDay + "';";*/
                    String query = "UPDATE PATIENT\n"
                            + "SET surname='" + lastnameInput.getText() + "', firstname='" + firstnameInput.getText() + "', dob='" + yearInput.getText() + "-" + monthInput.getText() + "-" + dayInput.getText() + "'\n"
                            + "WHERE id='" + inputList.get(0) + "';";
                    //System.out.print(query);
                    ResultSet rs = stmt.executeQuery(query);
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } catch (SQLException e) {
                    System.out.println("Error on adding Entry");
                }
            } else {
                /*Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("OpenDiabetesVault");
            alert.setContentText("Please enter valid Info first");
            
            alert.showAndWait();*/

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
                if ((dayInput.getText().isEmpty()) || (monthInput.getText().isEmpty()) || (yearInput.getText().isEmpty()) || (isDateValid(yearInput.getText() + "-" + monthInput.getText() + "-" + dayInput.getText()) == false)) {
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
        } else if (editBool == false) {
            if ((!(firstnameInput.getText().isEmpty()))
                    && (!(lastnameInput.getText().isEmpty()))
                    && (!(dayInput.getText().isEmpty()))
                    && (!(monthInput.getText().isEmpty()))
                    && (!(yearInput.getText().isEmpty()))
                    && (yearInput.getText().length() == 4)
                    && (isDateValid(yearInput.getText() + "-" + monthInput.getText() + "-" + dayInput.getText()) == true)) {
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
                if ((dayInput.getText().isEmpty()) || (monthInput.getText().isEmpty()) || (yearInput.getText().isEmpty()) || (isDateValid(yearInput.getText() + "-" + monthInput.getText() + "-" + dayInput.getText()) == false)) {
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
    }

    /**
     * Returns true when inputdate is a valid date Otherwise false
     *
     * @param dateInput Current date that was given by user input
     * @return Boolean
     */
    public boolean isDateValid(String dateInput) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        try {
            sdf.parse(dateInput.trim());
        } catch (ParseException p) {
            return false;
        }
        return true;
    }

}
