package ru.itis.dao.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {
    private Long id;
    private String fileName;
    private String originalFileName;
    private String filePath;
    private String contentType;
    private int size;
}
