package org.cobee.server.recommendation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ListingData {
    @JsonProperty("listing_id")
    private Long listingId;
    
    private String title;
    private String location;
    private Integer price;
    private String description;
    
    @JsonProperty("preferred_age_min")
    private Integer preferredAgeMin;
    
    @JsonProperty("preferred_age_max")
    private Integer preferredAgeMax;
    
    @JsonProperty("listing_preferred_gender")
    private String genderPreference;
    
    @JsonProperty("author_gender")
    private String authorGender;
    
    @JsonProperty("smoking_allowed")
    private String smokingAllowed;
    
    @JsonProperty("pet_allowed")
    private String petAllowed;
    
    @JsonProperty("lifestyle")
    private String lifestyle;
    
    @JsonProperty("personality")
    private String personality;
}