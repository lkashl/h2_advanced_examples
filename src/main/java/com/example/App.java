package com.example;

import java.sql.*;
import java.util.regex.Pattern;

import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.Server;

/**
 * Hello world!
 *
 */

public class App {

    public class SqlCommentStatementInspector implements StatementInspector {

        private final Pattern SQL_COMMENT_PATTERN = Pattern
                .compile(
                        "\\/\\*.*?\\*\\/\\s*");

        @Override
        public String inspect(String sql) {
            System.out.println("Executing SQL query");

            return SQL_COMMENT_PATTERN
                    .matcher(sql)
                    .replaceAll("");
        }
    }
    


    public static void main(String[] args) {
        try {
            Server.createWebServer("-webPort", "9999").start();
            Server.createTcpServer("-ifNotExists", 
            "-tcpAllowOthers", 
            "-webAllowOthers", 
            "-tcpPort", "9998").start();
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:9998/~/h2", "sa", "");

        
            System.out.println("Connection Established: "
                    + conn.getMetaData().getDatabaseProductName() + "/" + conn.getCatalog());

            // STEP 3: Execute a query
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS TEST;";
            stmt.executeUpdate(sql);
            //stmt.executeQuery("SELECT * FROM TEST;");
            stmt.execute("CREATE TRIGGER IF NOT EXISTS my_trigger "+
            "BEFORE SELECT "+
            "ON TEST " +
            "FOR EACH STATEMENT "+ 
            "CALL \"com.example.MyTrigger\"");
            stmt.executeQuery("SELECT * FROM TEST;");
            stmt.execute("CREATE ALIAS IF NOT EXISTS MATRIX FOR \"com.example.MyFunction.getMatrix\";");
            
            Object result = stmt.executeQuery("SELECT * FROM MATRIX(2) ORDER BY X, Y;");
            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
