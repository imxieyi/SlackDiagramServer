package org.slackdiagram.server.controller;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slackdiagram.server.model.Channel;
import org.slackdiagram.server.model.Team;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/channel")
public class ChannelController {

    private Logger log = Logger.getLogger(ChannelController.class.getName());

    @RequestMapping(method = RequestMethod.GET)
    public String get(HttpServletRequest req) {
        JSONObject obj = new JSONObject();
        String team = req.getParameter("team");
        try {
            if(team == null || team.length() <= 0) {
                obj.put("status", 2);
                obj.put("error", "Illegal request!");
            } else if(!Team.check(team)) {
                // Team does not exist
                obj.put("status", 3);
                obj.put("error", "Team does not exist!");
            } else {
                for (Channel t : Channel.all(team, true)) {
                    obj.append("channel", t.toJSON());
                }
                if (!obj.has("channel")) {
                    // No channel found
                    obj.put("channel", new JSONArray());
                }
                obj.put("status", 0);
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
