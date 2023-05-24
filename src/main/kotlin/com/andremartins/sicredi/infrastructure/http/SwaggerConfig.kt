package com.andremartins.sicredi.infrastructure.http

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class SwaggerConfig : WebMvcConfigurer {

    @Bean
    fun openApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("api")
            .packagesToScan("com.andremartins.sicredi")
            .build()
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
    }
}