package org.cobee.server.recruit.service;

import lombok.RequiredArgsConstructor;
import org.cobee.server.recruit.dto.ApplyRequest;
import org.cobee.server.recruit.dto.ApplyResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplyService {
    public ApplyResponse apply(Long memberId, ApplyRequest request) {
        return null;
    }
}
