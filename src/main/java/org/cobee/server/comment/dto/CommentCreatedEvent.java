package org.cobee.server.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCreatedEvent{
        private Long commentId;
        private Long postId;
        private Long fromUserId;
        private Long toUserId;
        private boolean isReply;
}