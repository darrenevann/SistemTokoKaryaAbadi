package com.example.demospringboot.controller;

import com.example.demospringboot.entity.User;
import com.example.demospringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("activePage", "users");
        return "users/index";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "User berhasil ditambahkan!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user,
                             RedirectAttributes redirectAttributes) {
        try {
            user.setId(id);
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "User berhasil diupdate!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "User berhasil dihapus!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }
}
