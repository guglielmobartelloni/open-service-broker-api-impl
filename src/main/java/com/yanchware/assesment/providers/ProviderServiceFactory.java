package com.yanchware.assesment.providers;

import com.yanchware.assesment.providers.service.AwsService;
import com.yanchware.assesment.providers.service.CloudProviderService;
import com.yanchware.assesment.providers.service.GoogleCloudService;
import org.springframework.stereotype.Component;

@Component
public class ProviderServiceFactory {

    private final AwsService awsService;
    private final GoogleCloudService googleCloudService;

    public ProviderServiceFactory(AwsService awsService, GoogleCloudService googleCloudService) {
        this.awsService = awsService;
        this.googleCloudService = googleCloudService;
    }

    public CloudProviderService getInstance(ProvidersEnum provider){
        return switch (provider){
            case AWS -> awsService;
            case GCP -> googleCloudService;
        };

    }
}
