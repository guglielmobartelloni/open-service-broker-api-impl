package com.yanchware.assesment.providers.service;

import org.springframework.cloud.servicebroker.model.instance.CreateServiceInstanceResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GoogleCloudService implements CloudProviderService{

    @Override
    public Mono<CreateServiceInstanceResponse> createVm(String instanceId) {
        return null;
    }
}
