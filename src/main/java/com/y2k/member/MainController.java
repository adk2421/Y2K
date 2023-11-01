package com.y2k.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/")
@RequiredArgsConstructor


public class MainController {
 

    @RequestMapping(value = "join")
    public String joinMember() throws Exception {

        return "join";
    }


    @RequestMapping(value = "login")
    public String loginUser() throws Exception {

        return "login";
    }
}
