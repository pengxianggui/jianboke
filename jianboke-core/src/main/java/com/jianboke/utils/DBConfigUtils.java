package com.jianboke.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created by pengxg on 2017/3/25.
 */
public class DBConfigUtils {


    private static final Logger log = LoggerFactory.getLogger(DBConfigUtils.class);


    public  static Connection getConn(String url, String username, String password){

        Connection conn = null;
        try{

            conn = DriverManager.getConnection(url,username,password);
        }catch (Exception e){
            e.printStackTrace();
        }

        return conn;
    }

    public static void closeConn(Connection conn, Statement stm, ResultSet rs){
        try {
            if(rs != null)
                rs.close();
            if(stm != null)
                stm.close();
            if(conn != null)
                conn.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}
