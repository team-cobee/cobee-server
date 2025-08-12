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
    
    @JsonProperty("gender_preference")
    private String genderPreference;
    
    @JsonProperty("smoking_allowed")
    private String smokingAllowed;
    
    @JsonProperty("pet_allowed")
    private String petAllowed;
}