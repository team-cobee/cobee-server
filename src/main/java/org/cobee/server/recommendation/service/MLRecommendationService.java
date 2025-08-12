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
    
    private final RestTemplate restTemplate = new RestTemplate();
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
        
        // TODO: PublicProfile과 UserPreferences에서 실제 데이터 매핑
        // 임시 구현
        userData.setAge(25); // member.getPublicProfile().getAge()
        userData.setGender("남자"); // member.getPublicProfile().getGender().getDisplayName()
        userData.setSmoking("흡연 불가"); // member.getUserPreferences().getSmokingPreference()
        userData.setPet("불가능"); // member.getUserPreferences().getPetPreference()
        userData.setSnoring("코골이 불가"); // member.getUserPreferences().getSnoringPreference()
        userData.setPreferredGender("여자"); // member.getUserPreferences().getPreferredGender()
        
        return userData;
    }
    
    private ListingData convertToListingData(RecruitPost post) {
        ListingData listingData = new ListingData();
        listingData.setListingId(post.getId());
        listingData.setTitle(post.getTitle());
        listingData.setDescription(post.getContent());
        
        // TODO: RecruitPost에서 실제 선호 조건들 매핑
        // 임시 구현
        listingData.setPrice(500000);
        listingData.setLocation("서울");
        listingData.setPreferredAgeMin(20);
        listingData.setPreferredAgeMax(30);
        listingData.setGenderPreference("상관없음");
        listingData.setSmokingAllowed("흡연 불가");
        listingData.setPetAllowed("상관 없음");
        
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