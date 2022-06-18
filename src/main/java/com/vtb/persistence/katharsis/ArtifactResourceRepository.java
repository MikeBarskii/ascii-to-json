package com.vtb.persistence.katharsis;

import com.vtb.persistence.dao.ArtifactRepository;
import com.vtb.persistence.model.Artifact;
import io.katharsis.queryspec.QuerySpec;
import io.katharsis.repository.ResourceRepositoryV2;
import io.katharsis.resource.list.ResourceList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArtifactResourceRepository implements ResourceRepositoryV2<Artifact, Long> {

  @Autowired
  private ArtifactRepository artifactRepository;

  @Override
  public Artifact findOne(Long id, QuerySpec querySpec) {
    Optional<Artifact> artifact = artifactRepository.findById(id);
    return artifact.orElse(null);
  }

  @Override
  public ResourceList<Artifact> findAll(QuerySpec querySpec) {
    return querySpec.apply(artifactRepository.findAll());
  }

  @Override
  public ResourceList<Artifact> findAll(Iterable<Long> ids, QuerySpec querySpec) {
    return querySpec.apply(artifactRepository.findAllById(ids));
  }

  @Override
  public <S extends Artifact> S save(S entity) {
    return artifactRepository.save(entity);
  }

  @Override
  public void delete(Long id) {
    artifactRepository.deleteById(id);
  }

  @Override
  public Class<Artifact> getResourceClass() {
    return Artifact.class;
  }

  @Override
  public <S extends Artifact> S create(S entity) {
    return save(entity);
  }

}
