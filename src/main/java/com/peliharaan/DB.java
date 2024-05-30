package com.peliharaan;

import java.sql.*;

public class DB {
    static Connection con;
    public static Connection connectDB() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tugas_pbo","root","");
            return con;
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
}
