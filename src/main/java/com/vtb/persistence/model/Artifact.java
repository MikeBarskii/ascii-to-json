package com.vtb.persistence.model;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@JsonApiResource(type = "artifacts")
@EqualsAndHashCode(of = "id")
public class Artifact {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonApiId
  private Long id;

  private String title;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Fragment> fragments;

  public Artifact() {
  }

  public Artifact(String title, List<Fragment> fragments) {
    this.title = title;
    this.fragments = fragments;
  }
}
