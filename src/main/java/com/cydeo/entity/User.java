package com.cydeo.entity;

import com.cydeo.enums.Gender;
import lombok.*;

import javax.persistence.*;


@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name="users")
public class User extends BaseEntity{

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private boolean enabled;
    private String phone;

    @ManyToOne
    private Role role;
    @Enumerated(EnumType.STRING)
    private Gender gender;


    }

