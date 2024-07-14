package com.yanchware.assesment.providers.service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.servicebroker.model.instance.CreateServiceInstanceResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class AwsService implements CloudProviderService {


    @Override
    public Mono<CreateServiceInstanceResponse> createVm() {
        AmazonEC2 ec2 = AmazonEC2Client.builder()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicSessionCredentials("access_key_id", "secret_key_id", "session_token")))
                .build();

//        List<Instance> allInstances = getAllInstances(ec2);
//        Optional<Instance> matchedInstance = allInstances.stream().filter(e -> e.getInstanceId().equals()).findFirst();
//
//        if(matchedInstance.isPresent()){
//            return Mono.just(CreateServiceInstanceResponse.builder().instanceExisted(true)
//                    .dashboardUrl(matchedInstance.get().getPublicIpAddress()).build());
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

}
