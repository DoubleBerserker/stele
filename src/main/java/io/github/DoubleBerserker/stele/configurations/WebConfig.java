package io.github.DoubleBerserker.stele.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${image.uploadDir}")
    private String imageUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Make the images directory accessible to Thymeleaf
        registry.addResourceHandler("/images/*").addResourceLocations("file:" + imageUploadDir);
    }
}
