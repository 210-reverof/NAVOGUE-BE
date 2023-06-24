package teo.sprint.navogue.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import teo.sprint.navogue.domain.user.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {


    private final UserService userService;

    @GetMapping ("/login")
    public String login(@RequestParam("code") String code) throws Exception{
        String accessToken = userService.login(code);

        return accessToken;
    }
}