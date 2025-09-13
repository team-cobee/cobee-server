package org.cobee.server.chat.repository;

import java.util.Optional;
import org.cobee.server.chat.domain.ChatRoom;
import org.cobee.server.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT r FROM ChatRoom r JOIN FETCH r.post JOIN r.users u WHERE u = :member")
    Optional<ChatRoom> findByMember(@Param("member") Member member);

}