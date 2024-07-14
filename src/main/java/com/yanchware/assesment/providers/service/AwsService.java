package com.yanchware.assesment.providers.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
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


@Service
@Slf4j
public class AwsService implements CloudProviderService {


    public Mono<CreateServiceInstanceResponse> createVm(Authentication auth, Plan plan, ServiceDefinition serviceDefinition) {
        AppUser loggedUser = AuthUtils.getLoggedUser(auth);
        AmazonEC2 ec2 = AmazonEC2Client.builder()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicSessionCredentials(
                        loggedUser.getAwsAccessKey(),
                        loggedUser.getAwsSecretKey(),
                        UUID.randomUUID().toString())))
                .build();

        RunInstancesRequest runInstancesRequest = new RunInstancesRequest("ami-08734ec479a1ace4a", 1, 1);
        Instance createdInstance = ec2.runInstances(runInstancesRequest)
                .getReservation()
                .getInstances()
                .stream().findFirst()
                .orElseThrow();

        return Mono.just(CreateServiceInstanceResponse.builder().instanceExisted(true)
                .dashboardUrl(createdInstance.getPublicIpAddress()).build());
    }

    private List<Instance> getAllInstances(AmazonEC2 ec2) {
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

    @Override
    public Mono<CreateServiceInstanceResponse> createServiceInstance(Authentication authentication, String serviceInstanceId, Plan plan, ServiceDefinition serviceDefinition) {
        return null;
    }

    @Override
    public Mono<GetServiceInstanceResponse> getServiceInstance(Authentication authentication, String serviceInstanceId) {
        return null;
    }

    @Override
    public Mono<DeleteServiceInstanceResponse> deleteServiceInstance(Authentication authentication, String serviceInstanceId) {
        return null;
    }
}
