package kz.iitu.herbalmedicineplatform.security.dto;

import jakarta.validation.constraints.NotNull;
import kz.iitu.herbalmedicineplatform.user.UserRole;

public record UpdateUserRoleRequest(@NotNull UserRole role) {
}
