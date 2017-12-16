package org.slackdiagram.server.model;

public class Mention {
    public long timestamp;// In microseconds
    public String team;
    public String channel;
    public String from_user;
    public String to_user;

    @Override
    public String toString() {
        return String.format("Team: %s\tChannel: %s\tFrom: %s\tTo: %s", team, channel, from_user, to_user);
    }
}
