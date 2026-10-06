package kz.iitu.herbalmedicineplatform.security.dto;

import kz.iitu.herbalmedicineplatform.user.AppUser;
import kz.iitu.herbalmedicineplatform.user.UserRole;

public record AuthUserResponse(Long id, String fullName, String email, UserRole role) {

    public static AuthUserResponse from(AppUser user) {
        return new AuthUserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole());
    }
}
