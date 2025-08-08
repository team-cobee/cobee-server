package org.cobee.server.home.controller;

import org.cobee.server.global.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/home")
    public ApiResponse<Map<String, Object>> home(@RequestParam(required = false) String code,
                                                @RequestParam(required = false) String token) {
        Map<String, Object> data = new HashMap<>();
        
        if (token != null) {
            data.put("token", token);
            return ApiResponse.success("Login successful", "200", data);
        } else if (code != null) {
            data.put("code", code);
            return ApiResponse.success("Authorization code received", "200", data);
        } else {
            return ApiResponse.failure("No token or code provided", "400", "MISSING_PARAMETER");
        }
    }
}
