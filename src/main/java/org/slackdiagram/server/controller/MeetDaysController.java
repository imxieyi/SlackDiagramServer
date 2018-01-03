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
@RequestMapping("/api/mention/meetdays")
public class MeetDaysController {

    private Logger log = Logger.getLogger(MeetDaysController.class.getName());

    @RequestMapping(method = RequestMethod.GET)
    public String get(HttpServletRequest req) {
        JSONObject obj = new JSONObject();
        String team = req.getParameter("team");
        String channel = req.getParameter("channel");
        String user1 = req.getParameter("user1");
        String user2 = req.getParameter("user2");
        try {
            if(team == null || team.length() <= 0 || user1 == null || user1.length() <= 0 || user2 == null || user2.length() <= 0) {
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
                    int max = 0;
                    int value = Mention.meetdays(team, channel, user1, user2);
                    if(value > max) {
                        max = value;
                    }
                    value = Mention.meetdays(team, channel, user2, user1);
                    if(value > max) {
                        max = value;
                    }
                    obj.put("data", max);
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
