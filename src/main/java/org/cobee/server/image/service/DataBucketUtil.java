package org.cobee.server.image.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataBucketUtil {
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    private final Storage storage;

    public String uploadImage(MultipartFile file) throws IOException {
        // 이미지 uuid와 파일 형식
        String uuid = UUID.randomUUID().toString();
        String ext = file.getContentType();
        log.info("uuid: {}, ext: {}", uuid, ext);

        // Google Cloud Storage에 이미지 업로드
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, uuid)
                .setContentType(ext)
                .build();
        log.info("blobInfo: {}", blobInfo);
        Blob blob = storage.create(blobInfo, file.getBytes());
        log.info("blob: {}", blob);
        return bucketName + "/" + uuid;
    }
}
