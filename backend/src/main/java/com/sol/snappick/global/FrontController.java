package com.sol.snappick.global;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
public class FrontController {

    @ApiIgnore
    @GetMapping
    public String index() {
        return "redirect:/swagger-ui/index.html";
    }

    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthcheck() {
        return ResponseEntity.ok("ok");
    }
}
