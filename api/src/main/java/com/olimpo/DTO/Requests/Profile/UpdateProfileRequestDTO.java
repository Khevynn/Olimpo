package com.olimpo.DTO.Requests.Profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateProfileRequestDTO {

    @NotBlank(message = "Campos obrigatórios ausentes ou formato inválido.")
    private String newUsername;

    @NotBlank(message = "Campos obrigatórios ausentes ou formato inválido.")
    private String newTag;

    @NotBlank(message = "Campos obrigatórios ausentes ou formato inválido.")
    private String newDescription;

    @NotBlank(message = "Campos obrigatórios ausentes ou formato inválido.")
    private String oldPassword;

    @NotBlank(message = "Campos obrigatórios ausentes ou formato inválido.")
    private String newPassword;

    @NotBlank(message = "Campos obrigatórios ausentes ou formato inválido.")
    private String confirmNewPassword;
    
    @NotBlank(message = "Campos obrigatórios ausentes ou formato inválido.")
    private String valorantUsername;

    @NotBlank(message = "Campos obrigatórios ausentes ou formato inválido.")
    private String valorantTag;

    public UpdateProfileRequestDTO(String newUsername, String oldPassword, 
                                    String newPassword, String confirmNewPassword, String newTag, 
                                    String newDescription, String valorantUsername, String valorantTag) {
        this.newUsername = newUsername;
        this.newTag = newTag;
        this.newDescription = newDescription;

        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;

        this.valorantUsername = valorantUsername;
        this.valorantTag = valorantTag;
    }
}
