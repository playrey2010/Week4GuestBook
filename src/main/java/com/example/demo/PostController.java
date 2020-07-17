package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PostController {

    @Autowired
    PostRepository postRepository;

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("posts", postRepository.findAll());
        return "index";
    }

    @GetMapping("/newRSVP")
    public String newRsvpMessage(Model model){
        model.addAttribute("post", new Post());
        return "rsvpform";
    }

    @PostMapping("/processRSVP")
    public String processRsvp(@ModelAttribute Post post){
        postRepository.save(post);
        return "redirect:/";
    }
}
