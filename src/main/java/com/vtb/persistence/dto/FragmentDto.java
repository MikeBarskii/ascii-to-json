package com.vtb.persistence.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FragmentDto {
  private Long id;
  private String title;
  private String order;
  private String type;
  private String source;
  private ContentDto content;
  private List<FragmentDto> children;
}
