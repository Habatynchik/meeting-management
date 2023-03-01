package com.example.meetingmanagement.controller.guest;

import com.example.meetingmanagement.model.dto.UserDto;
import com.example.meetingmanagement.model.exception.EmailIsReservedException;
import com.example.meetingmanagement.model.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/")
public class RegistrationController {
    private final UserService userService;

    @GetMapping("/register")
    public String getRegistrationPage(Model model) {
        model.addAttribute("userDto", new UserDto());

        return "register";
    }

    @PostMapping("/register")
    public String createNewAccount(@ModelAttribute(name = "userDto") @Valid UserDto userDto,
                                   BindingResult validationResult,
                                   Model model) {

        log.info("test");
        if (!validationResult.hasErrors()) {
            try {
                log.info("User wants to create new account");
                userService.registerNewAccount(userDto);

                return "redirect:/login";
            } catch (EmailIsReservedException e) {
                model.addAttribute("emailIsReserved", true);
            }
        }

        log.info("User print some incorrect data during registration");
        return "/register";
    }

}
