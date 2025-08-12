package org.cobee.server.recommendation.dto;

import lombok.Data;
import java.util.List;

@Data
public class DataSyncRequest {
    private List<UserData> users;
    private List<ListingData> listings;
}