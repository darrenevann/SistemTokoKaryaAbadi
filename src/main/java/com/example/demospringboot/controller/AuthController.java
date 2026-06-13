package com.example.demospringboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "expired", required = false) String expired,
            Model model) {

        if (error != null) {
            model.addAttribute("errorMessage", "Username atau password salah!");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Anda berhasil logout.");
        }
        if (expired != null) {
            model.addAttribute("errorMessage", "Sesi Anda telah berakhir. Silakan login kembali.");
        }

        return "auth/login";
    }
}
