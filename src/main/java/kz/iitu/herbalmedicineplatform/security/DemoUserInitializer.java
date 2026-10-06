package kz.iitu.herbalmedicineplatform.security;

import kz.iitu.herbalmedicineplatform.user.AppUser;
import kz.iitu.herbalmedicineplatform.user.AppUserRepository;
import kz.iitu.herbalmedicineplatform.user.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;

@Component
@Profile("demo")
public class DemoUserInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoUserInitializer.class);

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DemoUserInitializer(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.existsByEmailIgnoreCase("admin@herbal.local")) {
            return;
        }
        userRepository.save(new AppUser(
                "Администратор",
                "admin@herbal.local",
                passwordEncoder.encode("admin123"),
                UserRole.ADMIN
        ));
        log.info("Created demo admin user: admin@herbal.local / admin123");
    }
}
