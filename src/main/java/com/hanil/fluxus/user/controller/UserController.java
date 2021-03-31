package com.hanil.fluxus.user.controller;

import com.hanil.fluxus.user.model.User;
import com.hanil.fluxus.user.model.UserSignUp;
import com.hanil.fluxus.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "/home/index";
    }

    @GetMapping (value="/test")
    public String test(Model model){
        model.addAttribute("name","hanil");
        model.addAttribute("email","hanil@hanil.com");

        return "test";
    }

    @GetMapping (value="/login")
    public String login(){

        return "user/login";
    }

    @GetMapping("/user/signup")
    public String signupForm(Model model) {
        model.addAttribute("user",new UserSignUp());

        return "/user/signupForm";
    }

    @PostMapping("/user/signup")
    public String signup(UserSignUp usu) {

        userService.signUp(usu);

        return "redirect:/";
    }



    @GetMapping("/user/login")
    public String loginForm() {
        return "/user/loginForm";
    }


}
