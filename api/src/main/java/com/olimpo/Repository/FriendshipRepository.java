package com.olimpo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.olimpo.Entity.FriendEntity;

public interface FriendshipRepository extends JpaRepository<FriendEntity, Integer>{
    List<FriendEntity> findAllBySenderIdOrReceiverId(Integer senderId, Integer receiverId);
    Optional<FriendEntity> findBySenderIdAndReceiverId(Integer senderId, Integer receiverId);
    Optional<FriendEntity> findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
        Integer senderId1, Integer receiverId1,
        Integer senderId2, Integer receiverId2);
    Optional<FriendEntity> findById(Integer Id);
}
