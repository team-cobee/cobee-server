package org.cobee.server.recommendation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationItem {
    @JsonProperty("listing_id")
    private int listingId;
    
    private double similarity;
    private String title;
    private String location;
    private int price;
    private String description;
}