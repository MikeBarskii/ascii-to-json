package com.vtb.persistence.model;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonApiResource(type = "fragments")
public class Fragment {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonApiId
  private Long id;

  private String content;

  public Fragment() {
  }

  public Fragment(String content) {
    this.content = content;
  }
}
