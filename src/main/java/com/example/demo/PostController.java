package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@Controller
public class PostController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @RequestMapping("/")
    public String index(Model model, Principal principal, @ModelAttribute("post") Post post){

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
    public String testingPage(Model model, Principal principal){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getPrincipal().toString().equals("anonymousUser")){
            String username = principal.getName();
            User user = userRepository.findByUsername(username);
            if (!user.getPosts().isEmpty()){
                return "redirect:/";
            }
        }
        return "test";
    }

    @GetMapping("/accept")
    public String acceptRsvp(Model model, Principal principal){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            String username = principal.getName();
            User user = userRepository.findByUsername(username);
            if (!user.getPosts().isEmpty()){
                return "redirect:/";
            }
        }
        LocalDate date = LocalDate.now();
        Post post = new Post();
        post.setRsvp("Going");
        post.setDate(date);
        model.addAttribute("post", post);
        return "postform";
    }

    @GetMapping("/maybe")
    public String maybeRsvp(Model model, Principal principal){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = principal.getName();
            User user = userRepository.findByUsername(username);
            if (!user.getPosts().isEmpty()){
                return "redirect:/";
            }
        }
        LocalDate date = LocalDate.now();
        Post post = new Post();
        post.setRsvp("Maybe");
        post.setDate(date);
        model.addAttribute("post", post);
        return "postform";
    }

    @GetMapping("/sorry")
    public String sorryRsvp(Model model, Principal principal){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getPrincipal().toString().equals("anonymousUser")){
            String username = principal.getName();
            User user = userRepository.findByUsername(username);
            if (!user.getPosts().isEmpty()){
                return "redirect:/";
            }
        }
        LocalDate date = LocalDate.now();
        Post post = new Post();
        post.setRsvp("Sorry");
        post.setDate(date);
        model.addAttribute("post", post);
        return "postform";
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
        return "redirect:/thankYou";
    }

    @RequestMapping("/thankYou")
    public String thankYou(Principal principal, Model model){
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        if (user.getPosts().isEmpty()){
            return "redirect:/test";
        }
        model.addAttribute("user", user);
        model.addAttribute("post", postRepository.findByUser(user));
        return "thankyou";
    }

    @RequestMapping("/updateRSVP/{id}")
    public String updateRsvp(@PathVariable("id") long id, Model model){
        model.addAttribute("post", postRepository.findById(id).get());
        return "postform";
    }

    @RequestMapping("/deleteRSVP/{id}")
    public String deleteRsvp(@PathVariable("id")long id){
        postRepository.deleteById(id);
        return "redirect:/";
    }

}
