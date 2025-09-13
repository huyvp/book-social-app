package com.identity.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(name = "username", length = 50, nullable = false, unique = true)
    String username;
    @Column(name = "given_name", length = 200)
    String givenName;
    @Column(name = "family_name", length = 200)
    String familyName;
    @Column(name = "password", length = 200)
    String password;
    @Column(name = "email", length = 200, nullable = false)
    String email;
    @Column(name = "address", length = 200)
    String address;
    @Column(name = "phone_number", length = 10)
    String phoneNumber;
    @Column(name = "avatar", length = 300)
    String avatar;
    @Column(name = "active", nullable = false)
    boolean active;
    @ManyToMany
    Set<Role> roles;
}
