/*
 * Copyright 2005-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openwms.core.uaa.app;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.Filter;
import jakarta.validation.Validator;
import org.ameba.annotation.EnableAspects;
import org.ameba.app.SpringProfiles;
import org.ameba.http.PermitAllCorsConfigurationSource;
import org.ameba.i18n.AbstractSpringTranslator;
import org.ameba.i18n.Translator;
import org.ameba.system.NestedReloadableResourceBundleMessageSource;
import org.openwms.core.uaa.impl.UserUpdater;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * A UAAModuleConfiguration.
 *
 * @author Heiko Scherrer
 */
@Configuration
@EnableJpaAuditing
@EnableAspects(propagateRootCause = true)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@EnablePluginRegistries({UserUpdater.class})
public class UAAModuleConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Profile(SpringProfiles.DEVELOPMENT_PROFILE)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public @Bean Filter corsFiler() {
        return new CorsFilter(new PermitAllCorsConfigurationSource());
    }

    @Bean MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(@Value("${spring.application.name}") String applicationName) {
        return registry -> registry.config().commonTags("application", applicationName);
    }

    @Bean MethodValidationPostProcessor methodValidationPostProcessor(Validator validator) {
        var mvpp = new MethodValidationPostProcessor();
        mvpp.setValidator(validator);
        return mvpp;
    }

    public @Bean LocaleResolver localeResolver() {
        var slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }

    public @Bean LocaleChangeInterceptor localeChangeInterceptor() {
        var lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    public @Bean Translator translator() {
        return new AbstractSpringTranslator() {
            @Override
            protected MessageSource getMessageSource() {
                return messageSource();
            }
        };
    }

    public @Bean MessageSource messageSource() {
        var nrrbm = new NestedReloadableResourceBundleMessageSource();
        nrrbm.setBasename("classpath*:/META-INF/i18n");
        nrrbm.setDefaultEncoding("UTF-8");
        return nrrbm;
    }
}
