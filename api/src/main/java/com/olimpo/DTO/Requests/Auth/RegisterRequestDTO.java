package com.olimpo.DTO.Requests.Auth;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RegisterRequestDTO {

    @NotBlank(message = "O email é obrigatório")
    private String email;

    @NotBlank(message = "O nome de usuário é obrigatório")
    private String user;

    @NotBlank(message = "A senha é obrigatório")
    private String password;

    public RegisterRequestDTO(String email, String user, String password){
        this.email = email;
        this.user = user;
        this.password = password;
    }
}
