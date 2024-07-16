package com.yanchware.assesment.providers.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Async;
import com.amazonaws.services.ec2.AmazonEC2AsyncClient;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*;
import com.yanchware.assesment.security.AuthUtils;
import com.yanchware.assesment.user.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.servicebroker.model.catalog.Plan;
import org.springframework.cloud.servicebroker.model.catalog.ServiceDefinition;
import org.springframework.cloud.servicebroker.model.instance.CreateServiceInstanceResponse;
import org.springframework.cloud.servicebroker.model.instance.DeleteServiceInstanceResponse;
import org.springframework.cloud.servicebroker.model.instance.GetServiceInstanceResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.Future;


@Service
@Slf4j
public class AwsService implements CloudProviderService {


    public Mono<CreateServiceInstanceResponse> createVm(Authentication auth, Plan plan, ServiceDefinition serviceDefinition) {

        AmazonEC2 ec2 = getAmazonClient(auth);

        RunInstancesRequest runInstancesRequest = new RunInstancesRequest("ami-08734ec479a1ace4a", 1, 1);
        Instance createdInstance = ec2.runInstances(runInstancesRequest)
                .getReservation()
                .getInstances()
                .stream().findFirst()
                .orElseThrow();

        return Mono.just(CreateServiceInstanceResponse.builder().instanceExisted(true)
                .dashboardUrl(createdInstance.getPublicIpAddress()).build());
    }


    @Override
    public Mono<CreateServiceInstanceResponse> createServiceInstance(Authentication authentication, String serviceInstanceId, Plan plan, ServiceDefinition serviceDefinition) {

        return switch (serviceDefinition.getId()) {
            case "server-vm" -> createVm(authentication, plan, serviceDefinition);
            default -> throw new IllegalStateException("Unexpected value: " + serviceDefinition.getId());
        };
    }

    @Override
    public Mono<GetServiceInstanceResponse> getServiceInstance(Authentication authentication, String serviceInstanceId, String planId, String serviceDefinitionId) {
        return switch (serviceDefinitionId) {
            case "server-vm" -> Mono.just(GetServiceInstanceResponse
                    .builder()
                    .serviceDefinitionId(serviceDefinitionId)
                    .planId(planId)
                    .dashboardUrl(getEc2Instance(authentication, serviceInstanceId, planId, serviceDefinitionId).getPublicIpAddress())
                    .build());
            default -> throw new IllegalStateException("Unexpected value: " + serviceInstanceId);
        };

    }

    @Override
    public Mono<DeleteServiceInstanceResponse> deleteServiceInstance(Authentication authentication, String serviceInstanceId, Plan plan, ServiceDefinition serviceDefinition) {
        return switch (serviceDefinition.getId()) {
            case "server-vm" -> Mono.just(DeleteServiceInstanceResponse.builder()
                    .async(false)
                    .operation(deleteEc2Instance(authentication, serviceInstanceId, plan, serviceDefinition)
                            .getCurrentState()
                            .getName())
                    .build());
            default -> throw new IllegalStateException("Unexpected value: " + serviceInstanceId);
        };
    }

    private InstanceStateChange deleteEc2Instance(Authentication authentication, String serviceInstanceId, Plan plan, ServiceDefinition serviceDefinition) {
        TerminateInstancesResult terminateInstancesResult = getAmazonClient(authentication).terminateInstances(new TerminateInstancesRequest(Collections.singletonList(serviceInstanceId)));

        return terminateInstancesResult.getTerminatingInstances().stream().filter(e -> e.getInstanceId().equals(serviceInstanceId)).findFirst().orElseThrow();
    }

    private Instance getEc2Instance(Authentication authentication, String serviceInstanceId, String planId, String serviceDefinitionId) {

        List<Instance> allInstances = getAllInstances(getAmazonClient(authentication));
        return allInstances
                .stream()
                .filter(e -> e.getInstanceId().equals(serviceInstanceId))
                .findFirst()
                .orElseThrow();
    }

    private AmazonEC2Async getAmazonClient(Authentication auth) {
        AppUser loggedUser = AuthUtils.getLoggedUser(auth);

        return AmazonEC2AsyncClient.asyncBuilder()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicSessionCredentials(
                        loggedUser.getAwsAccessKey(),
                        loggedUser.getAwsSecretKey(),
                        UUID.randomUUID().toString())))
                .build();
    }


    private List<Instance> getAllInstances(AmazonEC2Async ec2) {
        List<Instance> instances = new ArrayList<>();
        String nextToken = null;
        do {
            DescribeInstancesResult describeInstancesResult = ec2.describeInstances();
            for (Reservation reservation : describeInstancesResult.getReservations()) {
                instances.addAll(reservation.getInstances());
            }
            nextToken = describeInstancesResult.getNextToken();
        } while (nextToken != null);
        return instances;
    }
}
