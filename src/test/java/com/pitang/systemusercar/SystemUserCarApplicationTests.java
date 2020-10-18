package com.pitang.systemusercar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pitang.systemusercar.exception.CustomException;
import com.pitang.systemusercar.model.Car;
import com.pitang.systemusercar.model.User;
import com.pitang.systemusercar.model.dto.AuthDTO;
import com.pitang.systemusercar.model.dto.CarDTO;
import com.pitang.systemusercar.model.dto.UserDTO;
import com.pitang.systemusercar.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SystemUserCarApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	private JacksonTester<AuthDTO> jsonSignin;
	private JacksonTester<UserDTO> jsonUser;
	private JacksonTester<CarDTO> jsonCar;

	static final String HEADER_STRING = "Authorization";
	private static boolean initialized = false;

	@Before
	public void setup() throws CustomException {

		ObjectMapper objectMapper = new ObjectMapper();
		JacksonTester.initFields(this, objectMapper);
		if (!initialized) {
			userService.save(getUser().getDto());
			initialized = true;
		}
	}

	@Test
	public void signin() throws Exception {

		AuthDTO authDTO = new AuthDTO();
		authDTO.setLogin(getUser().getLogin());
		authDTO.setPassword(getUser().getPassword());
		mvc.perform(MockMvcRequestBuilders.post("/api/signin").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonSignin.write(authDTO).getJson()).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
	}

	@Test
	public void createUser() throws Exception {
		// Teste à parte
		User user = new User();
		user.setFirstName("Admin");
		user.setLastName("Test");
		user.setEmail("test@admin.com");
		user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"));
		user.setLogin("admin");
		user.setPassword("admin");
		user.setPhone("988888888");
		mvc.perform(MockMvcRequestBuilders.post("/api/users").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonUser.write(user.getDto()).getJson()).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
	}

	@Test
	public void listUsers() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/api/users")).andExpect(status().isOk());
	}

	@Test
	public void findUser() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/api/users/1")).andExpect(status().isOk());
	}

	@Test
	public void me() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/api/me").header(HEADER_STRING, token())).andExpect(status().isOk());
	}

	@Test
	public void updateUser() throws Exception {

		getUser().setLastName("Teste da Silva");
		System.out.println(jsonUser.write(getUser().getDto()).getJson());
		mvc.perform(MockMvcRequestBuilders.put("/api/users/1").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonUser.write(getUser().getDto()).getJson()).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
	}

	@Test
	public void deleteUser() throws Exception {

		mvc.perform(MockMvcRequestBuilders.delete("/api/users/1")).andExpect(status().isNoContent());
	}

	@Test
	public void createCar() throws Exception {

		mvc.perform(MockMvcRequestBuilders.post("/api/cars").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonCar.write(getCar().getDto()).getJson())
				.header(HEADER_STRING, token())).andExpect(status().isOk());
	}

	@Test
	public void findCar() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/api/cars/1").header(HEADER_STRING, token()))
				.andExpect(status().isOk());
	}

	@Test
	public void listCars() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/api/cars").header(HEADER_STRING, token()))
				.andExpect(status().isOk());
	}

	@Test
	public void updateCar() throws Exception {

		getCar().setColor("Red");
		mvc.perform(MockMvcRequestBuilders.put("/api/cars/1").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonCar.write(getCar().getDto()).getJson())
				.header(HEADER_STRING, token())).andExpect(status().isOk());
	}

	@Test
	public void deleteCar() throws Exception {

		mvc.perform(MockMvcRequestBuilders.delete("/api/cars/1").header(HEADER_STRING, token()))
				.andExpect(status().isNoContent());
	}

	private User getUser() throws CustomException {
		try {
		User user = new User();
		user.setFirstName("Hello");
		user.setLastName("World");
		user.setEmail("hello@world.com");
		user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse("1990-05-01"));
		user.setLogin("hello.world");
		user.setPassword("h3ll0");
		user.setPhone("988888888");
		user.getCars().add(getCar());
		return user;
		} catch (ParseException e){
			throw new CustomException("Invalid fields", 406);
		}
	}

	private Car getCar() {
		Car car = new Car();
		car.setLicensePlate("PDV-0625");
		car.setModel("Audi");
		car.setColor("White");
		return car;
	}

	private String token() throws CustomException {
		long EXPIRATION_TIME = 1500000;
		String KEY_SECRET = "DesafioPitang";
		userService.validAuth(getUser().getLogin(), getUser().getPassword());
		return Jwts.builder().setIssuedAt(new Date(System.currentTimeMillis())).setSubject(getUser().getLogin())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, KEY_SECRET).compact();
	}

}
