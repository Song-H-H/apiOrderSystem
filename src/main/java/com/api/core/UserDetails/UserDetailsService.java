package com.api.core.UserDetails;


import com.api.core.jwt.JwtAuthUser;
import com.api.token.dto.TokenUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JwtAuthUser jwtAuthUser = JwtAuthUser.valueOf(username);

        return new TokenUser(username, jwtAuthUser.getPassword(), jwtAuthUser.getRoles());
    }

}
