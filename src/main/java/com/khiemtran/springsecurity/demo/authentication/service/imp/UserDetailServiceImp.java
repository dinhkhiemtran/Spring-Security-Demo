package com.khiemtran.springsecurity.demo.authentication.service.imp;

import com.khiemtran.springsecurity.demo.authentication.UserPrincipal;
import com.khiemtran.springsecurity.demo.model.User;
import com.khiemtran.springsecurity.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImp implements UserDetailsService {
  private final UserRepository userRepository;

  public UserDetailServiceImp(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String usernameOrEmail)
      throws UsernameNotFoundException {
    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
        .orElseThrow(() ->
            new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail)
        );
    return UserPrincipal.create(user);
  }

  @Transactional
  public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
    User user = userRepository.findById(id)
        .orElseThrow(() ->
            new UsernameNotFoundException("User not found with email: " + id));
    return UserPrincipal.create(user);
  }
}
