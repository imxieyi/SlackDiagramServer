package org.slackdiagram.server.controller;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slackdiagram.server.model.Channel;
import org.slackdiagram.server.model.MessageCount;
import org.slackdiagram.server.model.Team;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/message/count")
public class MessageCountController {

    private Logger log = Logger.getLogger(MessageCountController.class.getName());

    @RequestMapping(method = RequestMethod.GET)
    public String get(HttpServletRequest req) {
        JSONObject obj = new JSONObject();
        String team = req.getParameter("team");
        String channel = req.getParameter("channel");
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        try {
            if(team == null || team.length() <= 0 || from == null || from.length() <=0 || to == null || to.length() <= 0) {
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
                    for (MessageCount mc : MessageCount.range(team, channel, Long.parseLong(from), Long.parseLong(to))) {
                        obj.append("message", mc.toJSON());
                    }
                    if (!obj.has("message")) {
                        obj.put("message", new JSONArray());
                    }
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
