package com.paintingscollectors.web;

import com.paintingscollectors.painting.model.Painting;
import com.paintingscollectors.painting.service.PaintingService;
import com.paintingscollectors.user.model.User;
import com.paintingscollectors.user.service.UserService;
import com.paintingscollectors.web.dto.LoginRequest;
import com.paintingscollectors.web.dto.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
public class IndexController {
    private final UserService userService;
    private final PaintingService paintingService;

    @Autowired
    public IndexController(UserService userService, PaintingService paintingService) {
        this.userService = userService;
        this.paintingService = paintingService;
    }

    @GetMapping("/")
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("registerRequest", new RegisterRequest());

        modelAndView.setViewName("register");

        return modelAndView;
    }

    @PostMapping("/register")
    public String registerUser(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.register(registerRequest);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("loginRequest", new LoginRequest());

        modelAndView.setViewName("login");

        return modelAndView;
    }

    @PostMapping("/login")
    public String loginUser(@Valid LoginRequest loginRequest, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        User user = userService.login(loginRequest);
        session.setAttribute("user_id", user.getId());

        return "redirect:/home";
    }

    @GetMapping("/home")
    public ModelAndView getHomePage(HttpSession session) {
        UUID userId = (UUID) session.getAttribute("user_id");

        User user = userService.getById(userId);
        List<Painting> allPaintings = paintingService.getAllPaintings();


        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("user", user);
        modelAndView.addObject("paintings", allPaintings);

        modelAndView.setViewName("home");

        return modelAndView;
    }

    @GetMapping("/logout")
    public String getLogoutPage(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
