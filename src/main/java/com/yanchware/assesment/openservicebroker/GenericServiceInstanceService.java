package com.yanchware.assesment.openservicebroker;

import ch.qos.logback.core.util.StringUtil;
import com.yanchware.assesment.providers.ProviderServiceFactory;
import com.yanchware.assesment.providers.ProvidersEnum;
import com.yanchware.assesment.providers.service.CloudProviderService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.servicebroker.model.instance.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GenericServiceInstanceService implements ServiceInstanceService {

    private final ProviderServiceFactory providerServiceFactory;

    public GenericServiceInstanceService(ProviderServiceFactory providerServiceFactory) {
        this.providerServiceFactory = providerServiceFactory;
    }

    @Override
    public Mono<CreateServiceInstanceResponse> createServiceInstance(CreateServiceInstanceRequest createServiceInstanceRequest) {
        log.debug("Platform id: {}, Instance id: {}, Plan Id: {}",
                createServiceInstanceRequest.getPlatformInstanceId(),
                createServiceInstanceRequest.getServiceInstanceId(),
                createServiceInstanceRequest.getPlan().getId());


        CloudProviderService providerService = this.getProviderService(createServiceInstanceRequest.getPlatformInstanceId());

        return Mono.just(CreateServiceInstanceResponse.builder().dashboardUrl("https://google.it").build());
    }

    @Override
    public Mono<GetServiceInstanceResponse> getServiceInstance(GetServiceInstanceRequest request) {
        return ServiceInstanceService.super.getServiceInstance(request);
    }

    @Override
    public Mono<DeleteServiceInstanceResponse> deleteServiceInstance(DeleteServiceInstanceRequest deleteServiceInstanceRequest) {
        return null;
    }

    private CloudProviderService getProviderService(@NonNull String platformId){
        return providerServiceFactory
                .getInstance(ProvidersEnum.valueOf(platformId.toUpperCase()));
    }
}
