package com.olimpo.Services;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
     * Sends a friend request from the authenticated user to another user.
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

            // Search for friendship regardless of the order of users
            Optional<FriendEntity> friendRequest = friendshipRepository
                .findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
                    sender.getId(), receiver.getId(),
                    receiver.getId(), sender.getId()
                );
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
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseUtils.notFound(e.getMessage());
            }
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao enviar pedido de amizade: " + e.getMessage());
            return ResponseUtils.serverError("Erro interno ao enviar pedido de amizade.");
        }
    }

    /**
     * Returns all friends and received friend requests for the authenticated user.
     *
     * Uses a Set to collect all related user IDs (friends and requesters),
     * avoiding duplicates and optimizing batch fetching from the database.
     * The Map is used to quickly map the user ID to the corresponding UserEntity object,
     * making it easier to build the response DTOs.
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
            // Set to ensure there are no duplicate IDs when fetching related users
            Set<Integer> userIdsToFetch = new HashSet<>();

            for (FriendEntity friendship : allFriendships) {
                if (friendship.getStatus() == FriendRequestStatus.Accepted) {
                    friends.add(friendship);
                    // Add the ID of the other user involved in the friendship
                    userIdsToFetch.add(friendship.getSenderId() == userId ? friendship.getReceiverId() : friendship.getSenderId());
                } else if (friendship.getReceiverId() == userId) {
                    friendRequests.add(friendship);
                    // Add the ID of the sender of the friend request
                    userIdsToFetch.add(friendship.getSenderId());
                }
            }

            // Batch fetch all required users and map by ID for quick access
            Map<Integer, UserEntity> userMap = userRepository.findAllById(userIdsToFetch)
                .stream().collect(Collectors.toMap(UserEntity::getId, u -> u));

            // Build the list of friends (show only the other user, if activated)
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

            // Build the list of received friend requests (show only the sender, if activated)
            List<FriendDTO> friendRequestDTOs = friendRequests.stream()
                .map(request -> {
                    UserEntity sender = userMap.get(request.getSenderId());
                    return (sender != null && sender.getAccountStatus() == AccountStatus.Activated)
                        ? new FriendDTO(sender.getId(), sender.getUsername(), sender.getTag())
                        : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            return ResponseEntity.ok(new GetAllFriendsResponse(friendDTOs, friendRequestDTOs, "Busca realizada com sucesso."));
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseUtils.notFound(e.getMessage());
            }
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao buscar amigos/pedidos: " + e.getMessage());
            return ResponseUtils.serverError("Erro interno ao buscar amigos/pedidos.");
        }
    }

    /**
     * Responds to a friend request (accept or reject).
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
                return ResponseUtils.badRequest("Status do pedido inválido. Use 'accepted' ou 'rejected'.");
            }

            friendRequest.setStatus(newStatus);
            friendshipRepository.save(friendRequest);

            return ResponseUtils.ok("Pedido respondido com sucesso.");
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseUtils.notFound(e.getMessage());
            }
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao responder pedido de amizade: " + e.getMessage());
            return ResponseUtils.serverError("Erro interno ao responder pedido de amizade.");
        }
    }

    /**
     * Removes a friendship between the authenticated user and another user, regardless of the order (sender/receiver).
     */
    public ResponseEntity<APIResponse> deleteFriend(FriendRequestDTO request, String authenticatedUserEmail) {
        try {
            if (ProfileUtils.isAccountBannedOrDeleted(userRepository, authenticatedUserEmail)) {
                return ResponseUtils.forbidden(ResponseUtils.ACCOUNT_BANNED_OR_DELETED);
            }

            int userId = ProfileUtils.getUserOrThrow(userRepository, authenticatedUserEmail).getId();
            UserEntity otherUser = ProfileUtils.getUserOrThrow(userRepository, request.getUsername(), request.getTag());

            // Search for friendship regardless of the order of users
            Optional<FriendEntity> friendshipOpt = friendshipRepository
                .findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
                    userId, otherUser.getId(),
                    otherUser.getId(), userId
                );
            if (friendshipOpt.isEmpty()) {
                return ResponseUtils.notFound("Amizade não encontrada.");
            }
            FriendEntity friend = friendshipOpt.get();
            friendshipRepository.delete(friend);

            return ResponseUtils.ok("Amizade removida com sucesso!");
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseUtils.notFound(e.getMessage());
            }
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao remover amizade: " + e.getMessage());
            return ResponseUtils.serverError("Erro interno ao remover amizade.");
        }
    }
}
