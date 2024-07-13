package com.yanchware.assesment.openservicebroker;

import org.springframework.cloud.servicebroker.model.instance.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AwsServiceInstanceService implements ServiceInstanceService {
    @Override
    public Mono<CreateServiceInstanceResponse> createServiceInstance(CreateServiceInstanceRequest createServiceInstanceRequest) {
        return null;
    }

    @Override
    public Mono<GetServiceInstanceResponse> getServiceInstance(GetServiceInstanceRequest request) {
        return ServiceInstanceService.super.getServiceInstance(request);
    }

    @Override
    public Mono<DeleteServiceInstanceResponse> deleteServiceInstance(DeleteServiceInstanceRequest deleteServiceInstanceRequest) {
        return null;
    }
}
