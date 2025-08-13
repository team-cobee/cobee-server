package org.cobee.server.comment.service;

import lombok.RequiredArgsConstructor;
import org.cobee.server.comment.domain.Comment;
import org.cobee.server.comment.dto.CommentCreatedEvent;
import org.cobee.server.comment.dto.CommentRequest;
import org.cobee.server.comment.dto.CommentResponse;
import org.cobee.server.comment.repository.CommentRepository;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.repository.MemberRepository;
import org.cobee.server.recruit.domain.RecruitPost;
import org.cobee.server.recruit.repository.RecruitPostRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final RecruitPostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ApplicationEventPublisher publisher;  // 이벤트 발생기

    @Transactional
    public CommentResponse createComment(Long memberId, Long postId, CommentRequest request){
        Member member = memberRepository.findById(memberId).orElseThrow(()->new CustomException(ErrorCode.UNAUTHORIZED));// 추후 바꾸기
        RecruitPost post = postRepository.findById(postId).orElseThrow(
                ()->new CustomException(ErrorCode.POST_NOT_FOUND));

        Comment parentComment = null;

        // parentId가 있음 대댓글
        if (request.parentId() != null) {
            parentComment = commentRepository.findById(request.parentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        }

        // 댓글(또는 대댓글) 생성
        Comment comment = Comment.builder()
                .content(request.content())
                .isPrivate(request.isPrivate())
                .member(member)
                .post(post)
                .parent(parentComment) // 원댓글이면 null, 대댓글이면 parentId 값의 댓글이 들어가
                .build();

        // 양방향 동기화
        if (parentComment != null) {
            parentComment.addChild(comment); // 대댓글일 경우 부모 댓글에 추가
        } else {
            post.addComment(comment); // 일반 댓글일 경우 게시글에 추가
        }

        commentRepository.save(comment);


        // ===== 알림 대상 선정 =====
        java.util.Set<Long> recipientIds = new java.util.HashSet<>();

        // 항상 게시글 작성자
        recipientIds.add(post.getMember().getId());

        // 대댓글이면 부모 댓글 작성자도
        if (parentComment != null) {
            recipientIds.add(parentComment.getMember().getId());
        }

        // 자기 자신 제거
        recipientIds.remove(memberId);

        // 각 수신자에게 이벤트 발행 (중복 제거된 상태)
        boolean isReply = (parentComment != null);
        for (Long toUserId : recipientIds) {
            publisher.publishEvent(new CommentCreatedEvent(
                    comment.getId(),
                    post.getId(),
                    memberId,   // fromUserId
                    toUserId,   // toUserId
                    isReply
            ));
        }

        return CommentResponse.from(member, comment);
    }

    public List<CommentResponse> getAllComments(Long memberId, Long postId){
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return CommentResponse.toList(comments);
    }

    public CommentResponse updateComment(Long memberId, Long commentId, CommentRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(()->new CustomException(ErrorCode.UNAUTHORIZED));// 추후 바꾸기
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (request.parentId() != null) {
            Comment parent = commentRepository.findById(request.parentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
            comment.setParent(parent);
        }
        comment.updateComment(request);
        commentRepository.save(comment);
        return CommentResponse.from(member, comment);

    }

    public Boolean deleteComment(Long memberId, Long commentId){
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
            commentRepository.delete(comment);
            return true;
        } catch (CustomException e){
            return false;
        }

    }


}
