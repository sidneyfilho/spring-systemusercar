package com.pitang.systemusercar.service;

import com.pitang.systemusercar.exception.CustomException;
import com.pitang.systemusercar.model.Car;
import com.pitang.systemusercar.model.User;
import com.pitang.systemusercar.model.dto.CarDTO;
import com.pitang.systemusercar.model.dto.UserDTO;
import com.pitang.systemusercar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Classe que é feita as operacoes de CRUD com JPA/Hibernate
 * Especifica do Model User
 *
 */

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CarService carService;

	public User findById(String id) {

		return userRepository.findById(new BigInteger(id)).get();
	}

	public User findByLogin(String login) {

		return userRepository.findByLogin(login);
	}

	public List<User> list() {
		List<User> users = userRepository.findAll();
		sortList(users);
		return users;
	}

	public User save(UserDTO userDTO) throws CustomException {

		try {
			validFields(userDTO);
			User user = new User();
			user.setLogin(userDTO.getLogin());
			user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
			user.setFirstName(userDTO.getFirstName());
			user.setLastName(userDTO.getLastName());
			user.setBirthday(userDTO.getBirthdayConverted());
			user.setPhone(userDTO.getPhone());
			user.setEmail(userDTO.getEmail());
			user.setCreatedAt(new Date());
			user = userRepository.save(user);

			List<Car> cars = new ArrayList<>();
			for (CarDTO carDTO : userDTO.getCars()) {
				cars.add(carService.saveWithUser(carDTO, user));
			}
			user.setCars(cars);
			return user;
		} catch (CustomException e) {
			throw new CustomException(e);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new CustomException("Missing fields", 406);
		}
	}

	public User update(String id, UserDTO userDTO) throws CustomException {

		try {
			User user = findById(id);
			user.setFirstName(userDTO.getFirstName());
			user.setLastName(userDTO.getLastName());
			user.setBirthday(userDTO.getBirthdayConverted());
			user.setPhone(userDTO.getPhone());
			user = userRepository.save(user);
			return user;
		} catch (Exception e) {
			throw new CustomException("Missing fields", 406);
		}
	}

	public void delete(String id) {
		userRepository.deleteById(new BigInteger(id));
	}

	private void validFields(UserDTO userDTO) throws CustomException {
		String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		Pattern pattern = Pattern.compile(EMAIL_REGEX);
		if (!pattern.matcher(userDTO.getEmail()).matches()) {
			throw new CustomException("Invalid fields", 406);
		}

		if (userRepository.findByEmail(userDTO.getEmail()) != null) {
			throw new CustomException("Email already exists", 406);
		}

		if (userRepository.findByLogin(userDTO.getLogin()) != null) {
			throw new CustomException("Login already exists", 406);
		}
	}

	public void validAuth(String login, String password) throws CustomException {

		User user = findByLogin(login);
		if (user == null || !AuthService.matchedPassword(password, user.getPassword())) {
			throw new CustomException("Invalid login or password", 400);
		}
		user.setLastLogin(new Date(System.currentTimeMillis()));
		userRepository.save(user);
	}

	public User findUserByToken(String token) throws CustomException {

		return authService.getUser(token);
	}

	/**
	 * Requisito extra
	 *
	 */
	private void sortList(List<User> users) {
		for (User user : users) {
			// Ordenando lista de acordo com o total de utilizações (usedCount)
			// de acordo com o somatório total de utilizações de todos os seus carros
			user.getCars().sort(Comparator.comparing(Car::getUsedCount)

			// Caso os carros possuam a mesma quantidade de utilizações,
			// o modelo deverá ser utilizado como critério de desempate (ordem alfabética)
			.reversed().thenComparing(Car::getModel));

			// Atribuindo valor total de todos os carros usados no contador do usuario
			user.setTotalUsedCountCars(user.getCars().stream().mapToLong(Car::getUsedCount).sum());
		}

		// Caso os usuários possuam a mesma quantidade total de utilizações de seus carros,
		// o login deverá ser utilizado como critério de desempate (ordem alfabética)
		users.sort(Comparator.comparing(User::getTotalUsedCountCars).reversed().thenComparing(User::getLogin));
	}

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		User user = findByLogin(login);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		return user;
	}
}
