package com.dockerforjavadevelopers.hello;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "<html>"
             + "<head>"
             + "<style>"
             + "body {"
             + "    font-family: Arial, sans-serif;"
             + "    background-color: #2c3e50;"
             + "    color: white;"
             + "    display: flex;"
             + "    justify-content: center;"
             + "    align-items: center;"
             + "    height: 100vh;"
             + "    margin: 0;"
             + "}"
             + ".colorful-text {"
             + "    font-size: 48px;"
             + "    font-weight: bold;"
             + "    text-align: center;"
             + "}"
             + "</style>"
             + "</head>"
             + "<body>"
             + "<div class='colorful-text'>"
             + "<span style='color: #e74c3c;'>Hello, </span>"
             + "<span style='color: #f39c12;'>Welcome </span>"
             + "<span style='color: #1abc9c;'>to Sangamesh </span>"
             + "<span style='color: #3498db;'>Demo Session</span>"
             + "</div>"
             + "</body>"
             + "</html>";
    }
}

