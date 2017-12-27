package org.slackdiagram.server.util;

import com.mysql.cj.api.mysqla.result.Resultset;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBHelper {

    private static Logger log = Logger.getLogger(DBHelper.class.getName());

    // utf8mb4: https://stackoverflow.com/questions/24389862/mysql-connectorj-character-set-results-does-not-support-utf8mb4

    private static String host;
    private static String port;
    private static String schema;
    private static String user;
    private static String password;
    private static String url;

    public static void init() throws IOException {
        Properties config = new Properties();
        File configFile = new File("config.properties");
        if(!configFile.exists()) {
            throw new FileNotFoundException("config.properties not found!");
        }
        config.load(new FileInputStream(configFile));
        host = config.getProperty("db_host");
        port = config.getProperty("db_port");
        schema = config.getProperty("db_schema");
        user = config.getProperty("db_user");
        password = config.getProperty("db_password");
        url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC&useSSL=true", host, port, schema);
        log.info(String.format("Database host: %s, port: %s, schema: %s", host, port, schema));
    }

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }

    public static void close(Connection conn, Statement stat) {
        try {
            if(stat != null && !stat.isClosed()) {
                stat.close();
            }
        } catch (SQLException ignored) {
        } finally {
            try {
                if(conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ignored) {
            }
        }
    }

}
