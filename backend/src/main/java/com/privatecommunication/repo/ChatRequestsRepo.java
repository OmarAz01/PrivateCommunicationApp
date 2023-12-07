package com.privatecommunication.repo;

import com.privatecommunication.dto.RequestDTO;
import com.privatecommunication.entity.ChatRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRequestsRepo extends JpaRepository<ChatRequestEntity, Long> {

    @Query(value = "SELECT request_id, sender_id, recipient_id, status FROM chat_requests WHERE recipient_id = :recipientId AND (status NOT LIKE 'Accepted' OR status NOT LIKE 'Rejected')", nativeQuery = true)
    List<ChatRequestEntity> findRequestForUser(@Param("recipientId") Long recipientId);

    @Query(value = "SELECT * FROM chat_requests WHERE sender_id = :senderId AND recipient_id = :recipientId", nativeQuery = true)
    Optional<ChatRequestEntity> findBySenderAndRecipient(@Param("senderId") Long senderId, @Param("recipientId") Long recipientId);


}
