package kz.iitu.medicineplatform.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Medicine Platform API",
        version = "1.0.0",
        description = "Plant, compound, source, activity measurement, and prototype prediction API."))
public class OpenApiConfig {
}
