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
    public ApiResponse<Map<String, Object>> home(@RequestParam(required = false) String access_token,
                                                @RequestParam(required = false) String refresh_token,
                                                @RequestParam(required = false) String expiresIn,
                                                @RequestParam(required = false) String code) {
        Map<String, Object> data = new HashMap<>();
        
        // 토큰 정보 있을 때 (로그인 성공)
        if (access_token != null) {
            data.put("accessToken", access_token);
            if (refresh_token != null) {
                data.put("refreshToken", refresh_token);
            }
            if (expiresIn != null) {
                data.put("expiresIn", expiresIn);
            }
            return ApiResponse.success("Login successful", "AUTH_SUCCESS", data);
        }
        
        // OAuth2 코드만 있을 때
        if (code != null) {
            data.put("code", code);
            return ApiResponse.success("Authorization code received", "200", data);
        }
        
        // 아무 파라미터도 없을 때
        return ApiResponse.failure("No token or code provided", "400", "MISSING_PARAMETER");
    }
}
