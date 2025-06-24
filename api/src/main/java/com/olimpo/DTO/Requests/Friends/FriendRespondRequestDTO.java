package com.olimpo.DTO.Requests.Friends;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FriendRespondRequestDTO {
    @NotBlank(message = "Campos obrigatórios ausentes ou formato inválido.")
    private String status;

    public FriendRespondRequestDTO(String status){
        this.status = status;
    }
}
