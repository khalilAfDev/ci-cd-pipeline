package com.jenkins.cicdpipeline;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class LoginController {

    @GetMapping("/login")
    public String index(){
        return "index";
    }
	
}
