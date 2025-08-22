package org.cobee.server.recruit.dto;

import lombok.Getter;

@Getter
public class RecruitRequest{
    private String title;
    private int recruitCount;
    private int rentCost;
    private int monthlyCost;
    private String imgUrl;
    private String content;
}
