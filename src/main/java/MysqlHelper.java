package main.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by zhangbo on 2017/8/14.
 */
public class MysqlHelper {
    private static final String url = "jdbc:mysql://200.1.3.199/zq_cn";
    private static final String name = "com.mysql.jdbc.Driver";
    private static final String user = "root";
    private static final String password = "91test.com";

    public Connection conn = null;
    public PreparedStatement pst = null;

    public MysqlHelper(String sql) {
        try {
            Class.forName(name);//指定连接类型
            conn = DriverManager.getConnection(url, user, password);//获取连接
            pst = conn.prepareStatement(sql);//准备执行语句
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if(pst != null)
                this.pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(conn != null)
                this.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
