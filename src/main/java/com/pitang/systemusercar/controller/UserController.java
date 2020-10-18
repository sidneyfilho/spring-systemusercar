package com.pitang.systemusercar.controller;

import com.pitang.systemusercar.exception.CustomException;
import com.pitang.systemusercar.model.dto.UserDTO;
import com.pitang.systemusercar.service.UserService;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador CRUD Users
 *
 */

@RestController
@RequestMapping("/api")
@Api(tags = "api")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> list() {

        return ResponseEntity.ok(new ModelMapper().map(userService.list(), new TypeToken<List<UserDTO>>() {}.getType()));
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> find(@PathVariable("id") String id) {

        return ResponseEntity.ok(userService.findById(id).getDto());
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> save(@RequestBody UserDTO userDTO) throws CustomException {

        return ResponseEntity.ok(userService.save(userDTO).getDto());
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserDTO> update(@PathVariable("id") String id, @RequestBody UserDTO userDTO) throws CustomException {

        return ResponseEntity.ok(userService.update(id, userDTO).getDto());
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") String id) {

        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> me(@RequestHeader("Authorization") String token) throws CustomException {

        return ResponseEntity.ok(userService.findUserByToken(token).getDto());
    }
}