package com.DTechLtd.propertypulse_backend.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String usernameOrEmail);
}
