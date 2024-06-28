package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private static final String ACCESS_FILE = "access.txt";
    Map<String, Set<String>> userAccessMap = new HashMap<>();

    public AdminController() {
        loadAccessFromFile();
    }

    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(@RequestBody Map<String, List<String>> userAccess, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.Only admins can add user access.");
        }
        String userId = String.valueOf(userAccess.get("userId").get(0));//.toString();
        List<String> resources = userAccess.get("endpoint");
        userAccessMap.put(userId, new HashSet<>(resources));
        saveAccessToFile();
        return ResponseEntity.ok("User access added successfully.");
    }

    private void loadAccessFromFile() {
        try(BufferedReader reader = new BufferedReader(new FileReader(ACCESS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                userAccessMap.put(parts[0], new HashSet<>(Arrays.asList(parts[1].split(":"))));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveAccessToFile() {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(ACCESS_FILE))) {
            for (Map.Entry<String, Set<String>> entry : userAccessMap.entrySet()) {
                writer.write(entry.getKey() + "," + String.join(":", entry.getValue()));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
