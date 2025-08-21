package org.cobee.server.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cobee.server.recommendation.dto.DataSyncRequest;
import org.cobee.server.recommendation.dto.ListingData;
import org.cobee.server.recommendation.dto.UserData;
import org.cobee.server.member.service.MemberService;
import org.cobee.server.recruit.service.RecruitService;
import org.cobee.server.member.domain.Member;
import org.cobee.server.publicProfile.domain.PublicProfile;
import org.cobee.server.member.domain.UserPreferences;
import org.cobee.server.recruit.domain.RecruitPost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MLRecommendationService {
    
    @Value("${ml.api.url}")
    private String mlApiUrl;
    
    @Value("${ml.batch.enabled:true}")
    private boolean mlBatchEnabled;
    
    private final RestTemplate restTemplate;
    private final MemberService memberService;
    private final RecruitService recruitService;
    
    @Scheduled(cron = "0 0 0 * * ?") // 매일 00시
    public void syncDataToML() {
        if (!mlBatchEnabled) {
            log.info("ML 배치가 비활성화되어 있습니다.");
            return;
        }
        
        try {
            log.info("ML 데이터 동기화 시작...");
            
            // 데이터 수집
            List<UserData> users = collectUserData();
            List<ListingData> listings = collectListingData();
            
            log.info("수집된 데이터 - 사용자: {}명, 구인글: {}개", users.size(), listings.size());
            
            DataSyncRequest request = new DataSyncRequest();
            request.setUsers(users);
            request.setListings(listings);
            
            // ML API 호출
            String response = restTemplate.postForObject(
                mlApiUrl + "/sync-data", 
                request, 
                String.class
            );
            
            log.info("ML 데이터 동기화 완료: {}", response);
            
        } catch (Exception e) {
            log.error("ML 데이터 동기화 실패", e);
        }
    }
    
    private List<UserData> collectUserData() {
        // TODO: 실제 Member + PublicProfile + UserPreferences 조인해서 데이터 수집
        // 현재는 예시 구현
        return memberService.getAllMembers().stream()
            .map(this::convertToUserData)
            .collect(Collectors.toList());
    }
    
    private List<ListingData> collectListingData() {
        // TODO: 실제 RecruitPost 데이터 수집
        // 현재는 예시 구현
        return recruitService.getAllActiveRecruits().stream()
            .map(this::convertToListingData)
            .collect(Collectors.toList());
    }
    
    private UserData convertToUserData(Member member) {
        UserData userData = new UserData();
        userData.setUserId(member.getId());
        
        // Member의 기본 정보 - 성별을 표준화된 값으로 변환
        if (member.getGender() != null) {
            String gender = member.getGender();
            if ("남자".equals(gender)) {
                userData.setGender("MALE");
            } else if ("여자".equals(gender)) {
                userData.setGender("FEMALE");
            } else {
                userData.setGender("UNKNOWN");
            }
        } else {
            userData.setGender("UNKNOWN");
        }
        
        // 나이 계산 (간단 구현: 현재 연도 - 출생연도)
        if (member.getBirthDate() != null) {
            try {
                int birthYear = Integer.parseInt(member.getBirthDate().substring(0, 4));
                int currentYear = java.time.Year.now().getValue();
                userData.setAge(currentYear - birthYear);
            } catch (Exception e) {
                userData.setAge(25); // 기본값
            }
        } else {
            userData.setAge(25); // 기본값
        }
        
        // UserPreferences에서 데이터 매핑
        if (member.getUserPreferences() != null) {
            var preferences = member.getUserPreferences();
            
            // Lifestyle과 Personality를 문자열로 변환해서 설정
            userData.setLifestyle(preferences.getLifestyle() != null ? 
                preferences.getLifestyle().name() : null);
            userData.setPersonality(preferences.getPersonality() != null ? 
                preferences.getPersonality().name() : null);
            
            // 선호도 관련 데이터 - ML 모델용 영어 값 사용
            userData.setSmoking(preferences.getSmokingPreference() != null ? 
                preferences.getSmokingPreference().name() : "NO_PREFERENCE");
            userData.setPet(preferences.getPetPreference() != null ? 
                preferences.getPetPreference().name() : "NO_PREFERENCE");
            userData.setSnoring(preferences.getSnoringPreference() != null ? 
                preferences.getSnoringPreference().name() : "NO_PREFERENCE");
            userData.setPreferredGender(preferences.getPreferredGender() != null ? 
                preferences.getPreferredGender().name() : "NO_PREFERENCE");
        } else {
            // UserPreferences가 없는 경우 기본값 설정
            userData.setLifestyle(null);
            userData.setPersonality(null);
            userData.setSmoking("NO_PREFERENCE");
            userData.setPet("NO_PREFERENCE");
            userData.setSnoring("NO_PREFERENCE");
            userData.setPreferredGender("NO_PREFERENCE");
        }
        
        return userData;
    }
    
    private ListingData convertToListingData(RecruitPost post) {
        ListingData listingData = new ListingData();
        listingData.setListingId(post.getId());
        listingData.setTitle(post.getTitle());
        listingData.setDescription(post.getContent());
        
        // RecruitPost에서 실제 데이터 매핑
        listingData.setPrice(post.getRentCost() + post.getMonthlyCost()); // 보증금 + 월세
        listingData.setLocation("서울"); // TODO: 실제 위치 정보 매핑 필요
        
        // 나이대 설정 - RecruitPost 작성자의 UserPreferences에서 가져오기
        if (post.getMember() != null && post.getMember().getUserPreferences() != null) {
            var preferences = post.getMember().getUserPreferences();
            listingData.setPreferredAgeMin(preferences.getMinAge() != null ? preferences.getMinAge() : 20);
            listingData.setPreferredAgeMax(preferences.getMaxAge() != null ? preferences.getMaxAge() : 35);
        } else {
            listingData.setPreferredAgeMin(20);
            listingData.setPreferredAgeMax(35);
        }
        
        // 성별 선호도 매핑 - ML 모델용 영어 값 사용
        listingData.setGenderPreference(post.getPreferredGender() != null ? 
            post.getPreferredGender().name() : "NO_PREFERENCE");
        
        // 구인글 작성자의 성별 정보 - 표준화된 값 사용
        if (post.getMember() != null && post.getMember().getGender() != null) {
            String gender = post.getMember().getGender();
            if ("남자".equals(gender)) {
                listingData.setAuthorGender("MALE");
            } else if ("여자".equals(gender)) {
                listingData.setAuthorGender("FEMALE");
            } else {
                listingData.setAuthorGender("UNKNOWN");
            }
        } else {
            listingData.setAuthorGender("UNKNOWN");
        }
        
        // 흡연 허용 여부 - 영어 값 사용
        listingData.setSmokingAllowed(post.getIsSmoking() != null && post.getIsSmoking() ? 
            "SMOKING_ALLOWED" : "NO_SMOKING");
        
        // 반려동물 허용 여부 - 영어 값 사용
        listingData.setPetAllowed(post.getIsPetsAllowed() != null && post.getIsPetsAllowed() ? 
            "POSSIBLE" : "IMPOSSIBLE");
        
        // RecruitPost의 lifestyle과 personality 매핑
        listingData.setLifestyle(post.getLifeStyle() != null ? 
            post.getLifeStyle().name() : null);
        listingData.setPersonality(post.getPersonality() != null ? 
            post.getPersonality().name() : null);
        
        return listingData;
    }
    
    public Object getBatchResult() {
        try {
            return restTemplate.getForObject(mlApiUrl + "/batch/result", Object.class);
        } catch (Exception e) {
            log.error("배치 결과 조회 실패", e);
            return null;
        }
    }
    
    public Object getBatchStatus() {
        try {
            return restTemplate.getForObject(mlApiUrl + "/batch/status", Object.class);
        } catch (Exception e) {
            log.error("배치 상태 조회 실패", e);
            return null;
        }
    }
    
    public Object getRecommendations(Long userId) {
        try {
            return restTemplate.getForObject(mlApiUrl + "/recommend/" + userId, Object.class);
        } catch (Exception e) {
            log.error("추천 결과 조회 실패", e);
            return null;
        }
    }
}