package org.cobee.server.ocr.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.repository.MemberRepository;
import org.cobee.server.ocr.dto.OcrResponse;
import org.cobee.server.ocr.dto.OcrTask;
import org.cobee.server.ocr.dto.OcrVerificationResponseDto;
import org.cobee.server.ocr.enums.OcrTaskStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
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
  private final OcrMemberService ocrMemberService;
  private final MemberRepository memberRepository;

  @Value("${ocr.api.url}")
  private String ocrApiUrl;

  private final Map<String, OcrTask> taskStore = new ConcurrentHashMap<>();

  public String registerNewTask() {
    String taskId = UUID.randomUUID().toString();
    taskStore.put(taskId, new OcrTask());
    return taskId;
  }

  public OcrTask getTaskStatus(String taskId) {
    return taskStore.get(taskId);
  }

  @Async // 비동기 처리
  public void processOcrVerificationAsync(String taskId, Long memberId, MultipartFile file) {
    log.info("비동기 OCR 인증 프로세스 시작 - Task ID : {}", taskId);
    OcrTask task = taskStore.get(taskId);

    try {
      // 비동기 처리 전에 파일 데이터를 미리 읽어서 저장
      byte[] fileBytes = file.getBytes();
      String originalFilename = file.getOriginalFilename();
      
      OcrResponse ocrResult = this.processIdCard(fileBytes, originalFilename);
      if (ocrResult != null && ocrResult.isSuccess()) {
        ocrMemberService.updateMemberWithOcrData(memberId, ocrResult);

        Member updateMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        task.setStatus(OcrTaskStatus.SUCCESS);
        task.setResult(OcrVerificationResponseDto.success(updateMember));
        log.info("비동기 OCR 인증 프로세스 성공 - Task ID : {}", taskId);
      } else {
        // ocr API 자체가 실패한 경우
        throw new CustomException(ErrorCode.FAILED_OCR_API);
      }
    } catch (Exception e) {
      log.error("비동기 OCR 처리 실패 - Task ID: {}", taskId, e);
      task.setStatus(OcrTaskStatus.FAILED);
      task.setErrorMessage(e.getMessage());
    }
  }


  /**
   * FastAPI OCR 서버로 주민등록증 이미지 전송하여 정보 추출 (바이트 배열 버전)
   */
  public OcrResponse processIdCard(byte[] fileBytes, String originalFilename) {
    try {
      // 바이트 배열을 ByteArrayResource로 변환
      ByteArrayResource resource = new ByteArrayResource(fileBytes) {
        @Override
        public String getFilename() {
          return originalFilename;
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

  /**
   * FastAPI OCR 서버로 주민등록증 이미지 전송하여 정보 추출 (MultipartFile 버전)
   */
  public OcrResponse processIdCard(MultipartFile imageFile) {
    try {
      return processIdCard(imageFile.getBytes(), imageFile.getOriginalFilename());
    } catch (Exception e) {
      log.error("파일 읽기 실패: ", e);
      
      // 실패 응답 객체 생성
      OcrResponse errorResponse = new OcrResponse();
      errorResponse.setSuccess(false);
      errorResponse.setError("파일 읽기 중 오류가 발생했습니다: " + e.getMessage());
      
      return errorResponse;
    }
  }
}