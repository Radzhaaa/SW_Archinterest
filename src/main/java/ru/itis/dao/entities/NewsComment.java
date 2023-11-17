package ru.itis.dao.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.itis.dao.entities.abs.Comment;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class NewsComment extends Comment {
    private Long newsId;
}
