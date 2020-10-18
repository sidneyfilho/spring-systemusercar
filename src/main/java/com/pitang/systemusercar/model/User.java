package com.pitang.systemusercar.model;

import com.pitang.systemusercar.model.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name = "FIRST_NAME", nullable = false, length = 20)
	private String firstName;

	@Column(name = "LAST_NAME", nullable = false, length = 60)
	private String lastName;

	@Column(name = "EMAIL", nullable = false, unique = true, length = 100)
	private String email;

	@Column(name = "BIRTHDAY", nullable = false)
	private Date birthday;

	@Column(name = "login", nullable = false, unique = true, length = 30)
	private String login;

	@Column(name = "PASSWORD", nullable = false, length = 100)
	private String password;

	@Column(name = "PHONE", nullable = false, length = 15)
	private String phone;

	@OneToMany
	@JoinColumn(name = "USER_ID")
	private List<Car> cars = new ArrayList<>();

	@Column(name = "CREATED_AT", nullable = false)
	private Date createdAt;

	@Column(name = "LAST_LOGIN")
	private Date lastLogin;

	//Total de carros usados
	private long totalUsedCountCars;

	//Override do Security do Spring
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.login;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public UserDTO getDto() {
		UserDTO userDTO = new ModelMapper().map(this, UserDTO.class);
		userDTO.setConvertedBirthday(this.getBirthday());
		return userDTO;
//		return new ModelMapper().map(this, UserDTO.class);
	}
}
