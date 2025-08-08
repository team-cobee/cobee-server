package org.cobee.server.home.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/home")
    public Map<String, Object> home(@RequestParam(required = false) String code,
                                    @RequestParam(required = false) String token) {
        Map<String, Object> response = new HashMap<>();
        
        if (token != null) {
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("token", token);
        } else if (code != null) {
            response.put("success", true);
            response.put("message", "Authorization code received");
            response.put("code", code);
        } else {
            response.put("success", false);
            response.put("message", "No token or code provided");
        }
        
        return response;
    }
}
