package org.slackdiagram.server.controller;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@RestController
@RequestMapping("/api/analysis/**")
public class AnalysisController {

    private Logger log = Logger.getLogger(AnalysisController.class.getName());

    private static String analysis_url = null;

    private static synchronized String getAnalysisURL() throws IOException {
        if(analysis_url == null) {
            Properties config = new Properties();
            File configFile = new File("config.properties");
            if(!configFile.exists()) {
                throw new FileNotFoundException("config.properties not found!");
            }
            config.load(new FileInputStream(configFile));
            analysis_url = config.getProperty("analysis");
            if(analysis_url.charAt(analysis_url.length() - 1) != '/') {
                analysis_url = analysis_url + "/";
            }
        }
        return analysis_url;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String get(HttpServletRequest req) {
        JSONObject ret = new JSONObject();
        String uri = req.getRequestURI();
        try {
            uri = uri.replaceAll("/api/analysis/", getAnalysisURL());
            log.info("mapped url: " + uri);
            try {
                String analysis = Jsoup.connect(uri).get().text();
                if(analysis.length() <= 0) {
                    ret.put("code", 503);
                    ret.put("error", "No data returned from analysis API server.");
                }
                try {
                    ret.put("data", analysis);
                    ret.put("code", 20000);
                } catch (JSONException e) {
                    ret.put("code", 503);
                    ret.put("error", "Error parsing data from analysis API server.");
                }
            } catch (IOException e) {
                ret.put("code", 503);
                ret.put("error", "Analysis API server not available.");
            }
        } catch (IOException e) {
            ret.put("code", 503);
            ret.put("error", "Analysis API server not configured.");
        }
        return ret.toString();
    }

}
