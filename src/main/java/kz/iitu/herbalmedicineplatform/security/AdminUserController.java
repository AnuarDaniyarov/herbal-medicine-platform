package kz.iitu.herbalmedicineplatform.security;

import jakarta.validation.Valid;
import kz.iitu.herbalmedicineplatform.common.exception.ResourceNotFoundException;
import kz.iitu.herbalmedicineplatform.security.dto.AuthUserResponse;
import kz.iitu.herbalmedicineplatform.security.dto.UpdateUserRoleRequest;
import kz.iitu.herbalmedicineplatform.user.AppUser;
import kz.iitu.herbalmedicineplatform.user.AppUserRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AppUserRepository userRepository;

    public AdminUserController(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping("/{id}/role")
    public AuthUserResponse updateRole(@PathVariable Long id,
                                       @Valid @RequestBody UpdateUserRoleRequest request) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id.toString()));
        user.setRole(request.role());
        return AuthUserResponse.from(userRepository.save(user));
    }
}
