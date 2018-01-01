package org.slackdiagram.server.controller;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slackdiagram.server.model.Channel;
import org.slackdiagram.server.model.ChannelInfo;
import org.slackdiagram.server.model.MentionCount;
import org.slackdiagram.server.model.Team;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/channel/info")
public class ChannelInfoController {

    private Logger log = Logger.getLogger(ChannelInfoController.class.getName());

    @RequestMapping(method = RequestMethod.GET)
    public String get(HttpServletRequest req) {
        JSONObject obj = new JSONObject();
        String team = req.getParameter("team");
        String channel = req.getParameter("channel");
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        try {
            if(team == null || team.length() <= 0 || from == null || from.length() <=0 || to == null || to.length() <= 0) {
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
                ChannelInfo ci = ChannelInfo.getAll(team, channel, Long.parseLong(from), Long.parseLong(to));
                if(ci.accounts < 0 || ci.messages < 0 || ci.relations < 0) {
                    obj.put("code", 500);
                    obj.put("error", "Failed to read database");
                } else {
                    obj.put("data", ci.toJSON());
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
