package org.cobee.server.publicProfile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cobee.server.publicProfile.domain.enums.Lifestyle;
import org.cobee.server.publicProfile.domain.enums.Personality;
import org.cobee.server.publicProfile.dto.PublicProfileRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import org.cobee.server.member.Member;
import org.cobee.server.member.MemberRepository;
import org.cobee.server.publicProfile.domain.PublicProfile;
import org.cobee.server.publicProfile.dto.PublicProfileUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PublicProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member(null, "testUser", "test@example.com", "2000-01-01", "male", null, false, false, null, null, null, null, null, null, null, null);
        memberRepository.save(member);
    }

    @Test
    void createPublicProfile() throws Exception {
        // given
        PublicProfileRequestDto requestDto = new PublicProfileRequestDto(
                "저는 조용한 성격이에요.",
                Lifestyle.morning,
                Personality.introvert,
                false,
                true,
                false
        );

        // when & then
        mockMvc.perform(post("/public-profiles/" + member.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Public profile created successfully"));
    }

    @Test
    void getPublicProfile() throws Exception {
        // given
        PublicProfile publicProfile = new PublicProfile("info", Lifestyle.morning, Personality.introvert, false, true, false);
        member.setPublicProfile(publicProfile);
        memberRepository.save(member);

        // when & then
        mockMvc.perform(get("/public-profiles/" + member.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(member.getId()))
                .andExpect(jsonPath("$.info").value("info"));
    }

    @Test
    void updatePublicProfile() throws Exception {
        // given
        PublicProfile publicProfile = new PublicProfile("info", Lifestyle.morning, Personality.introvert, false, true, false);
        member.setPublicProfile(publicProfile);
        memberRepository.save(member);

        PublicProfileUpdateRequestDto requestDto = new PublicProfileUpdateRequestDto(
                "외향적인 성격이고 반려동물 좋아해요.",
                Lifestyle.night,
                Personality.extrovert,
                false,
                false,
                true
        );

        // when & then
        mockMvc.perform(patch("/public-profiles/" + member.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Public profile modified successfully"));
    }
}
