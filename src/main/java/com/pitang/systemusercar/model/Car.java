package com.pitang.systemusercar.model;

import com.pitang.systemusercar.model.dto.CarDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Getter @Setter
public class Car {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name = "YEAR", nullable = false)
	private int year;

	@Column(name = "LICENSE_PLATE", nullable = false, unique = true, length = 10)
	private String licensePlate;

	@Column(name = "MODEL", nullable = false, length = 30)
	private String model;

	@Column(name = "COLOR", nullable = false, length = 30)
	private String color;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@Column(name = "USED_COUNT", nullable = false, columnDefinition = "integer default 0")
	private int usedCount;

	public CarDTO getDto() {
		return new ModelMapper().map(this, CarDTO.class);
	}
}
