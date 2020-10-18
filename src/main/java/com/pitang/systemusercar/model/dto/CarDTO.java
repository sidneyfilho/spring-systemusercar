package com.pitang.systemusercar.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CarDTO {

	private String id;
	private int year;
	private String licensePlate;
	private String model;
	private String color;
	private int usedCount;

}
