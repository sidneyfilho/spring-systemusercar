package com.pitang.systemusercar.controller;

import com.pitang.systemusercar.exception.CustomException;
import com.pitang.systemusercar.model.dto.CarDTO;
import com.pitang.systemusercar.model.dto.UserDTO;
import com.pitang.systemusercar.service.CarService;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador CRUD Cars
 *
 */

@RestController
@RequestMapping("/api")
@Api(tags = "api")
public class CarController {

    @Autowired
    private CarService carService;

    @RequestMapping(value = "/cars", method = RequestMethod.GET)
    public ResponseEntity<List<CarDTO>> listCars() {

        return ResponseEntity.ok(new ModelMapper().map(carService.list(), new TypeToken<List<CarDTO>>() {}.getType()));
    }

    @RequestMapping(value = "/cars/{id}", method = RequestMethod.GET)
    public ResponseEntity<CarDTO> find(@PathVariable("id") String id) {

        return ResponseEntity.ok(carService.findByIdAndIncrement(id, 1).getDto());
    }

    @RequestMapping(value = "/cars", method = RequestMethod.POST)
    public ResponseEntity<CarDTO> save(@RequestBody CarDTO carDTO, @RequestHeader("Authorization") String token) throws CustomException {

        return ResponseEntity.ok(carService.saveWithToken(carDTO, token).getDto());
    }

    @RequestMapping(value = "/cars/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CarDTO> update(@PathVariable("id") String id,
                                         @RequestHeader("Authorization") String token,
                                         @RequestBody CarDTO carDTO) throws CustomException {

        return ResponseEntity.ok(carService.update(id, carDTO, token).getDto());
    }

    @RequestMapping(value = "/cars/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") String id) {

        carService.delete(id);
        return ResponseEntity.noContent().build();
    }

}