package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class UserController {
    @Autowired
    private AdminController adminController;

    @GetMapping("/user/{resource}")
    public ResponseEntity<String> accessResource(@PathVariable String resource, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        if (!"user".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.Only users can access this endpoint.");
        }

        Set<String> resources = adminController.userAccessMap.get(userId);

        if (resources != null && resources.contains(resource)) {
            return ResponseEntity.ok("You have access to resource: " + resource);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are denied to access resource: " + resource);
        }
    }
}
