//package com.vtb;
//
//import com.vtb.persistence.dao.ArtifactRepository;
//import com.vtb.persistence.dao.RoleRepository;
//import com.vtb.persistence.dao.UserRepository;
//import com.vtb.persistence.model.Artifact;
//import com.vtb.persistence.model.Fragment;
//import com.vtb.persistence.model.Role;
//import com.vtb.persistence.model.User;
//import java.util.Arrays;
//import java.util.HashSet;
//import javax.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class Setup {
//
//  @Autowired
//  private UserRepository userRepository;
//
//  @Autowired
//  private RoleRepository roleRepository;
//
//  @Autowired
//  private ArtifactRepository artifactRepository;
//
//  @PostConstruct
//  private void setupData() {
//    Role roleUser = new Role("ROLE_USER");
//    roleUser = roleRepository.save(roleUser);
//    Role roleAdmin = new Role("ROLE_ADMIN");
//    roleAdmin = roleRepository.save(roleAdmin);
//
//    final User userJohn = new User("john", "john@test.com");
//    userJohn.setRoles(new HashSet<Role>(Arrays.asList(roleUser, roleAdmin)));
//    userRepository.save(userJohn);
//
//    final User userTom = new User("tom", "tom@test.com");
//    userTom.setRoles(new HashSet<Role>(Arrays.asList(roleUser)));
//    userRepository.save(userTom);
//
//    var fragment1 = new Fragment("fragment #1");
//    var fragment2 = new Fragment("fragment #2");
//    final Artifact artifact = new Artifact("Main artifact", Arrays.asList(fragment1, fragment2));
//    artifactRepository.save(artifact);
//  }
//
//}