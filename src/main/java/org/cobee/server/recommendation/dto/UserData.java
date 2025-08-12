package org.cobee.server.recommendation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserData {
    @JsonProperty("user_id")
    private Long userId;
    
    @JsonProperty("age")
    private Integer age;
    
    @JsonProperty("gender")
    private String gender;
    
    @JsonProperty("smoking")
    private String smoking;
    
    @JsonProperty("pet")
    private String pet;
    
    @JsonProperty("snoring")
    private String snoring;
    
    @JsonProperty("preferred_gender")
    private String preferredGender;
}