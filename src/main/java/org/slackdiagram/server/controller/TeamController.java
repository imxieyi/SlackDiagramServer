package org.slackdiagram.server.controller;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slackdiagram.server.model.Team;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    private Logger log = Logger.getLogger(TeamController.class.getName());

    @RequestMapping(method = RequestMethod.GET)
    public String get(HttpServletRequest req) {
        JSONObject obj = new JSONObject();
        String domain = req.getParameter("domain");
        try {
            for (Team t : Team.all(domain, true)) {
                obj.append("team", t.toJSON());
            }
            if(!obj.has("team")) {
                // No team found
                obj.put("team", new JSONArray());
            }
            obj.put("status", 0);
        } catch (Exception e) {
            obj.put("status", 1);
            obj.put("message", e.getMessage());
            e.printStackTrace();
        }
        return obj.toString();
    }

}
