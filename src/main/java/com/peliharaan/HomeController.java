package com.peliharaan;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeController implements Initializable {

    Connection connection = DB.connectDB();

    @FXML
    private Button btnKeluar;

    @FXML
    private Button btnTambah;

    @FXML
    private GridPane grid;

    @FXML
    private Label lblAkun;

    @FXML
    private ScrollPane scroll;

    @FXML
    private TextField txtCari;

    ArrayList<Integer> listId = new ArrayList<>();
    ArrayList<String> listnama = new ArrayList<>();
    public ArrayList<Integer> getListid() {
        return listId;
    }

    public ArrayList<String> getListnama() {
        return listnama;
    }
    private static HomeController instance;

    public static HomeController getInstance() {
        return instance;
    }
    public boolean isCheckBoxChecked = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            instance = this;
            Refresh();
            if (!Akun.nama.equals("admin")) {
                btnTambah.setText("Adopsi");
            }
            lblAkun.setText(Akun.nama);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        txtCari.textProperty().addListener((observable, oldValue, newValue)->Cari());

    }

    @FXML
    private void Keluar(ActionEvent event) throws IOException {
        // Handle the exit button click
        App.setRoot("login");
    }

    @FXML
    private void Tambah(ActionEvent event) throws IOException{
        // Handle the add button click
        if(btnTambah.getText().equals("Tambah")){

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FormHewan.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load(),673,546);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        else if (btnTambah.getText().equals("Adopsi")){
            Connection connection = DB.connectDB();
        if (Tools.AlertNotifCONFIRM("Notifikasi", "Hapus Data",
                "Apakah ingin mengadopsi Hewan bernama : "+ listnama.get(0) +"?")) {
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE hewan set adopsi=?WHERE id_hewan=?");
                statement.setString(1, lblAkun.getText());
                statement.setInt(2, listId.get(0));
                statement.executeUpdate();
                HomeController homeController = new HomeController();
                homeController.getInstance().getRefresh();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        }

    }

    private void Refresh() {
        grid.getChildren().clear();
        int column = 0;
        int row = 1;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM hewan");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                try {
                    int id = resultSet.getInt("id_hewan");
                    String nama = resultSet.getString("nama");
                    String kelompok = resultSet.getString("kelompok");
                    String ras = resultSet.getString("ras");
                    int umur = resultSet.getInt("umur");
                    String adopsi = resultSet.getString("adopsi");
                    Blob blob = resultSet.getBlob("foto");

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("Hewan.fxml"));
                    VBox cardBox = fxmlLoader.load();
                    HewanController hewanController = fxmlLoader.getController();
                    hewanController.setData(id, nama, kelompok, ras, umur, adopsi, blob);

                    if (column > 3) {
                        column = 0;
                        row++;
                    }

                    grid.add(cardBox, column, row);
                    column++;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }grid.setHgap(30);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void getRefresh() {
        this.Refresh();
    }

    private void Cari() {
        grid.getChildren().clear();
        int column = 0;
        int row = 1;
        String cari = txtCari.getText();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM hewan WHERE nama Like ?");
            statement.setString(1, "%" + cari + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                try {
                    int id = resultSet.getInt("id_hewan");
                    String nama = resultSet.getString("nama");
                    String kelompok = resultSet.getString("kelompok");
                    String ras = resultSet.getString("ras");
                    int umur = resultSet.getInt("umur");
                    String adopsi = resultSet.getString("adopsi");
                    Blob blob = resultSet.getBlob("foto");

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("Hewan.fxml"));
                    VBox cardBox = fxmlLoader.load();
                    HewanController hewanController = fxmlLoader.getController();
                    hewanController.setData(id, nama, kelompok, ras, umur, adopsi, blob);

                    if (column > 3) {
                        column = 0;
                        row++;
                    }

                    grid.add(cardBox, column, row);
                    column++;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }grid.setHgap(30);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}