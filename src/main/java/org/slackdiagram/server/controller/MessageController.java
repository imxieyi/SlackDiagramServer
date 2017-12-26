package org.slackdiagram.server.controller;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slackdiagram.server.model.Channel;
import org.slackdiagram.server.model.LiteMessage;
import org.slackdiagram.server.model.MessageCount;
import org.slackdiagram.server.model.Team;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private Logger log = Logger.getLogger(MessageController.class.getName());

    @RequestMapping(method = RequestMethod.GET)
    public String get(HttpServletRequest req) {
        JSONObject obj = new JSONObject();
        String team = req.getParameter("team");
        String channel = req.getParameter("channel");
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String length = req.getParameter("length");
        String offset = req.getParameter("offset");
        try {
            if(team == null || team.length() <= 0 || channel == null || channel.length() <= 0) {
                obj.put("status", 2);
                obj.put("error", "Illegal request!");
            } else if(!Team.check(team)) {
                // Team does not exist
                obj.put("status", 3);
                obj.put("error", "Team does not exist!");
            } else {
                if(channel != null && channel.length() > 0 && !Channel.check(team, channel)) {
                    obj.put("status", 4);
                    obj.put("error", "Channel does not exist!");
                } else {
                    long fromTime, toTime;
                    if (from != null) {
                        fromTime = Long.parseLong(from);
                    } else {
                        fromTime = 0;
                    }
                    if (to != null) {
                        toTime = Long.parseLong(to);
                    } else {
                        toTime = new Date().getTime() / 1000;
                    }
                    if(length != null && length.length() > 0){
                        if (offset == null) {
                            offset = "0";
                        }
                        for (LiteMessage lm : LiteMessage.range(team, channel, fromTime, toTime, Integer.parseInt(length), Integer.parseInt(offset))) {
                            obj.append("message", lm.toJSON());
                        }
                        if (!obj.has("message")) {
                            obj.put("message", new JSONArray());
                        }
                    }
                    obj.put("total", LiteMessage.count(team, channel, fromTime, toTime));
                    obj.put("status", 0);
                }
            }
        } catch (Exception e) {
            obj.put("status", 1);
            obj.put("error", e.getMessage());
            e.printStackTrace();
        }
        JSONObject father = new JSONObject();
        father.put("code", 20000);
        father.put("data", obj);
        return father.toString();
    }

}
