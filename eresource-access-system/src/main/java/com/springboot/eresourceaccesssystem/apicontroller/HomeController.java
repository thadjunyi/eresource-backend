package com.springboot.eresourceaccesssystem.apicontroller;

import com.springboot.eresourceaccesssystem.service.AudioService;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController()
@RequestMapping("")
public class HomeController {

    @Autowired
    public HomeController() {
    }

    @GetMapping("")
    public String connection() {
        return "You are connected.";
    }

}
