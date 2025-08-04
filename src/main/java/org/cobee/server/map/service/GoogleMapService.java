package org.cobee.server.map.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cobee.server.map.dto.NearbyPlaceDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GoogleMapService {

    @Value("${google.maps.api-key}")
    private String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();

    public String getAddressFromCoordinates(double latitude, double longitude) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json"
                + "?latlng=" + latitude + "," + longitude
                + "&key=" + apiKey
                + "&language=ko";

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject json = new JSONObject(response.getBody());
            JSONArray results = json.getJSONArray("results");

            if (results.length() > 0) {
                JSONObject formatted = results.getJSONObject(0);
                return formatted.getString("formatted_address");
            } else {
                return "주소를 찾지 못 했어요.";
            }
        } catch (Exception e) {
            throw new RuntimeException("구글 맵에서 주소 가져오기 실패", e);
        }
    }

    public List<NearbyPlaceDto> getNearbyPlaces(double latitude, double longitude, Integer radiusInMeters) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
                + "?location=" + latitude + "," + longitude
                + "&radius=" + radiusInMeters
                + "&key=" + apiKey
                + "&language=ko";

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject json = new JSONObject(response.getBody());

            JSONArray results = json.getJSONArray("results");

            List<NearbyPlaceDto> places = new ArrayList<>();

            for (int i = 0; i < results.length(); i++) {
                JSONObject obj = results.getJSONObject(i);
                JSONObject location = obj.getJSONObject("geometry").getJSONObject("location");

                places.add(NearbyPlaceDto.builder()
                        .name(obj.optString("name"))
                        .address(obj.optString("vicinity"))
                        .latitude(location.getDouble("lat"))
                        .longitude(location.getDouble("lng"))
                        .build());
            }

            return places;

        } catch (Exception e) {
            throw new RuntimeException("Google Nearby Search API 호출 실패", e);
        }
    }
}
