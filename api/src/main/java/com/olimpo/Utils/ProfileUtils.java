package com.olimpo.Utils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.olimpo.Entity.UserEntity;
import com.olimpo.Enums.AccountStatus;
import com.olimpo.Repository.UserRepository;

public class ProfileUtils {
    
    // Constants
    public static final int MIN_TAG_LENGTH = 4;
    public static final int MAX_TAG_LENGTH = 5;
    private static final int MAX_TAG_GENERATION_ATTEMPTS = 100;
    private static final String TAG_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    
    // Public Methods
    public static Boolean isAccountBannedOrDeleted(UserEntity user) {
        return user.getAccountStatus() != AccountStatus.Activated;
    }
    public static Boolean isAccountBannedOrDeleted(UserRepository userRepository, String email) {
        UserEntity user = getUserOrThrow(userRepository, email);
        return user.getAccountStatus() != AccountStatus.Activated;
    }

    public static String generateUniqueTag(List<UserEntity> existingUsers) {
        Set<String> usedTags = extractUsedTags(existingUsers);
        return generateUniqueTagWithRetries(usedTags);
    }

    public static UserEntity getUserOrThrow(UserRepository userRepository, String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email de usuário não encontrado."));
    }
    public static UserEntity getUserOrThrow(UserRepository userRepository, String username, String tag) {
        return userRepository.findByUsernameAndTag(username, tag)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário e tag não encontrados."));
    }

    // Private Methods
    private static Set<String> extractUsedTags(List<UserEntity> users) {
        return users.stream()
            .map(UserEntity::getTag)
            .collect(Collectors.toSet());
    }

    private static String generateUniqueTagWithRetries(Set<String> usedTags) {
        int attempts = 0;
        String tag;
        do {
            tag = generateRandomTag();
            attempts++;
            if (attempts >= MAX_TAG_GENERATION_ATTEMPTS) {
                return null;
            }
        } while (usedTags.contains(tag));

        return tag;
    }

    private static String generateRandomTag() {
        StringBuilder tag = new StringBuilder();
        for (int i = 0; i < MIN_TAG_LENGTH; i++) {
            tag.append(TAG_CHARS.charAt((int)(Math.random() * TAG_CHARS.length())));
        }
        return tag.toString();
    }
}
