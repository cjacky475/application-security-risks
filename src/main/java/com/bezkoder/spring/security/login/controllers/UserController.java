package com.bezkoder.spring.security.login.controllers;

import com.bezkoder.spring.security.login.models.User;
import com.bezkoder.spring.security.login.repository.UserRepository;
import com.bezkoder.spring.security.login.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/")
public class UserController {

    UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/health")
    public HttpStatus health() {
        return HttpStatus.OK;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

        if (!Objects.equals(currentUser.getId(), userId)) {
            return new ResponseEntity<>("You are not authorized to access this resource!", HttpStatus.FORBIDDEN);
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Specified user could not be found!", HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/users/{id}/insecure")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
//    public ResponseEntity<?> getUserByIdInsecure(@PathVariable String id) {
//        List<User> users = entityManager.createQuery(
//                "SELECT u FROM User u WHERE u.id = " + id).getResultList();
//
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }

}
