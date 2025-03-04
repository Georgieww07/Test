package com.paintingscollectors.web;

import com.paintingscollectors.painting.model.Painting;
import com.paintingscollectors.painting.service.PaintingService;
import com.paintingscollectors.user.model.User;
import com.paintingscollectors.user.service.UserService;
import com.paintingscollectors.web.dto.CreatePaintingRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/paintings")
public class PaintingController {
    private final UserService userService;
    private final PaintingService paintingService;

    @Autowired
    public PaintingController(UserService userService, PaintingService paintingService) {
        this.userService = userService;
        this.paintingService = paintingService;
    }

    @GetMapping("/new")
    public ModelAndView getAddPaintingPage(HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getById(userId);

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("user", user);
        modelAndView.addObject("createPaintingRequest", new CreatePaintingRequest());

        modelAndView.setViewName("new-painting");

        return modelAndView;
    }

    @PostMapping
    public String createPainting(@Valid CreatePaintingRequest createPaintingRequest, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "new-painting";
        }

        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getById(userId);

        paintingService.createPainting(createPaintingRequest, user);

        return "redirect:/home";
    }

    @PostMapping("/{id}")
    public String makeFavouritePainting(@PathVariable UUID id, HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getById(userId);

        paintingService.createFavouritePaintingById(id, user);


        return "redirect:/home";
    }

    @PutMapping("/votes/{id}")
    public String increaseVotes(@PathVariable UUID id) {
        paintingService.increaseVotes(id);

        return "redirect:/home";
    }


    @DeleteMapping("/delete/{id}")
    public String deletePainting(@PathVariable UUID id) {
        paintingService.deleteById(id);

        return "redirect:/home";
    }

    @DeleteMapping("/delete/favourites/{id}")
    public String deleteFavouritePainting(@PathVariable UUID id) {
        paintingService.deleteFavouriteById(id);

        return "redirect:/home";
    }
}
