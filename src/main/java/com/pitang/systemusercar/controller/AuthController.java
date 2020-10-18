package com.pitang.systemusercar.controller;

import com.pitang.systemusercar.exception.CustomException;
import com.pitang.systemusercar.model.dto.AuthDTO;
import com.pitang.systemusercar.service.AuthService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador responsavel de autenticar usuario e retornar token
 *
 */

@RestController
@RequestMapping("/api")
@Api(tags = "api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public Map<String,String> login(@RequestBody AuthDTO authDTO) throws CustomException {
        System.out.println(authDTO.getLogin());
        Map<String,String> mapResponse = new HashMap<>();
        mapResponse.put("token", authService.login(authDTO.getLogin(), authDTO.getPassword()));
        return mapResponse;
    }
}

