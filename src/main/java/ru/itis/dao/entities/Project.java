package ru.itis.dao.entities;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
   private Long id;
   private Long authorId;
   private String authorUsername;
   private String title;
   private String coverPath;
   private String content;
   private String address;
   private Double area;
   private Integer year;
   private Timestamp postedAt;
}
