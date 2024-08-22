package com.sol.snappick.global;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {

    @Hidden
    @GetMapping
    public String index() {
        return "redirect:/swagger-ui/index.html";
    }

    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthcheck() {
        return ResponseEntity.ok("ok");
    }
}
