package org.slackdiagram.server.model;

import org.json.JSONObject;
import org.slackdiagram.server.util.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MentionMessage {
    public long timestamp;
    public String user;
    public String text;

    public static ArrayList<MentionMessage> both(String team, String channel, long from, long to, String user1, String user2) {
        ArrayList<MentionMessage> all = new ArrayList<>();
        all.addAll(range(team, channel, from, to, user1, user2));
        all.addAll(range(team, channel, from, to, user2, user1));
        return all;
    }

    private static ArrayList<MentionMessage> range(String team, String channel, long from, long to, String fromuser, String touser) {
        ArrayList<MentionMessage> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DBHelper.getConnection();
            String sql;
            if(channel != null && channel.length() > 0) {
                sql = "select * from message ms join\n" +
                        "(SELECT timestamp FROM mention\n" +
                        "WHERE team = ? and channel = ? AND\n" +
                        "  from_user = ? and to_user = ? and\n" +
                        "      TIMESTAMP >= date_add('1970-01-01', INTERVAL ? SECOND ) AND\n" +
                        "      TIMESTAMP <= date_add('1970-01-01', INTERVAL ? SECOND )) mt\n" +
                        "on ms.timestamp = mt.timestamp\n" +
                        "where ms.team = ? and ms.channel = ? and ms.user = ? " +
                        "order by ms.timestamp desc";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setString(2, channel);
                stat.setString(3, fromuser);
                stat.setString(4, touser);
                stat.setLong(5, from);
                stat.setLong(6, to);
                stat.setString(7, team);
                stat.setString(8, channel);
                stat.setString(9, fromuser);
            } else {
                sql = "select * from message ms join\n" +
                        "(SELECT timestamp FROM mention\n" +
                        "WHERE team = ? AND\n" +
                        "  from_user = ? and to_user = ? and\n" +
                        "      TIMESTAMP >= date_add('1970-01-01', INTERVAL ? SECOND ) AND\n" +
                        "      TIMESTAMP <= date_add('1970-01-01', INTERVAL ? SECOND )) mt\n" +
                        "on ms.timestamp = mt.timestamp\n" +
                        "where ms.team = ? and ms.user = ? " +
                        "order by ms.timestamp desc";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setString(2, fromuser);
                stat.setString(3, touser);
                stat.setLong(4, from);
                stat.setLong(5, to);
                stat.setString(6, team);
                stat.setString(7, fromuser);
            }
            ResultSet rs = stat.executeQuery();
            while(rs.next()) {
                MentionMessage mm = new MentionMessage();
                mm.timestamp = rs.getTimestamp("timestamp").getTime() / 1000;
                mm.user = rs.getString("user");
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
        return new JSONObject(this, new String[]{"timestamp", "user", "text"});
    }
}
