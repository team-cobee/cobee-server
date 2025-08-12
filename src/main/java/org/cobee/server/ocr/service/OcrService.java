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

            OcrResponse ocrResponse = response.getBody();
            
            if (ocrResponse != null) {
                log.info("OCR API 호출 성공 - filename: {}, success: {}", 
                    ocrResponse.getFilename(), ocrResponse.isSuccess());
                
                if (!ocrResponse.isSuccess()) {
                    log.warn("OCR 처리 실패: {}", ocrResponse.getMessage());
                }
                
                return ocrResponse;
            } else {
                log.error("OCR API 응답이 null입니다.");
                throw new RuntimeException("OCR API에서 응답을 받지 못했습니다.");
            }

        } catch (Exception e) {
            log.error("OCR API 호출 실패: ", e);
            
            // 실패 응답 객체 생성
            OcrResponse errorResponse = new OcrResponse();
            errorResponse.setSuccess(false);
            errorResponse.setError("OCR 처리 중 오류가 발생했습니다: " + e.getMessage());
            
            return errorResponse;
        }
    }
}