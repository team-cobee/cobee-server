package org.cobee.server.auth.service;

import lombok.RequiredArgsConstructor;
import org.cobee.server.auth.dto.OAuthAttributes;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.domain.enums.SocialType;
import org.cobee.server.member.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        System.out.println("=== OAuth2 Debug Info ===");
        System.out.println("Registration ID: " + registrationId);
        System.out.println("Social Type: " + socialType);
        System.out.println("User Attributes: " + attributes);

        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        Member createdMember = getUser(extractAttributes, socialType);
        System.out.println("Created Member: " + createdMember.getId() + ", " + createdMember.getName());

        return new PrincipalDetails(createdMember, attributes);
    }

    private SocialType getSocialType(String registrationId) {
        if ("kakao".equals(registrationId)) {
            return SocialType.KAKAO;
        }
        if ("google".equals(registrationId)) {
            return SocialType.GOOGLE;
        }
        return null;
    }

    private Member getUser(OAuthAttributes attributes, SocialType socialType) {
        String socialId = attributes.getOauth2UserInfo().getId();
        System.out.println("=== getUser Debug ===");
        System.out.println("socialType: " + socialType);
        System.out.println("socialId: " + socialId);
        
        Member findMember = memberRepository.findBySocialTypeAndSocialId(socialType, socialId).orElse(null);
        System.out.println("findMember: " + findMember);

        if (findMember == null) {
            System.out.println("Member not found, creating new member...");
            return saveUser(attributes, socialType);
        }
        System.out.println("Member found, returning existing member");
        return findMember;
    }

    private Member saveUser(OAuthAttributes attributes, SocialType socialType) {
        System.out.println("=== saveUser Debug ===");
        Member createdMember = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
        System.out.println("Before save - Member: " + createdMember);
        System.out.println("Email: " + createdMember.getEmail());
        System.out.println("SocialId: " + createdMember.getSocialId());
        System.out.println("SocialType: " + createdMember.getSocialType());
        System.out.println("IsCompleted: " + createdMember.getIsCompleted());
        
        Member savedMember = memberRepository.save(createdMember);
        System.out.println("After save - Member ID: " + savedMember.getId());
        return savedMember;
    }
}