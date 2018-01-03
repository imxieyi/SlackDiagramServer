package org.slackdiagram.server.model;

import org.json.JSONObject;
import org.slackdiagram.server.util.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UserMessage {
    public long timestamp;
    public String text;

    public static ArrayList<UserMessage> range(String team, String channel, long from, long to, String user, int length, int offset) {
        ArrayList<UserMessage> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DBHelper.getConnection();
            String sql;
            if(channel != null && channel.length() > 0) {
                sql = "select * from message where team = ? and channel = ? and user = ?\n" +
                        "  and TIMESTAMP >= date_add('1970-01-01', INTERVAL ? SECOND )\n" +
                        "  and TIMESTAMP <= date_add('1970-01-01', INTERVAL ? SECOND )\n" +
                        "order by timestamp desc limit ? offset ?";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setString(2, channel);
                stat.setString(3, user);
                stat.setLong(4, from);
                stat.setLong(5, to);
                stat.setInt(6, length);
                stat.setInt(7, offset);
            } else {
                sql = "select * from message where team = ? and user = ?\n" +
                        "  and TIMESTAMP >= date_add('1970-01-01', INTERVAL ? SECOND )\n" +
                        "  and TIMESTAMP <= date_add('1970-01-01', INTERVAL ? SECOND )\n" +
                        "order by timestamp desc limit ? offset ?";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setString(2, user);
                stat.setLong(3, from);
                stat.setLong(4, to);
                stat.setInt(5, length);
                stat.setInt(6, offset);
            }
            ResultSet rs = stat.executeQuery();
            while(rs.next()) {
                UserMessage mm = new UserMessage();
                mm.timestamp = rs.getTimestamp("timestamp").getTime() / 1000;
                mm.text = rs.getString("text");
                result.add(mm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.close(conn, stat);
        }
        return result;
    }

    public JSONObject toJSON() {
        return new JSONObject(this, new String[]{"timestamp", "text"});
    }
}
