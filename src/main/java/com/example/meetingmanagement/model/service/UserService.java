package com.example.meetingmanagement.model.service;

import com.example.meetingmanagement.model.dto.UserDto;
import com.example.meetingmanagement.model.entity.Provider;
import com.example.meetingmanagement.model.entity.Role;
import com.example.meetingmanagement.model.entity.User;
import com.example.meetingmanagement.model.exception.EmailIsReservedException;
import com.example.meetingmanagement.model.exception.EmailNotFoundException;
import com.example.meetingmanagement.model.repository.RoleRepository;
import com.example.meetingmanagement.model.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public User registerNewAccount(UserDto userDto) throws EmailIsReservedException {
        checkEmailIsUnique(userDto.getEmail());

        User user = User.builder()
                .username(userDto.getEmail())
                .email(userDto.getEmail())
                .password(new BCryptPasswordEncoder().encode(userDto.getPassword()))
                .role(roleRepository.findByRoleEnum(Role.RoleEnum.CLIENT))
                .blocked(false)
                .build();

        log.info("New account '{}' has been created", user);
        return userRepository.save(user);
    }

    @Transactional
    public Optional<User> processOAuthPostLogin(String email) {
        Optional<User> existUser = userRepository.findByEmail(email);

        if (existUser.isEmpty()) {

            User user = User.builder()
                    .username(email)
                    .email(email)
                    .role(roleRepository.findByRoleEnum(Role.RoleEnum.CLIENT))
                    .blocked(false)
                    .build();

            userRepository.save(user);

            return Optional.of(user);
        }
        return existUser;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.orElseThrow(() -> new EmailNotFoundException(email));
    }

    private void checkEmailIsUnique(String email) throws EmailIsReservedException {
        if (userRepository.findByEmail(email).isPresent()) {
            log.info("Email {} is reserved", email);
            throw new EmailIsReservedException();
        }

    }
}
