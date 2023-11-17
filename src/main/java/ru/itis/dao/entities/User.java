package ru.itis.dao.entities;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Timestamp registeredAt;
    private String avatarPath;
    private String name;
    private String patronymic;
    private String lastname;
    private String about;
}
