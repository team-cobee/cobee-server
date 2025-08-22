package org.cobee.server.recruit.dto;

import lombok.Getter;
import org.cobee.server.publicProfile.domain.enums.*;

import java.util.List;

@Getter
public class RecruitRequest{
    private String title;
    private int recruitCount;
    private int rentCostMin;
    private int rentCostMax;
    private int monthlyCostMin;
    private int monthlyCostMax;
    private int minAge;
    private int maxAge;
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
