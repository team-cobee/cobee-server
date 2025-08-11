package org.cobee.server.comment.dto;

public record CommentRequest(String content,
                             Boolean isPrivate,
                             Long parentId) {
}
