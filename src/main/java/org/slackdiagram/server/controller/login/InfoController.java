package org.slackdiagram.server.controller.login;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user/info")
public class InfoController {

    private Logger log = Logger.getLogger(InfoController.class.getName());

    @RequestMapping(method = RequestMethod.GET)
    public String get(HttpServletRequest req) {
        return "{\n" +
                "  \"code\": 20000,\n" +
                "  \"data\": {\n" +
                "    \"role\": [\n" +
                "      \"admin\"\n" +
                "    ],\n" +
                "    \"name\": \"admin\",\n" +
                "    \"avatar\": \"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif\"\n" +
                "  }\n" +
                "}";
    }

}
