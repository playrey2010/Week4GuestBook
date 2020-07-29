package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CommandLineRunnerBean implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PostRepository postRepository;

    public void run(String... args){
        // test User bart with no posts
        User user = new User("bart", "bart@domain.com", "bart", "Bart", "Simpson",
                true);
        Role userRole = new Role("bart", "ROLE_USER");
        userRepository.save(user);
        roleRepository.save(userRole);

        // test users with posts, not enabled for login
        User userWS = new User("willSmith", "willSmith@realemail.com", "IamWill", "Will", "Smith",
                false);
        Role userRoleforWS = new Role("willSmith", "ROLE_USER");
        userRepository.save(userWS);
        roleRepository.save(userRoleforWS);

        User userNW = new User("catlover", "ninaCatLover@somewhere.com", "iLoveCats",
                "Nina", "Williams",false);
        Role userRoleforNW = new Role("catLover", "ROLE_USER");
        userRepository.save(userNW);
        roleRepository.save(userRoleforNW);

        // Preloading posts for Rsvp List page
        Post willSmithPost = new Post();
        LocalDate date = LocalDate.now();
        willSmithPost.setDate(date);
        willSmithPost.setRsvp("Going");
        willSmithPost.setMessage("Rey's Birthday Party?? You know I can't miss this one. It will be a blast!! Can I bring my dog? See you then!");
        willSmithPost.setUser(userWS);

        Post ninaWilliamsPost = new Post();
        ninaWilliamsPost.setDate(date);
        ninaWilliamsPost.setRsvp("Maybe");
        ninaWilliamsPost.setMessage("I haven't decided whether I am going since you did not show up to mine... Depends on my mood that day. HBD, anyways. ");
        ninaWilliamsPost.setUser(userNW);

        postRepository.save(willSmithPost);
        postRepository.save(ninaWilliamsPost);

        User admin = new User("super", "super@domain.com", "super",
                "Super", "Hero", true);
        Role adminRole1 = new Role("super", "ROLE_ADMIN");
        Role adminRole2 = new Role("super", "ROLE_USER");
        userRepository.save(admin);
        roleRepository.save(adminRole1);
        roleRepository.save(adminRole2);
    }

}
