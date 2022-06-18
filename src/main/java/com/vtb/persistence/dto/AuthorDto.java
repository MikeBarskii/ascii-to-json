package com.vtb.persistence.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {

  private UUID guid;
  private String displayName;
  private String avatar;
  private String email;
  private String position;
}
