package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.User;
import org.example.servise.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public String saveUser(@ModelAttribute("user") User user  ) {
        userService.saveUser(user);
        return "redirect:/";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/sing-up")
    public String addUser(User user , Model model) {
        model.addAttribute("user", user);
        return "sing_up";
    }


}
