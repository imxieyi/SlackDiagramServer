package org.slackdiagram.server.model;

import org.json.JSONObject;
import org.slackdiagram.server.util.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {
    public String id;
    public String name;
    public String color;
    public String first_name;
    public String last_name;
    public String real_name;
    public String image;
    public String title;

    private static final String[] resolutions = new String[]{"original", "192", "72", "48", "32", "24"};

    private static String retrieveImage(ResultSet rs, String from) throws SQLException {
        int i = 0;
        for(; i<resolutions.length; i++)
            if(resolutions[i].equals(from)) break;
        for(; i<resolutions.length; i++) {
            String link = rs.getString("image_" + resolutions[i]);
            if(link != null && link.length() > 0)
                return link;
        }
        return "";
    }

    public static ArrayList<User> get(String team, String channel) {
        ArrayList<User> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DBHelper.getConnection();
            String sql;
            if(channel != null && channel.length() > 0) {
                sql = "select * from user u join ( " +
                        "select distinct user from message where team = ? and channel = ?) cu " +
                        "on cu.user = u.id";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setString(2, channel);
            } else {
                sql = "select * from user u join ( " +
                        "select distinct user from message where team = ?) cu " +
                        "on cu.user = u.id";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
            }
            ResultSet rs = stat.executeQuery();
            while(rs.next()) {
                User u = new User();
                u.id = rs.getString("id");
                u.name = rs.getString("name");
                u.color = rs.getString("color");
                u.first_name = rs.getString("first_name");
                u.last_name = rs.getString("last_name");
                u.real_name = rs.getString("real_name");
                u.image = retrieveImage(rs, "192");
                u.title = rs.getString("title");
                result.add(u);
            }
            rs.close();
            stat.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.close(conn, stat);
        }
        return result;
    }

    public static boolean check(String user) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DBHelper.getConnection();
            String sql;
            if(user != null && user.length() > 0) {
                sql = "select * from user where id = ?";
                stat = conn.prepareStatement(sql);
                stat.setString(1, user);
            } else {
                conn.close();
                return false;
            }
            ResultSet rs = stat.executeQuery();
            if(rs.next()) {
                DBHelper.close(conn, stat);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.close(conn, stat);
        }
        return false;
    }

    public static int joindays(String team, String channel, String user) {
        int result = 0;
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DBHelper.getConnection();
            String sql;
            if(channel != null && channel.length() > 0) {
                sql = "select datediff(current_timestamp, min(timestamp)) diff from message where team = ? and channel = ? and user = ?";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setString(2, channel);
                stat.setString(3, user);
            } else {
                sql = "select datediff(current_timestamp, min(timestamp)) diff from message where team = ? and user = ?";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setString(2, user);
            }
            ResultSet rs = stat.executeQuery();
            if(rs.next()) {
                result = rs.getInt("diff");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.close(conn, stat);
        }
        return result;
    }

    public JSONObject toJSON() {
        return new JSONObject(this, new String[]{"name", "color", "first_name", "last_name", "real_name", "title", "image"});
    }

    @Override
    public String toString() {
        return String.format("User: %s", id);
    }

}
