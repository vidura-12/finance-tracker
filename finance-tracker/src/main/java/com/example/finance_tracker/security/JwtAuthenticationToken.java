package com.example.finance_tracker.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class JwtAuthenticationToken implements Authentication {
    private final UserDetails principal;
    private final String credentials;
    private boolean authenticated;
    private Object details;

    public JwtAuthenticationToken(UserDetails principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
        this.authenticated = true; // Mark as authenticated by default
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return principal != null ? principal.getAuthorities() : Collections.emptyList();
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return principal != null ? principal.getUsername() : null;
    }

    public void setDetails(Object details) {
        this.details = details;
    }
}