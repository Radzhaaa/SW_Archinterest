package ru.itis.dao.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.itis.dao.entities.abs.Comment;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProjectComment extends Comment {
    private Long projectId;
}
