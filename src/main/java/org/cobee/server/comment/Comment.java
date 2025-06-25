package org.cobee.server.comment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.member.Member;
import org.cobee.server.recruit.RecruitPost;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private Boolean is_private;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private RecruitPost post;


}
