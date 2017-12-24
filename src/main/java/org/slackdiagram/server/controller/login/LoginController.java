package org.slackdiagram.server.controller.login;

import org.apache.log4j.Logger;
import org.slackdiagram.server.controller.AllUserController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user/login")
public class LoginController {

    private Logger log = Logger.getLogger(LoginController.class.getName());

    @RequestMapping(method = RequestMethod.POST)
    public String post(HttpServletRequest req) {
        return "{\n" +
                "  \"code\": 20000,\n" +
                "  \"data\": {\n" +
                "    \"token\": \"admin\"\n" +
                "  }\n" +
                "}";
    }

}
