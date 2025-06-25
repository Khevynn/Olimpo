package com.olimpo.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.olimpo.DTO.Requests.Profile.UpdateProfileRequestDTO;
import com.olimpo.DTO.Responses.APIResponse;
import com.olimpo.DTO.Responses.Profile.GetProfileResponse;
import com.olimpo.Entity.UserEntity;
import com.olimpo.Enums.AccountStatus;
import com.olimpo.Repository.UserRepository;
import com.olimpo.Utils.PasswordUtils;
import com.olimpo.Utils.ProfileUtils;
import com.olimpo.Utils.ResponseUtils;

@Service
public class ProfileService {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<APIResponse> deleteUser(String authenticatedUserEmail) {
        try {
            UserEntity user = ProfileUtils.getUserOrThrow(userRepository, authenticatedUserEmail);

            if(ProfileUtils.isAccountBannedOrDeleted(user)){
                return ResponseUtils.forbidden(ResponseUtils.ACCOUNT_BANNED_OR_DELETED);
            }
            
            user.setAccountStatus(AccountStatus.Deleted);
            userRepository.save(user);

            return ResponseUtils.ok("Profile deleted successfully.");

        } catch(ResponseStatusException e){
            System.out.println(e.getMessage());
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                return ResponseUtils.notFound(e.getMessage());
            }
            return ResponseUtils.serverError(ResponseUtils.INTERNAL_SERVER_ERROR);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return ResponseUtils.serverError(ResponseUtils.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<APIResponse> getProfileByUserAndTag(String user, String tag) {
        try {
            Optional<UserEntity> userSearched = userRepository.findByUsernameAndTag(user, tag);

            if(userSearched.isEmpty()) {
                return ResponseUtils.notFound(ResponseUtils.USER_NOT_FOUND);
            }

            String userEmail = userSearched.get().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())
                                ? userSearched.get().getEmail() 
                                : null;

            return ResponseEntity.ok(new GetProfileResponse(
                userSearched.get().getUsername(),
                userSearched.get().getTag(),
                userEmail,
                userSearched.get().getDescription(),
                userSearched.get().getValorantUsername(),
                userSearched.get().getValorantTag(),
                userSearched.get().getAccountStatus(),
                "Profile loaded successfully."
            ));

        } catch(Exception e) {
            System.out.println(e.getMessage());
            return ResponseUtils.serverError(ResponseUtils.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<APIResponse> getProfileByEmail(String email) {
        try {
            UserEntity user = ProfileUtils.getUserOrThrow(userRepository, email);

            return ResponseEntity.ok(new GetProfileResponse(
                user.getUsername(),
                user.getTag(),
                user.getEmail(),
                user.getDescription(),
                user.getValorantUsername(),
                user.getValorantTag(),
                user.getAccountStatus(),
                "Profile loaded successfully."
            ));

        } catch(ResponseStatusException e){
            System.out.println(e.getMessage());
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                return ResponseUtils.notFound(e.getMessage());
            }
            return ResponseUtils.serverError(ResponseUtils.INTERNAL_SERVER_ERROR);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return ResponseUtils.serverError(ResponseUtils.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<APIResponse> updateProfile(UpdateProfileRequestDTO request, String authenticatedUserEmail) {
        try {
            UserEntity user = ProfileUtils.getUserOrThrow(userRepository, authenticatedUserEmail);

            if(ProfileUtils.isAccountBannedOrDeleted(user)){
                return ResponseUtils.forbidden(ResponseUtils.ACCOUNT_BANNED_OR_DELETED);
            }

            ResponseEntity<APIResponse> validationResponse = validateProfileUpdate(request, user);
            if (validationResponse != null) {
                return validationResponse;
            }

            updateUserProfile(user, request);
            return ResponseUtils.ok("Profile updated successfully.");

        } catch(ResponseStatusException e){
            System.out.println(e.getMessage());
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                return ResponseUtils.notFound(e.getMessage());
            }
            return ResponseUtils.serverError(ResponseUtils.INTERNAL_SERVER_ERROR);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return ResponseUtils.serverError(ResponseUtils.INTERNAL_SERVER_ERROR);
        }
    }

    // Private Methods - Validation
    private ResponseEntity<APIResponse> validateProfileUpdate(UpdateProfileRequestDTO request, UserEntity user) {
        if(!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            return ResponseUtils.badRequest(ResponseUtils.PASSWORDS_DONT_MATCH);
        }

        if (!PasswordUtils.isPasswordStrong(request.getNewPassword())) {
            return ResponseUtils.badRequest(ResponseUtils.INVALID_PASSWORD_FORMAT);
        }
        
        if(request.getNewTag().length() > ProfileUtils.MAX_TAG_LENGTH) {
            return ResponseUtils.badRequest(ResponseUtils.INVALID_TAG_LENGTH);
        }

        if(!request.getNewTag().matches("^[A-Za-z0-9]+$")) {
            return ResponseUtils.badRequest(ResponseUtils.INVALID_TAG_FORMAT);
        }
        
        if(!PasswordUtils.passwordMatches(request.getOldPassword(), user.getPassword())) {
            return ResponseUtils.unauthorized("Invalid old password.");
        }

        Optional<UserEntity> existingUser = userRepository.findByUsernameAndTag(request.getNewUsername(), request.getNewTag());
        if(!existingUser.isEmpty()) {
            return ResponseUtils.conflict(ResponseUtils.USERNAME_TAG_IN_USE);
        }

        return null;
    }

    // Private Methods - Profile Management
    private void updateUserProfile(UserEntity user, UpdateProfileRequestDTO request) {
        user.setUsername(request.getNewUsername());
        user.setTag(request.getNewTag());
        user.setDescription(request.getNewDescription());
        user.setPassword(PasswordUtils.getEncodedPassword(request.getNewPassword()));
        user.setValorantUsername(request.getValorantUsername());
        user.setValorantTag(request.getValorantTag());
        userRepository.save(user);
    }
}