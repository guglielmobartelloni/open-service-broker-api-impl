package com.yanchware.assesment.providers;

import com.yanchware.assesment.providers.service.AwsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProviderServiceFactoryTest {


    @Autowired
    private ProviderServiceFactory providerServiceFactory;

    @Test
    void getAwsInstanceTest() {
        assertThat(providerServiceFactory.getInstance(ProvidersEnum.AWS))
                .isInstanceOf(AwsService.class);
    }

    @Test
    void getGcpInstanceTest() {
        assertThat(providerServiceFactory.getInstance(ProvidersEnum.GCP))
                .isNull();
    }
}