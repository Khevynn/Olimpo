package com.olimpo.DTO.Responses.Friends;

import lombok.Getter;

@Getter
public class FriendDTO {
    private int id;
    private String username;
    private String tag;

    public FriendDTO (int id, String username, String tag){
        this.id = id;
        this.username = username;
        this.tag = tag;
    }
}
