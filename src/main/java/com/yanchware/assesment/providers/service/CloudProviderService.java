package com.yanchware.assesment.providers.service;

import org.springframework.cloud.servicebroker.model.instance.CreateServiceInstanceResponse;
import reactor.core.publisher.Mono;

public interface CloudProviderService {
    Mono<CreateServiceInstanceResponse> createVm();
}
