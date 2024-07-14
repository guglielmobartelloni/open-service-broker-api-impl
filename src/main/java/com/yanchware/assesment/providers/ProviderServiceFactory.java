package com.yanchware.assesment.providers;

import com.yanchware.assesment.providers.service.AwsService;
import com.yanchware.assesment.providers.service.CloudProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProviderServiceFactory {

    private final AwsService awsService;

    @Autowired
    public ProviderServiceFactory(AwsService awsService) {
        this.awsService = awsService;
    }

    public CloudProviderService getInstance(ProvidersEnum provider){
        return switch (provider){
            case AWS -> awsService;
            case GCP -> null;
        };

    }
}
