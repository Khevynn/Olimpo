package com.olimpo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.olimpo.DTO.Requests.Profile.UpdateProfileRequestDTO;
import com.olimpo.DTO.Responses.APIResponse;
import com.olimpo.Routes.APIRoutes;
import com.olimpo.Services.ProfileService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProfileController {

    @Autowired
    private ProfileService profileServices;

    @GetMapping(APIRoutes.USER_GET_OWN_PROFILE_ROUTE)
    public ResponseEntity<APIResponse> callGetOwnProfile() {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return profileServices.getProfileByEmail(authenticatedUserEmail);
    }

    @GetMapping(APIRoutes.USER_GET_PROFILE_ROUTE)
    public ResponseEntity<APIResponse> callGetProfile(@PathVariable String user, @PathVariable String tag) {
        if(user.isEmpty() || tag.isEmpty()) {
            return ResponseEntity
                .badRequest()
                .body(new APIResponse("Usuário ou tag não encontrados."));
        }

        return profileServices.getProfileByUserAndTag(user, tag);
    }

    @PutMapping(APIRoutes.USER_UPDATE_PROFILE_ROUTE)
    public ResponseEntity<APIResponse> callUpdateProfile(@RequestBody @Valid UpdateProfileRequestDTO request) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return profileServices.updateProfile(request, authenticatedUserEmail);
    }

    @DeleteMapping(APIRoutes.USER_DELETE_ROUTE)
    public ResponseEntity<APIResponse> callDeleteUser() {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return profileServices.deleteUser(authenticatedUserEmail);
    }

}
