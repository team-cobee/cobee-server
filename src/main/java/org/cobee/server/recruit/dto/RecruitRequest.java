package org.cobee.server.recruit.dto;

public record RecruitRequest
        (String title, int recruitCount, int rentCost,
         int monthlyCost, String imgUrl, String content) { }
