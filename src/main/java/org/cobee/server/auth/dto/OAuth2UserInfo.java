package org.cobee.server.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OAuth2UserInfo {
    private String id;
    private String nickname;
    private String email;
}

