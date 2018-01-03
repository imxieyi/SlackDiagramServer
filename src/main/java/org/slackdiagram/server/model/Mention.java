package org.slackdiagram.server.model;

import org.slackdiagram.server.util.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Mention {
    public long timestamp;// In microseconds
    public String team;
    public String channel;
    public String from_user;
    public String to_user;

    public static int meetdays(String team, String channel, String from, String to) {
        int result = 0;
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DBHelper.getConnection();
            String sql;
            if(channel != null && channel.length() > 0) {
                sql = "select datediff(current_timestamp, min(timestamp)) diff from mention where team = ? and channel = ? and from_user = ? and to_user = ?";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setString(2, channel);
                stat.setString(3, from);
                stat.setString(4, to);
            } else {
                sql = "select datediff(current_timestamp, min(timestamp)) diff from mention where team = ? and from_user = ? and to_user = ?";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setString(2, from);
                stat.setString(3, to);
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

    @Override
    public String toString() {
        return String.format("Team: %s\tChannel: %s\tFrom: %s\tTo: %s", team, channel, from_user, to_user);
    }
}
