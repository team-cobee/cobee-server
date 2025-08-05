package org.cobee.server.comment.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.member.domain.Member;
import org.cobee.server.recruit.domain.RecruitPost;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private Boolean isPrivate;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private RecruitPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;  // 부모 댓글

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    public void addChild(Comment child) {
        children.add(child);
        child.parent = this;
    }
}
