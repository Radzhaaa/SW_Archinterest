package ru.itis.dao.entities.abs;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class Comment {
    private Long id;
    private Long authorId;
    private String authorUsername;
    private String avatarPath;
    private String content;
    private Integer likes;
    private Integer dislikes;
    private Timestamp createdAt;
}
