package me.javaroad.oauth.config;

import me.javaroad.data.config.JpaAuditingConfig;
import me.javaroad.web.config.SimpleSwaggerConfig;
import me.javaroad.web.exception.SimpleExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author heyx
 */
@Import({
    SimpleSwaggerConfig.class,
    JpaAuditingConfig.class,
    SimpleExceptionHandler.class
})
@Configuration
public class AppConfig {

}

