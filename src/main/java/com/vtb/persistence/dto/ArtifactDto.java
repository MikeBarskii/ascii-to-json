package com.vtb.persistence.dto;

import com.vtb.persistence.model.Fragment;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtifactDto {
  private Long id;
  private String title;
  private String type;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private AuthorDto author;
  private List<FragmentDto> fragments;
}
