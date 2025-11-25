package org.sopt.web1.global.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SOPT Web1 API 명세서")
                        .description("SOPT 37기 Web1팀 API 명세서입니다.")
                        .version("1.0.0"))
                .addServersItem(new Server().url("http://localhost:8080").description("Local"))
                .addServersItem(new Server().url("https://nvp.kr").description("Production"));

    }
}
