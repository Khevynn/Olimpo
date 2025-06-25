package com.olimpo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.olimpo.DTO.Requests.Friends.FriendRequestDTO;
import com.olimpo.DTO.Requests.Friends.FriendRespondRequestDTO;
import com.olimpo.DTO.Responses.APIResponse;
import com.olimpo.Routes.APIRoutes;
import com.olimpo.Services.FriendshipService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FriendshipController {
    @Autowired
    private FriendshipService friendshipService;

    @PostMapping(APIRoutes.FRIENDS_SEND)
    public ResponseEntity<APIResponse> sendRequest(@RequestBody FriendRequestDTO request) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return friendshipService.sendFriendRequest(request, authenticatedUserEmail);
    }
    
    @GetMapping(APIRoutes.FRIENDS_GET_ALL)
    public ResponseEntity<APIResponse> getAllFriendsAndFriendRequests() {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return friendshipService.getAllFriendsAndFriendRequests(authenticatedUserEmail);
    }

    @PostMapping(APIRoutes.FRIENDS_RESPOND)
    public ResponseEntity<APIResponse> respondFriendRequest(@PathVariable String requestId, @RequestBody FriendRespondRequestDTO request) {
        if(requestId.isEmpty()){
            return ResponseEntity
                .badRequest()
                .body(new APIResponse("Pedido de amizade não encontrado."));
        }

        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return friendshipService.respondFriendRequest(requestId, request, authenticatedUserEmail);
    }

    @DeleteMapping(APIRoutes.FRIENDS_DELETE)
    public ResponseEntity<APIResponse> deleteFriend(@RequestBody FriendRequestDTO request) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return friendshipService.deleteFriend(request, authenticatedUserEmail);
    }
}
