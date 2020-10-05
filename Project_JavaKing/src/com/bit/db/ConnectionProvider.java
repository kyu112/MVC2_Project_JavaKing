package com.bit.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectionProvider {

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("close ���ܹ߻�:" + e.getMessage());
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            String driver = "oracle.jdbc.driver.OracleDriver";
            String url = "jdbc:oracle:thin:@192.168.0.33:1521:XE";
            String user = "C##madang";
            String pwd = "madang";

            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pwd);

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("getConn ���ܹ߻�:" + e.getMessage());
        }
        return conn;
    }
}
