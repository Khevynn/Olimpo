package com.olimpo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.olimpo.DTO.Requests.LoginRequestDTO;
import com.olimpo.DTO.Requests.RegisterRequestDTO;
import com.olimpo.DTO.Requests.UpdateProfileRequestDTO;

import com.olimpo.DTO.Responses.APIResponse;
import com.olimpo.Repository.UserRepository;
import com.olimpo.Routes.APIRoutes;
import com.olimpo.Services.UserServices;

import jakarta.validation.Valid;

@RestController
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

    @CrossOrigin(origins = "*")
    @PostMapping(APIRoutes.USER_REGISTER_ROUTE)
    public ResponseEntity<APIResponse> CallRegistration(@RequestBody @Valid RegisterRequestDTO request) {

        //Check if email is duplicated
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {  
            return ResponseEntity
                    .badRequest()
                    .body(new APIResponse("E-mail já existe.")); 
        }
        
        //Check if password is strong
        if (!isPasswordStrong(request.getPassword())) {
            return ResponseEntity
                .badRequest()
                .body(new APIResponse("A senha deve conter pelo menos 8 caracteres, uma letra maiúscula, uma letra minúscula e um número."));
        }

        return UserServices.RegisterUser(request, userRepository);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(APIRoutes.USER_LOGIN_ROUTE)
    public ResponseEntity<APIResponse> CallLogin(@RequestBody @Valid LoginRequestDTO request) {
        return UserServices.LoginUser(request, userRepository);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(APIRoutes.USER_GET_PROFILE_ROUTE)
    public ResponseEntity<APIResponse> CallGetProfile(@PathVariable String user, @PathVariable String tag) {

        if(user.isEmpty() || tag.isEmpty()) {
            return ResponseEntity
                .badRequest()
                .body(new APIResponse("Usuário ou tag não encontrados."));
        }

        return UserServices.GetProfile(user, tag, userRepository);
    }

    @CrossOrigin(origins = "*")
    @PutMapping(APIRoutes.USER_UPDATE_PROFILE_ROUTE)
    public ResponseEntity<APIResponse> CallUpdateProfile(@RequestBody @Valid UpdateProfileRequestDTO request, @PathVariable String user, @PathVariable String tag) {

        if(user.isEmpty() || tag.isEmpty()) {
            return ResponseEntity
                .badRequest()
                .body(new APIResponse("Campos obrigatórios ausentes."));
        }

        //Check if the new password and the confirm new password are the same
        if(!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            return ResponseEntity
                .badRequest()
                .body(new APIResponse("As senhas não coincidem."));
        }

        //Check if new password is strong
        if (!isPasswordStrong(request.getNewPassword())) {
            return ResponseEntity
                .badRequest()
                .body(new APIResponse("A nova senha deve conter pelo menos 8 caracteres, uma letra maiúscula, uma letra minúscula e um número."));
        }

        return UserServices.UpdateProfile(request, user, tag, userRepository);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(APIRoutes.USER_DELETE_ROUTE)
    public ResponseEntity<APIResponse> CallDeleteUser(@PathVariable String user, @PathVariable String tag){
        
        if(user.isEmpty() || tag.isEmpty()) {
            return ResponseEntity
                .badRequest()
                .body(new APIResponse("Campos obrigatórios ausentes."));
        }

        return UserServices.DeleteUser(user, tag, userRepository);
    }

    @CrossOrigin(origins = "*")
    private static boolean isPasswordStrong(String password) {
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*[a-z].*") &&
               password.matches(".*\\d.*");
    }
}
