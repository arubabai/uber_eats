package com.uber.eats.serviceimpl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uber.eats.entity.AddressEntity;
import com.uber.eats.entity.CityEntity;
import com.uber.eats.entity.PasswordResetTokenEntity;
import com.uber.eats.entity.UserEntity;
import com.uber.eats.enums.RoleConstants;
import com.uber.eats.enums.StatusType;
import com.uber.eats.exception.DataInvalidException;
import com.uber.eats.repository.AddressRepository;
import com.uber.eats.repository.CityRepository;
import com.uber.eats.repository.CountryRepository;
import com.uber.eats.repository.PasswordResetTokenRepository;
import com.uber.eats.repository.RoleRepository;
import com.uber.eats.repository.StateRepository;
import com.uber.eats.repository.UserRepository;
import com.uber.eats.request.PasswordResetRequestDTO;
import com.uber.eats.request.UserCreateRequestDTO;
import com.uber.eats.response.BasicIdAndNameResponseDTO;
import com.uber.eats.service.CustomMailService;
import com.uber.eats.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	PasswordEncoder userPasswordEncoder;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	StateRepository stateRepository;

	@Autowired
	CityRepository cityRepository;

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	CustomMailService mailService;

	@Autowired
	MessageSource messageSource;

	@Override
	public List<BasicIdAndNameResponseDTO> findAllCountries() {

		return countryRepository.findAllByStatus(StatusType.ACTIVE);

	}

	@Override
	public List<BasicIdAndNameResponseDTO> findAllStatesByCountryId(Long countryId) {

		return stateRepository.findAllByCountryIdAndStatus(countryId, StatusType.ACTIVE);

	}

	@Override
	public List<BasicIdAndNameResponseDTO> findAllCitiesByStateId(Long stateId) {

		return cityRepository.findAllByStateIdAndStatus(stateId, StatusType.ACTIVE);

	}

	@Override
	public void signUp(UserCreateRequestDTO request) throws DataInvalidException {

		if (!EmailValidator.getInstance().isValid(request.getEmail())) {

			throw new DataInvalidException(messageSource.getMessage("invalid.email", null, null));

		}

		if (userRepository.existsByEmailAndStatus(request.getEmail(), StatusType.ACTIVE)) {

			throw new DataInvalidException(messageSource.getMessage("email.already.exists", null, null));

		}

		UserEntity user = new UserEntity();

		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setMobilePhone(request.getPhone());
		user.setStatus(StatusType.ACTIVE);

		AddressEntity address = new AddressEntity();

		CityEntity city = cityRepository.findByIdAndStatus(request.getAddress().getCityId(), StatusType.ACTIVE)
				.orElseThrow(DataInvalidException::new);

		address.setAddress(request.getAddress().getAddress());
		address.setCity(city);
		address.setState(city.getState());
		address.setCountry(city.getState().getCountry());

		addressRepository.save(address);

		user.getAddressList().add(address);
		user.setUserName(request.getEmail());
		user.setPassword(userPasswordEncoder.encode(request.getPassword()));
		user.setRole(roleRepository.findByShortCode(RoleConstants.ROLE_CLIENT_ADMIN).orElseThrow(
				() -> new DataInvalidException(messageSource.getMessage("client.role.not.found", null, null))));

		userRepository.save(user);

	}

	@Override
	public void generatePasswordResetMail(HttpServletRequest httpServletRequest, String email)
			throws DataInvalidException {

		UserEntity user = userRepository.findByUserName(email)
				.orElseThrow(() -> new DataInvalidException(messageSource.getMessage("invalid.email", null, null)));

		String token = UUID.randomUUID().toString();

		PasswordResetTokenEntity passwordResetToken = passwordResetTokenRepository.findByUser(user)
				.orElse(new PasswordResetTokenEntity());

		passwordResetToken.setToken(token);
		passwordResetToken.setUser(user);
		passwordResetToken.setExpiryDate(DateUtils.addHours(new Date(), 1));

		passwordResetTokenRepository.save(passwordResetToken);

		mailService.sendMail(email, messageSource.getMessage("mail.subject", null, null),
				messageSource.getMessage("mail.body", null, null) + "\n\n" + "http://"
						+ httpServletRequest.getServerName() + "/user/reset-password?token=" + token);

	}

	@Override
	public void resetPassword(PasswordResetRequestDTO request) throws DataInvalidException {

		PasswordResetTokenEntity passwordResetToken = passwordResetTokenRepository.findByToken(request.getToken())
				.orElseThrow(() -> new DataInvalidException(messageSource.getMessage("invalid.token", null, null)));

		if (passwordResetToken.getExpiryDate().compareTo(new Date()) < 0) {

			throw new DataInvalidException(messageSource.getMessage("token.expired", null, null));

		}

		passwordResetToken.getUser().setPassword(userPasswordEncoder.encode(request.getPassword()));
		passwordResetToken.getUser().setModifiedDate(new Date());

		userRepository.save(passwordResetToken.getUser());

		passwordResetTokenRepository.delete(passwordResetToken);

	}

}
