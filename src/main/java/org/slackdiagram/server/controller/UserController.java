package org.slackdiagram.server.controller;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.slackdiagram.server.model.Channel;
import org.slackdiagram.server.model.Team;
import org.slackdiagram.server.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private Logger log = Logger.getLogger(UserController.class.getName());

    @RequestMapping(method = RequestMethod.GET)
    public String get(HttpServletRequest req) {
        JSONObject obj = new JSONObject();
        String team = req.getParameter("team");
        String channel = req.getParameter("channel");
        try {
            if(team == null || team.length() <= 0) {
                obj.put("code", 400);
                obj.put("error", "Illegal request!");
            } else if(!Team.check(team)) {
                // Team does not exist
                obj.put("code", 400);
                obj.put("error", "Team does not exist!");
            } else {
                JSONObject userObj = new JSONObject();
                for (User u : User.get(team, channel)) {
                    userObj.put(u.id, u.toJSON());
                }
                obj.put("data", userObj);
                obj.put("code", 20000);
            }
        } catch (Exception e) {
            obj.put("code", 500);
            obj.put("error", e.getMessage());
            e.printStackTrace();
        }
        return obj.toString();
    }

}
