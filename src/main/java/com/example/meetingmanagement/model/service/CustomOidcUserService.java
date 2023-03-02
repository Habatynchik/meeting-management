package com.example.meetingmanagement.model.service;

import com.example.meetingmanagement.model.entity.Role;
import com.example.meetingmanagement.model.entity.User;
import com.example.meetingmanagement.model.repository.RoleRepository;
import com.example.meetingmanagement.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private static final String GOOGLE_KEY_EMAIL = "email";
    private static final String GOOGLE_KEY_LASTNAME = "family_name";
    private static final String GOOGLE_KEY_FIRSTNAME = "given_name";

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        final OidcUser oidcUser = super.loadUser(userRequest);
        CustomOidcUser newUser = new CustomOidcUser(oidcUser);

        String email = oidcUser.getAttributes().get(GOOGLE_KEY_EMAIL).toString();
        User user = createUserIfNoExist(email, oidcUser);
        newUser.setRole(user.getRole().getRoleEnum().name());

        return newUser;
    }

    @Transactional
    public User createUserIfNoExist(String email, OidcUser oidcUser) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User user = new User()
                            .setUsername(email)
                            .setEmail(email)
                            .setRole(roleRepository.findByRoleEnum(Role.RoleEnum.CLIENT));

                    return userRepository.save(user);
                });
    }
}
