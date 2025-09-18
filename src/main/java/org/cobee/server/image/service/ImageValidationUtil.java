package org.cobee.server.image.service;

import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class ImageValidationUtil {
    private static final int MAX_MULTIPLE_COUNT = 3;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/jpg", "image/png", "image/webp");

    public static void validateSingleImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_FILE);
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new CustomException(ErrorCode.FILE_SIZE_EXCEEDED);
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new CustomException(ErrorCode.INVALID_FILE_TYPE);
        }
    }

    public static void validateMultipleImageFiles(MultipartFile[] files)
    {
        if (files == null || files.length == 0) {
            throw new CustomException(ErrorCode.NO_FILES_PROVIDED);
        }
        if (files.length > MAX_MULTIPLE_COUNT) {
            throw new CustomException(ErrorCode.TOO_MANY_FILES);
        }
        for (MultipartFile file : files) {
            validateSingleImageFile(file);
        }
    }
}

