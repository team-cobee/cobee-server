package org.cobee.server.chat.dto;

import lombok.Builder;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;
@Getter
@Builder
public class JoinRoomRequestDto {
    @NotNull
    private Long userId;
}

