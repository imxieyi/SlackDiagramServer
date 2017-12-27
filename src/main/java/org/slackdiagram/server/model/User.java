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

    public static ArrayList<User> all(String team) {
        ArrayList<User> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DBHelper.getConnection();
            String sql = "select * from user where team = ?";
            stat = conn.prepareStatement(sql);
            stat.setString(1, team);
            ResultSet rs = stat.executeQuery();
            while(rs.next()) {
                User u = new User();
                u.id = rs.getString("id");
                u.name = rs.getString("name");
                u.color = rs.getString("color");
                u.first_name = rs.getString("first_name");
                u.last_name = rs.getString("last_name");
                u.real_name = rs.getString("real_name");
                u.image = retrieveImage(rs, "48");
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

    public JSONObject toJSON() {
        return new JSONObject(this, new String[]{"name", "color", "first_name", "last_name", "real_name", "title", "image"});
    }

    @Override
    public String toString() {
        return String.format("User: %s", id);
    }

}
