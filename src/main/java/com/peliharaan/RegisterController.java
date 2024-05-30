package com.peliharaan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.sql.*;


import java.io.IOException;

public class RegisterController {

    Connection connection = DB.connectDB();

    @FXML
    private Button btnDaftar;

    @FXML
    private Button btnKembali;

    @FXML
    private TextField txtHP;

    @FXML
    private TextField txtNama;

    @FXML
    private TextField txtPass;

    @FXML
    private TextField txtUser;

    @FXML
    private void Daftar(ActionEvent event) throws IOException {
        String username = txtUser.getText();
        String password = txtPass.getText();
        String nama = txtNama.getText();
        String hp = txtHP.getText();
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM akun WHERE user=?");
            statement.setString(1, username);
            ResultSet resultset = statement.executeQuery();
            if(!resultset.next()){
                statement = connection.prepareStatement("INSERT INTO akun values (NULL, ?, ?, ?, ?)");
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, nama);
                statement.setString(4, hp);
                statement.executeUpdate();

                Tools.AlertNotifINFORMATION("Notifikasi", "Registrasi", "Registrasi Berhasil");
            }
            resultset.close();
            App.setRoot("login");

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void Kembali(ActionEvent event) throws IOException {
        App.setRoot("login");
    }

}
