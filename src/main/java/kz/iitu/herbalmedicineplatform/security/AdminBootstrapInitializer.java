package kz.iitu.herbalmedicineplatform.security;

import kz.iitu.herbalmedicineplatform.user.AppUser;
import kz.iitu.herbalmedicineplatform.user.AppUserRepository;
import kz.iitu.herbalmedicineplatform.user.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminBootstrapInitializer implements ApplicationRunner {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String email;
    private final String password;
    private final String fullName;

    public AdminBootstrapInitializer(AppUserRepository userRepository, PasswordEncoder passwordEncoder,
                                     @Value("${app.bootstrap-admin.email:}") String email,
                                     @Value("${app.bootstrap-admin.password:}") String password,
                                     @Value("${app.bootstrap-admin.full-name:Administrator}") String fullName) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.email = email.trim().toLowerCase(java.util.Locale.ROOT);
        this.password = password;
        this.fullName = fullName;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (email.isEmpty() && password.isEmpty()) {
            return;
        }
        if (email.isEmpty() || password.length() < 12) {
            throw new IllegalStateException("Configure both app.bootstrap-admin.email and a password of at least 12 characters");
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            return;
        }
        userRepository.save(new AppUser(fullName, email, passwordEncoder.encode(password), UserRole.ADMIN));
    }
}
