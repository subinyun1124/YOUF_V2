package com.uf.assistance.config.jwt;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    @PostMapping("/test")
    public String test(@RequestBody Map<String, Object> body) {
        System.out.println(body);
        return "ok";
    }
}
