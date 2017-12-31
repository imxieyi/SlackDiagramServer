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
                obj.append("data", t.toJSON());
            }
            if(!obj.has("data")) {
                // No team found
                obj.put("data", new JSONArray());
            }
            obj.put("code", 20000);
        } catch (Exception e) {
            obj.put("code", 500);
            obj.put("error", e.getMessage());
            e.printStackTrace();
        }
        return obj.toString();
    }

}
