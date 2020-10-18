package com.pitang.systemusercar.service;

import com.pitang.systemusercar.exception.CustomException;
import com.pitang.systemusercar.model.Car;
import com.pitang.systemusercar.model.User;
import com.pitang.systemusercar.model.dto.CarDTO;
import com.pitang.systemusercar.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.Optional;

/**
 * Classe que é feita as operacoes de CRUD com JPA/Hibernate
 * Especifica do Model Car
 *
 */

@Service
public class CarService {

	@Autowired
	private AuthService authService;

	@Autowired
	private CarRepository carRepository;

	public Car findByIdAndIncrement(String id, int increment) {
		Car car = null;
		Optional<Car> opt = carRepository.findById(new BigInteger(id));
		if (opt.isPresent()) {
			car = opt.get();
			car.setUsedCount(car.getUsedCount() + increment);
			carRepository.save(car);
		}
		return car;
	}

	public Object list() {
		return carRepository.findAll();
	}

	public Car saveWithUser(CarDTO carDTO, User user) {

		Car car = new Car();
		car.setModel(carDTO.getModel());
		car.setYear(carDTO.getYear());
		car.setColor(carDTO.getColor());
		car.setLicensePlate(carDTO.getLicensePlate());
		car.setUser(user);
		return carRepository.save(car);
	}

	public Car saveWithToken(CarDTO carDTO, String token) throws CustomException {

		try {
			validFields(carDTO);
			return saveWithUser(carDTO, authService.getUser(token));
		} catch (CustomException e) {
			throw new CustomException(e);
		} catch (Exception e) {
			throw new CustomException("Missing fields", 406);
		}
	}

	public Car update(String id, CarDTO carDTO, String token) throws CustomException {

		try {
			Car car = findByIdAndIncrement(id, 0);
			car.setModel(carDTO.getModel());
			car.setYear(carDTO.getYear());
			car.setColor(carDTO.getColor());
			car = carRepository.save(car);
			return car;
		} catch (Exception e) {
			throw new CustomException("Missing fields", 406);
		}
	}

	public void delete(String id) {
		carRepository.deleteById(new BigInteger(id));
	}

	private void validFields(CarDTO carDTO) throws CustomException {

		if (carRepository.findByLicensePlate(carDTO.getLicensePlate()) != null) {
			throw new CustomException("License plate already exists", 406);
		}

		if (StringUtils.hasText(carDTO.getColor()) && !carDTO.getColor().matches("^[a-zA-Z]*$")) {
			throw new CustomException("“Invalid fields", 406);
		}
	}

}
