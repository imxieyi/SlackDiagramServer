package org.slackdiagram.server.model;

import ch.qos.logback.core.db.dialect.DBUtil;
import org.json.JSONObject;
import org.slackdiagram.server.util.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Channel {
    public String id;
    public String name;
    public String team;
    public boolean is_channel;
    public boolean is_archived;
    public boolean is_general;
    public boolean is_group;
    public boolean is_starred;
    public boolean is_member;
    public String purpose;
    public int num_members;

    public static ArrayList<Channel> all(String team, boolean available) {
        ArrayList<Channel> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DBHelper.getConnection();
            String sql;
            if(available) {
                sql = "select * from channel c join (select distinct channel from mention where team = ?) cc on c.id = cc.channel";
            } else {
                sql = "select * from channel where team = ?";
            }
            stat = conn.prepareStatement(sql);
            stat.setString(1, team);
            ResultSet rs = stat.executeQuery();
            while(rs.next()) {
                Channel c = new Channel();
                c.id = rs.getString("id");
                c.name = rs.getString("name");
                c.team = rs.getString("team");
                c.is_channel = rs.getBoolean("is_channel");
                c.is_archived = rs.getBoolean("is_archived");
                c.is_general = rs.getBoolean("is_general");
                c.is_group = rs.getBoolean("is_group");
                c.is_starred = rs.getBoolean("is_starred");
                c.is_member = rs.getBoolean("is_member");
                c.purpose = rs.getString("purpose");
                c.num_members = rs.getInt("num_members");
                result.add(c);
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

    public static boolean check(String team, String channel) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DBHelper.getConnection();
            String sql;
            if(team != null && team.length() > 0 && channel != null && channel.length() > 0) {
                sql = "select * from channel where team = ? and id = ?";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setString(2, channel);
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

    public JSONObject toJSON() {
        return new JSONObject(this, new String[]{"id", "name", "purpose", "num_members"});
    }

    @Override
    public String toString() {
        return String.format("Channel: %s", id);
    }

}
