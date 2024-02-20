package life.macchiato.customer.controller;

import life.macchiato.customer.dto.CustomerRegistrationRequest;
import life.macchiato.customer.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    @Autowired
    private final CustomerService customerService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.status(200).body("hello");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        log.info("New customer registration {}", request);
        customerService.registerCustomer(request);
        return ResponseEntity.status(200).body("Registered: " + request);
    }

}
