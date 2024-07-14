package com.yanchware.assesment.security;

import com.yanchware.assesment.user.AppUser;
import org.springframework.security.core.Authentication;

public class AuthUtils {

    public static AppUser getLoggedUser(Authentication authentication){
        if (authentication.getCredentials() instanceof AppUser){
            return (AppUser) authentication.getCredentials();
        }
        throw new RuntimeException();
    }

}
