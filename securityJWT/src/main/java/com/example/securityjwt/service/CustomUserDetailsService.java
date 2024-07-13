package com.example.securityjwt.service;

import com.example.securityjwt.dto.CustomUserDetails;
import com.example.securityjwt.entity.UserEntity;
import com.example.securityjwt.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // DB에서 조회
        UserEntity userEntity = userRepository.findByUsername(username);

        // UserDetails에 담아서 return하면 AuthenticationManager가 검증한다
        if (userEntity != null) {
            return new CustomUserDetails(userEntity);
        }

        return null;
    }
}
