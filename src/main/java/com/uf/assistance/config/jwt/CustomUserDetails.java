package com.uf.assistance.config.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private Long id;
    private String userId;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long id, String userId, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.userId = userId;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}