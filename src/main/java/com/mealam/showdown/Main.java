package com.mealam.showdown;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Main {

    public static void main(String[] pArgs) {
        SpringApplication.run(Main.class, pArgs);
    }

    @RestController
    static class TestKotlin {

        @GetMapping("/test-kt")
        public String test() {
            return Test.greet("Kotlin");
        }
    }
}
