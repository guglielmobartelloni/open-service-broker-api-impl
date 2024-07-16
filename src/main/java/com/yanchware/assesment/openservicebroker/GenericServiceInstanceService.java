package com.yanchware.assesment.openservicebroker;

import com.yanchware.assesment.providers.ProviderServiceFactory;
import com.yanchware.assesment.providers.ProvidersEnum;
import com.yanchware.assesment.providers.service.CloudProviderService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.servicebroker.model.instance.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.security.core.context.SecurityContextHolder;
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
        log.debug("Platform id: {}, Instance id: {}, Plan Id: {}, Service definition id: {}",
                createServiceInstanceRequest.getPlatformInstanceId(),
                createServiceInstanceRequest.getServiceInstanceId(),
                createServiceInstanceRequest.getPlanId(),
                createServiceInstanceRequest.getServiceDefinitionId());

        CloudProviderService providerService = this.getProviderService(createServiceInstanceRequest.getPlatformInstanceId());

        return providerService
                .createServiceInstance(
                        SecurityContextHolder.getContext().getAuthentication(),
                        createServiceInstanceRequest.getServiceInstanceId(),
                        createServiceInstanceRequest.getPlan(),
                        createServiceInstanceRequest.getServiceDefinition());

    }

    @Override
    public Mono<GetServiceInstanceResponse> getServiceInstance(GetServiceInstanceRequest request) {
        CloudProviderService providerService = this.getProviderService(request.getPlatformInstanceId());

        return providerService
                .getServiceInstance(
                        SecurityContextHolder.getContext().getAuthentication(),
                        request.getServiceInstanceId(),
                        request.getPlanId(),
                        request.getServiceDefinitionId()
                );
    }

    @Override
    public Mono<DeleteServiceInstanceResponse> deleteServiceInstance(DeleteServiceInstanceRequest deleteServiceInstanceRequest) {
        CloudProviderService providerService = this.getProviderService(deleteServiceInstanceRequest.getPlatformInstanceId());

        return providerService
                .deleteServiceInstance(
                        SecurityContextHolder.getContext().getAuthentication(),
                        deleteServiceInstanceRequest.getServiceInstanceId(),
                        deleteServiceInstanceRequest.getPlan(),
                        deleteServiceInstanceRequest.getServiceDefinition());
    }

    private CloudProviderService getProviderService(@NonNull String platformId) {
        return providerServiceFactory
                .getInstance(ProvidersEnum.valueOf(platformId.toUpperCase()));
    }
}
