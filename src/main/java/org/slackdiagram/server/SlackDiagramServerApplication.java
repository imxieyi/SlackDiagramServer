package org.slackdiagram.server;

import org.slackdiagram.server.util.DBHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SlackDiagramServerApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SlackDiagramServerApplication.class, args);
        DBHelper.init();
        // Test db connection
        DBHelper.getConnection().close();
	}
}
