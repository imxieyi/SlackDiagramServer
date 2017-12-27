package org.slackdiagram.server.model;

import org.json.JSONObject;
import org.slackdiagram.server.util.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MessageCount {
    public String user;
    public int count;

    public static ArrayList<MessageCount> range(String team, String channel, long from, long to) {
        ArrayList<MessageCount> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DBHelper.getConnection();
            String sql;
            if(channel != null && channel.length() > 0) {
                sql = "select user, count(*) cnt from message where " +
                        "team = ? and channel = ? and " +
                        "timestamp >= date_add('1970-01-01', interval ? second) and timestamp <= date_add('1970-01-01', interval ? second) " +
                        "group by user order by cnt desc";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setString(2, channel);
                stat.setLong(3, from);
                stat.setLong(4, to);
            } else {
                sql = "select channel, user, count(*) cnt from message where " +
                        "team = ? and timestamp >= date_add('1970-01-01', interval ? second) and timestamp <= date_add('1970-01-01', interval ? second) " +
                        "group by channel, user order by cnt desc";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setLong(2, from);
                stat.setLong(3, to);
            }
            ResultSet rs = stat.executeQuery();
            while(rs.next()) {
                MessageCount mc = new MessageCount();
                mc.user = rs.getString("user");
                mc.count = rs.getInt("cnt");
                result.add(mc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.close(conn, stat);
        }
        return result;
    }

    public JSONObject toJSON() {
        return new JSONObject(this, new String[]{"user", "count"});
    }

    @Override
    public String toString() {
        return String.format("User: %s", user);
    }
}
