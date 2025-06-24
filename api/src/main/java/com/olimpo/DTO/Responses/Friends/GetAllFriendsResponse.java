package com.olimpo.DTO.Responses.Friends;

import java.util.ArrayList;
import java.util.List;

import com.olimpo.DTO.Responses.APIResponse;
import lombok.Getter;

@Getter
public class GetAllFriendsResponse extends APIResponse {
    private List<FriendDTO> friends = new ArrayList<>();
    private List<FriendDTO> friendRequests = new ArrayList<>();

    public GetAllFriendsResponse(List<FriendDTO> friends, List<FriendDTO> friendRequests, String message){
        super(message);
        this.friends = friends;
        this.friendRequests = friendRequests;
    }
}
