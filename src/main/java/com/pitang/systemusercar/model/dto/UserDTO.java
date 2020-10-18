package com.pitang.systemusercar.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter @Setter
public class UserDTO {

	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String birthday;
	private String login;
	private String password;
	private String phone;
	private List<CarDTO> cars = new ArrayList<>();
	private String createdAt;
	private String lastLogin;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public Date getBirthdayConverted() throws ParseException {
		return dateFormat.parse(this.birthday);
	}

	public void setConvertedBirthday(Date date) {
		this.birthday = dateFormat.format(date);
	}

}
