package org.cobee.server.recruit.dto;

import lombok.Getter;
import org.cobee.server.publicProfile.domain.enums.*;

import java.util.List;

@Getter
public class RecruitRequest{
    private String title;
    private Integer recruitCount;
    private Integer rentCostMin;
    private Integer rentCostMax;
    private Integer monthlyCostMin;
    private Integer monthlyCostMax;
    private Integer minAge;
    private Integer maxAge;
    private Lifestyle lifestyle;
    private Personality personality;
    private Smoking isSmoking;
    private Snoring isSnoring;
    private Pets isPetsAllowed;
    private Boolean hasRoom;
    private List<String> imgUrl;
    private String address;  //  지도가 처리?
    private String detailDescription;
    private String additionalDescription;
}
