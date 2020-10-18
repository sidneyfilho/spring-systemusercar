package com.pitang.systemusercar.repository;

import com.pitang.systemusercar.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigInteger;

@Repository
public interface CarRepository extends JpaRepository<Car, BigInteger> {

	Car findByLicensePlate(String licensePlate);
}
