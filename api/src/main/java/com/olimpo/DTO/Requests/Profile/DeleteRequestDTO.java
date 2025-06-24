package com.olimpo.DTO.Requests.Profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeleteRequestDTO {
    
    @NotBlank(message = "Campos obrigatórios ausentes ou formato inválido.")
    private String userEmail;

    public DeleteRequestDTO(String userEmail){
        this.userEmail = userEmail;
    }
}
