package com.yanchware.assesment.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserService {

//    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

//    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }

//    public void saveUser(User user) {
//        String encodedKey = passwordEncoder.encode(user.getPrivateKey());
//        String encodedAwsAccessKey = passwordEncoder.encode(user.getAwsAccessKey());
//        String encodedAwsSecretKey = passwordEncoder.encode(user.getAwsSecretKey());
//        user.setPrivateKey(encodedKey);
//        user.setAwsAccessKey(encodedAwsAccessKey);
//        user.setAwsSecretKey(encodedAwsSecretKey);
//        userRepository.save(user);
//    }
}
