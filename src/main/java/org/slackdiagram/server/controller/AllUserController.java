package org.slackdiagram.server.controller;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.slackdiagram.server.model.Team;
import org.slackdiagram.server.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user/all")
public class AllUserController {

    private Logger log = Logger.getLogger(AllUserController.class.getName());

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
                JSONObject userObj = new JSONObject();
                for (User u : User.all(team)) {
                    userObj.put(u.id, u.toJSON());
                }
                obj.put("user", userObj);
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
