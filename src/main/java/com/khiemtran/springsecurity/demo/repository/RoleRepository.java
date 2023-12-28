package com.khiemtran.springsecurity.demo.repository;

import com.khiemtran.springsecurity.demo.model.Role;
import com.khiemtran.springsecurity.demo.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(RoleName roleName);
}
