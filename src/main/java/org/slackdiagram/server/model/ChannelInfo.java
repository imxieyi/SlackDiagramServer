package org.slackdiagram.server.model;

import org.json.JSONObject;
import org.slackdiagram.server.util.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ChannelInfo {
    public int accounts;
    public int messages;
    public int relations;
    public long earliest;

    public static ChannelInfo getAll(String team, String channel, long from, long to) {
        ChannelInfo result = new ChannelInfo();
        result.accounts = accounts(team, channel, from, to);
        result.messages = messages(team, channel, from, to);
        result.relations = relations(team, channel, from, to);
        result.earliest = earliest(team, channel);
        return result;
    }

    private static int accounts(String team, String channel, long from, long to) {
        int result = -1;
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DBHelper.getConnection();
            String sql;
            if(channel != null && channel.length() > 0) {
                sql = "select count(distinct user) cnt from message where " +
                        "team = ? and channel = ? and " +
                        "timestamp >= date_add('1970-01-01', interval ? second) and timestamp <= date_add('1970-01-01', interval ? second) ";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setString(2, channel);
                stat.setLong(3, from);
                stat.setLong(4, to);
            } else {
                sql = "select count(distinct user) cnt from message where " +
                        "team = ? and " +
                        "timestamp >= date_add('1970-01-01', interval ? second) and timestamp <= date_add('1970-01-01', interval ? second) ";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setLong(2, from);
                stat.setLong(3, to);
            }
            ResultSet rs = stat.executeQuery();
            while(rs.next()) {
                result = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.close(conn, stat);
        }
        return result;
    }

    private static int messages(String team, String channel, long from, long to) {
        int result = -1;
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DBHelper.getConnection();
            String sql;
            if(channel != null && channel.length() > 0) {
                sql = "select count(*) cnt from message where " +
                        "team = ? and channel = ? and " +
                        "timestamp >= date_add('1970-01-01', interval ? second) and timestamp <= date_add('1970-01-01', interval ? second) ";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setString(2, channel);
                stat.setLong(3, from);
                stat.setLong(4, to);
            } else {
                sql = "select count(*) cnt from message where " +
                        "team = ? and " +
                        "timestamp >= date_add('1970-01-01', interval ? second) and timestamp <= date_add('1970-01-01', interval ? second) ";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setLong(2, from);
                stat.setLong(3, to);
            }
            ResultSet rs = stat.executeQuery();
            while(rs.next()) {
                result = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.close(conn, stat);
        }
        return result;
    }

    private static int relations(String team, String channel, long from, long to) {
        int result = -1;
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DBHelper.getConnection();
            String sql;
            if(channel != null && channel.length() > 0) {
                sql = "select count(*) cnt from mention where " +
                        "team = ? and channel = ? and " +
                        "timestamp >= date_add('1970-01-01', interval ? second) and timestamp <= date_add('1970-01-01', interval ? second) ";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setString(2, channel);
                stat.setLong(3, from);
                stat.setLong(4, to);
            } else {
                sql = "select count(*) cnt from mention where " +
                        "team = ? and " +
                        "timestamp >= date_add('1970-01-01', interval ? second) and timestamp <= date_add('1970-01-01', interval ? second) ";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setLong(2, from);
                stat.setLong(3, to);
            }
            ResultSet rs = stat.executeQuery();
            while(rs.next()) {
                result = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.close(conn, stat);
        }
        return result;
    }

    private static long earliest(String team, String channel) {
        long result = -1;
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DBHelper.getConnection();
            String sql;
            if(channel != null && channel.length() > 0) {
                sql = "select timestamp from message where " +
                        "team = ? and channel = ? " +
                        "order by timestamp asc limit 1";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setString(2, channel);
            } else {
                sql = "select timestamp from message where " +
                        "team = ? " +
                        "order by timestamp asc limit 1";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
            }
            ResultSet rs = stat.executeQuery();
            while(rs.next()) {
                result = rs.getTimestamp("timestamp").getTime() / 1000;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.close(conn, stat);
        }
        return result;
    }

    public JSONObject toJSON() {
        return new JSONObject(this, new String[]{"accounts", "messages", "relations", "earliest"});
    }

}
