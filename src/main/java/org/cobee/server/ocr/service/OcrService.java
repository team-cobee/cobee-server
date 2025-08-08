package org.cobee.server.ocr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cobee.server.ocr.dto.OcrResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class OcrService {

    private final RestTemplate restTemplate;

    @Value("${ocr.api.url}")
    private String ocrApiUrl;

    /**
     * FastAPI OCR 서버로 주민등록증 이미지 전송하여 정보 추출
     */
    public OcrResponse processIdCard(MultipartFile imageFile) {
        try {
            // MultipartFile을 ByteArrayResource로 변환
            ByteArrayResource resource = new ByteArrayResource(imageFile.getBytes()) {
                @Override
                public String getFilename() {
                    return imageFile.getOriginalFilename();
                }
            };

            // Multipart 요청 구성
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", resource);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // FastAPI 서버로 POST 요청
            String uploadUrl = ocrApiUrl + "/upload";
            ResponseEntity<OcrResponse> response = restTemplate.postForEntity(
                    uploadUrl, requestEntity, OcrResponse.class);

            log.info("OCR API 호출 성공: {}", response.getBody());
            return response.getBody();

        } catch (Exception e) {
            log.error("OCR API 호출 실패: ", e);
            throw new RuntimeException("OCR 처리 중 오류가 발생했습니다.", e);
        }
    }
}