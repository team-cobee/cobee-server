package org.cobee.server.home.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.cobee.server.auth.jwt.TokenInfo;
import org.cobee.server.global.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/home")
    public ApiResponse<Map<String, Object>> home(HttpServletRequest request) {
      HttpSession session = request.getSession(false);
      if (session != null) {
        TokenInfo tokenInfo = (TokenInfo) session.getAttribute("tokenInfo");
        if (tokenInfo != null) {
          Map<String, Object> data = new HashMap<>();
          data.put("accessToken", tokenInfo.getAccessToken());
          data.put("refreshToken", tokenInfo.getRefreshToken());
          return ApiResponse.success("Login successful", "AUTH_SUCCESS", data);
        }
      }
        return ApiResponse.failure("No token found", "400", "NO_TOKEN");
    }
}