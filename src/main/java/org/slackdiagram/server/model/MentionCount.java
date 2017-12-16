package org.slackdiagram.server.model;

import org.json.JSONObject;
import org.slackdiagram.server.util.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MentionCount {
    public String from_user;
    public String to_user;
    public int count;

    public static ArrayList<MentionCount> range(String team, String channel, long from, long to) {
        ArrayList<MentionCount> result = new ArrayList<>();
        try {
            Connection conn = DBHelper.getConnection();
            String sql;
            PreparedStatement stat;
            if(channel != null && channel.length() > 0) {
                sql = "select *, count(*) cnt from mention where " +
                        "team = ? and channel = ? and " +
                        "timestamp >= date_add('1970-01-01', interval ? second) and timestamp <= date_add('1970-01-01', interval ? second) " +
                        "group by team, channel, from_user order by cnt desc";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setString(2, channel);
                stat.setLong(3, from);
                stat.setLong(4, to);
            } else {
                sql = "select *, count(*) cnt from mention where " +
                        "team = ? and timestamp >= date_add('1970-01-01', interval ? second) and timestamp <= date_add('1970-01-01', interval ? second) " +
                        "group by team, from_user order by cnt desc";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setLong(2, from);
                stat.setLong(3, to);
            }
            ResultSet rs = stat.executeQuery();
            while(rs.next()) {
                MentionCount mc = new MentionCount();
                mc.from_user = rs.getString("from_user");
                mc.to_user = rs.getString("to_user");
                mc.count = rs.getInt("cnt");
                result.add(mc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public JSONObject toJSON() {
        return new JSONObject(this, new String[]{"from_user", "to_user", "count"});
    }

    @Override
    public String toString() {
        return String.format("From: %s\tTo: %s", from_user, to_user);
    }
}
