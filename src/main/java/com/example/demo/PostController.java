package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class PostController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @RequestMapping("/")
    public String index(Model model, Principal principal){

        // passing user information so user id match post-user id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.getPrincipal().toString().equals("anonymousUser")){
            String username = principal.getName();
            model.addAttribute("user", userRepository.findByUsername(username));
        }
        model.addAttribute("posts", postRepository.findAll());
        return "index";
    }

    @RequestMapping("/test")
    public String testingPage(Model model){
        return "test";
    }

    @GetMapping("/newRSVP")
    public String newRsvpMessage(Model model){
        model.addAttribute("post", new Post());
        return "rsvpform";
    }

    @PostMapping("/processRSVP")
    public String processRsvp(@ModelAttribute Post post, Principal principal){
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        post.setUser(user);
        postRepository.save(post);
        return "redirect:/";
    }

    @RequestMapping("/updateRSVP/{id}")
    public String updateRsvp(@PathVariable("id") long id, Model model){
        model.addAttribute("post", postRepository.findById(id).get());
        return "rsvpform";
    }

    @RequestMapping("/deleteRSVP/{id}")
    public String deleteRsvp(@PathVariable("id")long id){
        postRepository.deleteById(id);
        return "redirect:/";
    }

}
