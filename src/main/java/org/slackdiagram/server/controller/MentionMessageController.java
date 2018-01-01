package org.slackdiagram.server.controller;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slackdiagram.server.model.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/mention/message")
public class MentionMessageController {

    private Logger log = Logger.getLogger(MentionMessageController.class.getName());

    @RequestMapping(method = RequestMethod.GET)
    public String get(HttpServletRequest req) {
        JSONObject obj = new JSONObject();
        String team = req.getParameter("team");
        String channel = req.getParameter("channel");
        String user1 = req.getParameter("user1");
        String user2 = req.getParameter("user2");
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        try {
            if(team == null || team.length() <= 0 || from == null || from.length() <=0 ||
                    to == null || to.length() <= 0 || user1 == null || user1.length() <= 0 || user2 == null || user2.length() <= 0) {
                obj.put("code", 400);
                obj.put("error", "Illegal request!");
            } else if(!Team.check(team)) {
                // Team does not exist
                obj.put("code", 400);
                obj.put("error", "Team does not exist!");
            } else {
                if(channel != null && channel.length() > 0 && !Channel.check(team, channel)) {
                    obj.put("code", 400);
                    obj.put("error", "Channel does not exist!");
                }
                if(!User.check(user1) || !User.check(user2)) {
                    obj.put("code", 400);
                    obj.put("error", "User does not exist!");
                } else {
                    for (MentionMessage mm : MentionMessage.both(team, channel, Long.parseLong(from), Long.parseLong(to), user1, user2)) {
                        obj.append("data", mm.toJSON());
                    }
                    if (!obj.has("data")) {
                        obj.put("data", new JSONArray());
                    }
                    obj.put("code", 20000);
                }
            }
        } catch (Exception e) {
            obj.put("code", 500);
            obj.put("error", e.getMessage());
            e.printStackTrace();
        }
        return obj.toString();
    }

}
