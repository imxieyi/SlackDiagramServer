package org.slackdiagram.server.model;

import org.json.JSONObject;
import org.slackdiagram.server.util.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class LiteMessage {
    public long timestamp;
    public String user;
    public String text;

    public static ArrayList<LiteMessage> range(String team, String channel, long from, long to, int length, int offset) {
        ArrayList<LiteMessage> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DBHelper.getConnection();
            String sql;
            sql = "select timestamp, user, text from message where " +
                    "team = ? and channel = ? and " +
                    "timestamp >= date_add('1970-01-01', interval ? second) and timestamp <= date_add('1970-01-01', interval ? second) " +
                    "order by timestamp desc limit ? offset ?";
            stat = conn.prepareStatement(sql);
            stat.setString(1, team);
            stat.setString(2, channel);
            stat.setLong(3, from);
            stat.setLong(4, to);
            stat.setInt(5, length);
            stat.setInt(6, offset);
            ResultSet rs = stat.executeQuery();
            while(rs.next()) {
                LiteMessage lm = new LiteMessage();
                lm.user = rs.getString("user");
                lm.text = rs.getString("text");
                lm.timestamp = rs.getTimestamp("timestamp").getTime() / 1000;
                result.add(lm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.close(conn, stat);
        }
        return result;
    }

    public static int count(String team, String channel, long from, long to) {
        int result = 0;
        try {
            Connection conn = DBHelper.getConnection();
            String sql;
            PreparedStatement stat;
            sql = "select count(*) from message where " +
                    "team = ? and channel = ? and " +
                    "timestamp >= date_add('1970-01-01', interval ? second) and timestamp <= date_add('1970-01-01', interval ? second)";
            stat = conn.prepareStatement(sql);
            stat.setString(1, team);
            stat.setString(2, channel);
            stat.setLong(3, from);
            stat.setLong(4, to);
            ResultSet rs = stat.executeQuery();
            while(rs.next()) {
                result = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public JSONObject toJSON() {
        return new JSONObject(this, new String[]{"timestamp", "user", "text"});
    }

    @Override
    public String toString() {
        return String.format("User: %s", user);
    }
}
