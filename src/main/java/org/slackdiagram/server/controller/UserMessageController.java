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
@RequestMapping("/api/user/message")
public class UserMessageController {

    private Logger log = Logger.getLogger(UserMessageController.class.getName());

    @RequestMapping(method = RequestMethod.GET)
    public String get(HttpServletRequest req) {
        JSONObject obj = new JSONObject();
        String team = req.getParameter("team");
        String channel = req.getParameter("channel");
        String user = req.getParameter("user");
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String length = req.getParameter("length");
        String offset = req.getParameter("offset");
        try {
            if(team == null || team.length() <= 0 || from == null || from.length() <=0 ||
                    to == null || to.length() <= 0 || user == null || user.length() <= 0) {
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
                if(!User.check(user)) {
                    obj.put("code", 400);
                    obj.put("error", "User does not exist!");
                } else {
                    int len, off;
                    if(length != null && length.length() > 0) {
                        len = Integer.parseInt(length);
                    } else {
                        len = Integer.MAX_VALUE;
                    }
                    if(offset != null && offset.length() > 0) {
                        off = Integer.parseInt(offset);
                    } else {
                        off = 0;
                    }
                    for (UserMessage mm : UserMessage.range(team, channel, Long.parseLong(from), Long.parseLong(to), user, len, off)) {
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
