package com.peliharaan;

import java.util.ResourceBundle;
import java.sql.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class FormHewanController implements Initializable {
    Connection connection = DB.connectDB();

    @FXML
    private Button btnGambar;

    @FXML
    private Button btnTambah;

    @FXML
    private ComboBox<String> cbKelompok;

    @FXML
    private ImageView image;

    @FXML
    private TextField txtNama;

    @FXML
    private TextField txtRas;

    @FXML
    private TextField txtUmur;

    File currentFile = null;
    byte[] imageBytes;
    int ids;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        btnTambah.setText("Tambah");
        cbKelompok.getItems().addAll("Kucing","Anjing","Kelinci");
        btnGambar.setOnAction(actionEvent->getImage());
    }

    public void setForm(int id){
        btnTambah.setText("Ubah");
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM hewan WHERE id_hewan="+id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                ids = id;
                txtNama.setText(resultSet.getString("nama"));
                cbKelompok.setValue(resultSet.getString("kelompok"));
                txtRas.setText(resultSet.getString("ras"));
                txtUmur.setText(resultSet.getString("umur"));
                imageBytes = resultSet.getBytes("foto");
                InputStream is = new ByteArrayInputStream(imageBytes);
                Image img = new Image(is);
                image.setImage(img);
                
            }
        }
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void getImage() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.JPG *.PNG *.jpg *.png)", "*.JPG", "jpg files (*.jpg)", "*.jpg", "PNG files (*.PNG)", "*.PNG", "png files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                Image img = new Image(file.toURI().toString());
                image.setImage(img);
                currentFile = file;
                imageBytes = FileToByteArray(file);
                
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    @FXML
private void Tambah(ActionEvent actionEvent){
    try {
        if(!txtNama.getText().trim().isEmpty() && !txtRas.getText().trim().isEmpty()
            && !txtUmur.getText().trim().isEmpty() && !cbKelompok.getValue().trim().isEmpty() && (currentFile != null||imageBytes !=null)){
            if(btnTambah.getText().equals("Tambah")){
                PreparedStatement statement = connection.prepareStatement("INSERT INTO hewan VALUES (NULL, ?, ?, ?, ?, ?, ?)");
                statement.setString(1, txtNama.getText());
                statement.setString(2, cbKelompok.getValue());
                statement.setString(3, txtRas.getText());
                statement.setInt(4, Integer.parseInt(txtUmur.getText()));
                statement.setString(5, "Belum Diadopsi");
                InputStream is = new ByteArrayInputStream(FileToByteArray(currentFile));
                statement.setBlob(6, is);
                statement.executeUpdate();
                Tools.AlertNotifINFORMATION("Tambah Data", "Data Ditambahkan", "Berhasil");
                Tools.CloseMeStage(actionEvent);
                HomeController homeController =new HomeController();
                homeController.getInstance().getRefresh();
            }
            else if(btnTambah.getText().equals("Ubah")){
                PreparedStatement statement = connection.prepareStatement("UPDATE hewan SET nama = ?, kelompok = ?, ras = ?, umur = ?, foto = ? WHERE id_hewan = ?");
                statement.setString(1, txtNama.getText());
                statement.setString(2, cbKelompok.getValue());
                statement.setString(3, txtRas.getText());
                statement.setInt(4, Integer.parseInt(txtUmur.getText()));
                InputStream is = new ByteArrayInputStream(imageBytes);
                statement.setBlob(5, is);
                statement.setInt(6, ids);
                statement.executeUpdate();
                Tools.AlertNotifINFORMATION("Tambah Data", "Data Ditambahkan", "Berhasil");
                Tools.CloseMeStage(actionEvent);
                HomeController homeController =new HomeController();
                homeController.getInstance().getRefresh();
            }
        }
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}

    private byte[] FileToByteArray(File file) throws Exception {
        InputStream is = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        is.read(buffer);
        is.close();
        return buffer;
    }
}