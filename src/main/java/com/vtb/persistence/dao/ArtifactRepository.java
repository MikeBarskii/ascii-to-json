package com.vtb.persistence.dao;

import com.vtb.persistence.model.Artifact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtifactRepository extends JpaRepository<Artifact, Long> {

}
