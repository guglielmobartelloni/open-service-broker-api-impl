package com.yanchware.assesment.providers.service;

import org.springframework.cloud.servicebroker.model.catalog.Plan;
import org.springframework.cloud.servicebroker.model.catalog.ServiceDefinition;
import org.springframework.cloud.servicebroker.model.instance.CreateServiceInstanceResponse;
import org.springframework.cloud.servicebroker.model.instance.DeleteServiceInstanceResponse;
import org.springframework.cloud.servicebroker.model.instance.GetServiceInstanceResponse;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

public interface CloudProviderService {
    Mono<CreateServiceInstanceResponse> createServiceInstance(Authentication authentication, String serviceInstanceId, Plan plan, ServiceDefinition serviceDefinition);
    Mono<GetServiceInstanceResponse> getServiceInstance(Authentication authentication, String serviceInstanceId);
    Mono<DeleteServiceInstanceResponse> deleteServiceInstance(Authentication authentication, String serviceInstanceId);
}
