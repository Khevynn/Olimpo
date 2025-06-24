package com.olimpo.Services;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.olimpo.DTO.Requests.Friends.FriendRequestDTO;
import com.olimpo.DTO.Requests.Friends.FriendRespondRequestDTO;
import com.olimpo.DTO.Responses.APIResponse;
import com.olimpo.DTO.Responses.Friends.FriendDTO;
import com.olimpo.DTO.Responses.Friends.GetAllFriendsResponse;
import com.olimpo.Entity.FriendEntity;
import com.olimpo.Entity.UserEntity;
import com.olimpo.Enums.AccountStatus;
import com.olimpo.Enums.FriendRequestStatus;
import com.olimpo.Repository.FriendshipRepository;
import com.olimpo.Repository.UserRepository;
import com.olimpo.Utils.ProfileUtils;
import com.olimpo.Utils.ResponseUtils;

@Service
public class FriendshipService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    /**
     * Send a friend request from the authenticated user to another user.
     */
    public ResponseEntity<APIResponse> sendFriendRequest(FriendRequestDTO request, String authenticatedUserEmail) {
        try {
            if (ProfileUtils.isAccountBannedOrDeleted(userRepository, authenticatedUserEmail)) {
                return ResponseUtils.forbidden(ResponseUtils.ACCOUNT_BANNED_OR_DELETED);
            }
            
            UserEntity sender = ProfileUtils.getUserOrThrow(userRepository, authenticatedUserEmail);
            UserEntity receiver = ProfileUtils.getUserOrThrow(userRepository, request.getUsername(), request.getTag());

            if (sender.getId() == receiver.getId()) {
                return ResponseUtils.badRequest("Não pode enviar pedido de amizade para você mesmo");
            }

            Optional<FriendEntity> friendRequest = friendshipRepository.findBySenderIdAndReceiverId(sender.getId(), receiver.getId());
            if (friendRequest.isPresent()) {
                return ResponseUtils.conflict(
                    friendRequest.get().getStatus() == FriendRequestStatus.Accepted
                        ? "Vocês já são amigos"
                        : "Pedido de amizade já enviado anteriormente!"
                );
            }

            FriendEntity friendship = new FriendEntity();
            friendship.setSenderId(sender.getId());
            friendship.setReceiverId(receiver.getId());
            friendship.setStatus(FriendRequestStatus.Sent);
            friendshipRepository.save(friendship);

            return ResponseUtils.ok("Pedido de amizade enviado!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseUtils.serverError(ResponseUtils.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all friends and incoming friend requests for the authenticated user.
     */
    public ResponseEntity<APIResponse> getAllFriendsAndFriendRequests(String authenticatedUserEmail) {
        try {
            if (ProfileUtils.isAccountBannedOrDeleted(userRepository, authenticatedUserEmail)) {
                return ResponseUtils.forbidden(ResponseUtils.ACCOUNT_BANNED_OR_DELETED);
            }

            int userId = ProfileUtils.getUserOrThrow(userRepository, authenticatedUserEmail).getId();
            List<FriendEntity> allFriendships = friendshipRepository.findAllBySenderIdOrReceiverId(userId, userId);

            List<FriendEntity> friends = new ArrayList<>();
            List<FriendEntity> friendRequests = new ArrayList<>();
            Set<Integer> userIdsToFetch = new HashSet<>();

            for (FriendEntity friendship : allFriendships) {
                if (friendship.getStatus() == FriendRequestStatus.Accepted) {
                    friends.add(friendship);
                    userIdsToFetch.add(friendship.getSenderId() == userId ? friendship.getReceiverId() : friendship.getSenderId());
                } else if (friendship.getReceiverId() == userId) {
                    friendRequests.add(friendship);
                    userIdsToFetch.add(friendship.getSenderId());
                }
            }

            // Batch fetch all needed users
            Map<Integer, UserEntity> userMap = userRepository.findAllById(userIdsToFetch)
                .stream().collect(Collectors.toMap(UserEntity::getId, u -> u));

            // Map friends (show the "other" user, only if activated)
            List<FriendDTO> friendDTOs = friends.stream()
                .map(friend -> {
                    int otherUserId = friend.getSenderId() == userId ? friend.getReceiverId() : friend.getSenderId();
                    UserEntity otherUser = userMap.get(otherUserId);
                    return (otherUser != null && otherUser.getAccountStatus() == AccountStatus.Activated)
                        ? new FriendDTO(otherUser.getId(), otherUser.getUsername(), otherUser.getTag())
                        : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            // Map friend requests (show the sender, only if activated)
            List<FriendDTO> friendRequestDTOs = friendRequests.stream()
                .map(request -> {
                    UserEntity sender = userMap.get(request.getSenderId());
                    return (sender != null && sender.getAccountStatus() == AccountStatus.Activated)
                        ? new FriendDTO(sender.getId(), sender.getUsername(), sender.getTag())
                        : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            return ResponseEntity.ok(new GetAllFriendsResponse(friendDTOs, friendRequestDTOs, "Successfully Searched"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseUtils.serverError(ResponseUtils.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Respond to a friend request (accept or reject).
     */
    public ResponseEntity<APIResponse> respondFriendRequest(String requestId, FriendRespondRequestDTO request, String authenticatedUserEmail) {
        try {
            if (ProfileUtils.isAccountBannedOrDeleted(userRepository, authenticatedUserEmail)) {
                return ResponseUtils.forbidden(ResponseUtils.ACCOUNT_BANNED_OR_DELETED);
            }

            int userId = ProfileUtils.getUserOrThrow(userRepository, authenticatedUserEmail).getId();
            Optional<FriendEntity> friendRequestOpt = friendshipRepository.findById(Integer.parseInt(requestId));
            if (friendRequestOpt.isEmpty()) {
                return ResponseUtils.notFound("Pedido de amizade não encontrado.");
            }
            FriendEntity friendRequest = friendRequestOpt.get();

            if (friendRequest.getReceiverId() != userId) {
                return ResponseUtils.forbidden("Não foi possível encontrar o pedido de amizade em sua conta.");
            }

            FriendRequestStatus newStatus = switch (request.getStatus().toLowerCase()) {
                case "accepted" -> FriendRequestStatus.Accepted;
                case "rejected" -> FriendRequestStatus.Rejected;
                default -> null;
            };
            if (newStatus == null) {
                return ResponseUtils.badRequest("Não foi possível determinar os status do pedido.");
            }

            friendRequest.setStatus(newStatus);
            friendshipRepository.save(friendRequest);

            return ResponseUtils.ok("Pedido respondido com sucesso.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseUtils.serverError(ResponseUtils.INTERNAL_SERVER_ERROR);
        }
    }
}
