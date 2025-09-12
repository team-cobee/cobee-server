package org.cobee.server.bookmark.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.global.BaseEntity;
import org.cobee.server.member.domain.Member;
import org.cobee.server.recruit.domain.RecruitPost;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "recruit_post_id"})) // 중복 북마크 방지
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_post_id", nullable = false)
    private RecruitPost recruitPost;
}
