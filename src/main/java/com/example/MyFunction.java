package com.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.h2.tools.SimpleResultSet;

public class MyFunction {
    public static ResultSet getMatrix(Connection conn, Integer size)
            throws SQLException {
        SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("X", Types.INTEGER, 10, 0);
        rs.addColumn("Y", Types.INTEGER, 10, 0);
        String url = conn.getMetaData().getURL();
        if (url.equals("jdbc:columnlist:connection")) {
            return rs;
        }
        for (int s = size.intValue(), x = 0; x < s; x++) {
            for (int y = 0; y < s; y++) {
                rs.addRow(x, y);
            }
        }
        return rs;
    }
}
