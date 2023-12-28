package com.khiemtran.springsecurity.demo.model;

import com.khiemtran.springsecurity.demo.model.audit.DateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = {
        "username"
    }),
    @UniqueConstraint(columnNames = {
        "email"
    })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends DateAudit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotBlank
  private String name;
  @NotBlank
  private String username;
  @NotBlank
  @Email
  private String email;
  @NotBlank
  private String password;
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();
}
