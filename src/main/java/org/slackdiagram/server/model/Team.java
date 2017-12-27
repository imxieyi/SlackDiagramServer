package org.slackdiagram.server.model;

import org.json.JSONObject;
import org.slackdiagram.server.util.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Team {
    public String id;
    public String domain;
    public String name;
    public boolean is_disabled;
    public boolean is_hidden;

    public static ArrayList<Team> all(String domain, boolean available) {
        ArrayList<Team> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stat = null;
        try {
           conn = DBHelper.getConnection();
            String sql;
            if(domain != null && domain.length() > 0) {
                sql = "select * from team where domain = ?";
                stat = conn.prepareStatement(sql);
                stat.setString(1, domain);
            } else {
                if (available) {
                    sql = "select * from (select distinct team from message) m join team t on t.id = m.team";
                } else {
                    sql = "select * from team";
                }
                stat = conn.prepareStatement(sql);
            }
            ResultSet rs = stat.executeQuery();
            while(rs.next()) {
                Team t = new Team();
                t.id = rs.getString("id");
                t.domain = rs.getString("domain");
                t.name = rs.getString("name");
                t.is_disabled = rs.getBoolean("is_disabled");
                t.is_hidden = rs.getBoolean("is_hidden");
                result.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.close(conn, stat);
        }
        return result;
    }

    public static ArrayList<Team> all(boolean available) {
        return all(null, available);
    }

    public static boolean check(String team) {
        try {
            Connection conn = DBHelper.getConnection();
            String sql;
            PreparedStatement stat;
            if(team != null && team.length() > 0) {
                sql = "select * from team where id = ?";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
            } else {
                conn.close();
                return false;
            }
            ResultSet rs = stat.executeQuery();
            if(rs.next()) {
                rs.close();
                stat.close();
                conn.close();
                return true;
            } else {
                rs.close();
                stat.close();
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public JSONObject toJSON() {
        return new JSONObject(this, new String[]{"id", "domain", "name"});
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

}
