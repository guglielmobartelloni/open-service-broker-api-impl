package com.yanchware.assesment.openservicebroker;


import org.springframework.cloud.servicebroker.model.catalog.Catalog;
import org.springframework.cloud.servicebroker.model.catalog.Plan;
import org.springframework.cloud.servicebroker.model.catalog.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogConfiguration {


    @Bean
    public Catalog catalog() {
        Plan freePlan = Plan.builder()
                .id("free-plan")
                .name("free")
                .description("The starting free plan")
                .free(true)
                .build();

        Plan enterprisePlan = Plan.builder()
                .id("enterprise-plan")
                .name("enterprise")
                .description("The enterprise plan")
                .free(false)
                .build();

        ServiceDefinition serviceDefinition = ServiceDefinition.builder()
                .id("server-wm")
                .name("wm")
                .description("A server in the cloud")
                .bindable(true)
                .plans(freePlan, enterprisePlan)
                .build();

        return Catalog.builder()
                .serviceDefinitions(serviceDefinition)
                .build();
    }

}

