package org.slackdiagram.server.model;

public class Message {
    public long timestamp;// In microseconds
    public String team;
    public String channel;
    public String user;
    public String text;

    @Override
    public String toString() {
        return String.format("Team: %s\tChannel: %s\tUser: %s", team, channel, user);
    }
}
