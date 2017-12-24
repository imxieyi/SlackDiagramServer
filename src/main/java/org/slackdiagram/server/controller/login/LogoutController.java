package org.slackdiagram.server.controller.login;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user/logout")
public class LogoutController {

    private Logger log = Logger.getLogger(LogoutController.class.getName());

    @RequestMapping(method = RequestMethod.POST)
    public String post(HttpServletRequest req) {
        return "{\n" +
                "  \"code\": 20000,\n" +
                "  \"data\": \"success\"\n" +
                "}";
    }

}
