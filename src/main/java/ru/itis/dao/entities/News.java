package ru.itis.dao.entities;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class News {
    private Long id;
    private Long authorId;
    private String authorUsername;
    private String authorName;
    private String authorLastname;
    private String title;
    private String annotation;
    private String content;
    private String coverPath;
    private Timestamp createdAt;
}
