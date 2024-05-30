package com.peliharaan;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HewanController {

    @FXML
    private HBox HboxBtn;

    @FXML
    private Button btnHapus;

    @FXML
    private Button btnUbah;

    @FXML
    private CheckBox chkBox;

    @FXML
    private ImageView image;

    @FXML
    private Label lblAdopsi;

    @FXML
    private Label lblNama;

    @FXML
    private Label lblRas;

    @FXML
    private Label lblUmur;

    void Hapus(int id) {
        Connection connection = DB.connectDB();
        if (Tools.AlertNotifCONFIRM("Notifikasi", "Hapus Data",
                "Apakah anda yakin ingin menghapus Hewan berikut : " + lblNama.getText() + "?")) {
            try {
                PreparedStatement statement = connection.prepareStatement("DELETE FROM hewan WHERE id_hewan=" + id);
                statement.executeUpdate();
                HomeController homeController = new HomeController();
                homeController.getInstance().getRefresh();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void Ubah(int id) {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FormHewan.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 673, 546);
            FormHewanController formHewanController = fxmlLoader.getController();
            formHewanController.setForm(id);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    

    private void Checker(int id, String nama) {
        HomeController homeController = new HomeController();
        ArrayList<Integer> list = homeController.getInstance().getListid();
        ArrayList<String> listnama = homeController.getInstance().getListnama();
        if (chkBox.isSelected()) {
            if (!homeController.getInstance().isCheckBoxChecked) {
                if (!list.contains(id) && !listnama.contains(nama)) {
                    list.add(id);
                    listnama.add(nama);
                    homeController.getInstance().isCheckBoxChecked = true;
                }
            }
        } else {
            if (homeController.getInstance().isCheckBoxChecked) {
                list.remove(Integer.valueOf(id));
                listnama.remove(nama);
                homeController.getInstance().isCheckBoxChecked = false;
            }
        }
    }

    public void setData(int id, String nama, String kelompok, String ras, int umur, String adopsi, Blob gambar) {
        if (!Akun.nama.equals("admin")) {
            HboxBtn.getChildren().clear();
        }
        if (!gambar.toString().trim().isEmpty()) {
            try {
                byte[] imageBytes = gambar.getBytes(1, (int) gambar.length());
                InputStream is = new ByteArrayInputStream(imageBytes);
                Image img = new Image(is);
                image.setImage(img);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        lblNama.setText(nama);
        lblRas.setText(ras);
        lblUmur.setText(String.valueOf(umur));
        lblAdopsi.setText(adopsi);

        btnHapus.setOnAction(actionEvent -> Hapus(id));
        btnUbah.setOnAction(actionEvent -> Ubah(id));
        chkBox.setOnAction(actionEvent -> Checker(id, nama));
    }
}