package com.yanchware.assesment.security;

import com.yanchware.assesment.user.AppUser;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BasicAuthManager implements AuthenticationManager {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BasicAuthManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();
        UserDetails user;

        try {
            user = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException ex) {
            throw new BadCredentialsException("User does not exists");
        }

        if (StringUtils.isBlank(password) || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Password is wrong");
        }

        AppUser appUser = (AppUser) user;
        return new UsernamePasswordAuthenticationToken(username, appUser, user.getAuthorities());
    }
}

