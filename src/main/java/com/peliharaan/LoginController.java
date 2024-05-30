package com.peliharaan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


public class LoginController {

    Connection connection = DB.connectDB();

    static Stage loginState;

    @FXML
    private Button btnLogin;

    @FXML
    private Hyperlink txtDaftar;
    
    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUser;

    @FXML
    private void Daftar(ActionEvent event) throws IOException {
        App.setRoot("register");
    }

    @FXML
    private void Login(ActionEvent event) throws IOException {
        String user = txtUser.getText();
        String pass = txtPassword.getText();
        
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM akun WHERE user=? AND password=?");
            statement.setString(1, user);
            statement.setString(2, pass);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Akun.uid = resultSet.getInt("id_akun");
                Akun.nama = resultSet.getString("nama");

                Tools.AlertNotifINFORMATION("Notifikasi", "Login", "Login Berhasil");

                App.setRoot("Home");
            }
            else{
                Tools.AlertNotif("Pemeberitahuan", "error", "error");
            }
        }catch(SQLException e){
            // Tools.AlertNotif("Pemeberitahuan", "error", "error");
            e.printStackTrace();
        }
    }

}
