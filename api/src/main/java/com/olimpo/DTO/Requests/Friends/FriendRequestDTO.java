package com.olimpo.DTO.Requests.Friends;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FriendRequestDTO {

    @NotBlank(message = "Campos obrigatórios ausentes ou formato inválido.")
    private String username;

    @NotBlank(message = "Campos obrigatórios ausentes ou formato inválido.")
    private String tag;

    public FriendRequestDTO(String username, String tag){
        this.username = username;
        this.tag = tag;
    }
}
