package com.registry.office.system.registry_office_system;

import com.registry.office.system.registry_office_system.entity.*;
import com.registry.office.system.registry_office_system.enums.Gender;
import com.registry.office.system.registry_office_system.enums.Operation;
import com.registry.office.system.registry_office_system.enums.Role;
import com.registry.office.system.registry_office_system.enums.Status;
import com.registry.office.system.registry_office_system.repository.birthRecord.BirthRecordRepository;
import com.registry.office.system.registry_office_system.repository.citizen.CitizenRepository;
import com.registry.office.system.registry_office_system.repository.deathRegistration.DeathRegistrationRepository;
import com.registry.office.system.registry_office_system.repository.divorceRegistration.DivorceRegistrationRepository;
import com.registry.office.system.registry_office_system.repository.employee.EmployeeRepository;
import com.registry.office.system.registry_office_system.repository.marriageRegistration.MarriageRegistrationRepository;
import com.registry.office.system.registry_office_system.repository.request.RequestRepository;
import com.registry.office.system.registry_office_system.service.user.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
@Rollback
class RegistryOfficeSystemApplicationTests {

	@Autowired
	UserService userService;

	@Autowired
	BirthRecordRepository birthRecordRepository;

	@Autowired
	CitizenRepository citizenRepository;

	@Autowired
	DeathRegistrationRepository deathRegistrationRepository;

	@Autowired
	MarriageRegistrationRepository marriageRegistrationRepository;

	@Autowired
	DivorceRegistrationRepository divorceRegistrationRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	RequestRepository requestRepository;

	User testUser;
	User secondTestUser;
	User thirdTestUser;
	BirthRecord birthRecord;
	DeathRegistration deathRegistration;
	MarriageRegistration marriageRegistration;
	DivorceRegistration divorceRegistration;
	Request request;

	void setUpTestUser(Role role) {
		testUser = new User();
		testUser.setGender(Gender.MALE);
		testUser.setUsername("magic-username");
		testUser.setPassword("magic-password");
		testUser.setName("Magic");
		testUser.setSurname("Magic");
		testUser.setPatronymic("Magic");
		testUser.setSnils("987-654-321 11");
		testUser.setRole(role);
		testUser.setDateOfBirth(LocalDate.parse("2000-01-01"));

		testUser = userService.registerUser(testUser);
	}

	void setUpSecondTestUser(Role role) {
		secondTestUser = new User();
		secondTestUser.setGender(Gender.FEMALE);
		secondTestUser.setUsername("magic-username-1");
		secondTestUser.setPassword("magic-password-1");
		secondTestUser.setName("MagicMagic");
		secondTestUser.setSurname("MagicMagic");
		secondTestUser.setPatronymic("MagicMagic");
		secondTestUser.setSnils("123-456-789 99");
		secondTestUser.setRole(role);
		secondTestUser.setDateOfBirth(LocalDate.parse("2001-02-02"));

		secondTestUser = userService.registerUser(secondTestUser);
	}

	void setUpThirdTestUser() {
		thirdTestUser = new User();
		thirdTestUser.setGender(Gender.MALE);
		thirdTestUser.setUsername("magic-username-2");
		thirdTestUser.setPassword("magic-password-2");
		thirdTestUser.setName("MagicMagicMagic");
		thirdTestUser.setSurname("MagicMagicMagic");
		thirdTestUser.setPatronymic("MagicMagicMagic");
		thirdTestUser.setSnils("123-123-123 12");
		thirdTestUser.setDateOfBirth(LocalDate.parse("2020-02-02"));

		thirdTestUser = userService.registerUser(thirdTestUser);
	}

	void setUpBirthRecord() {
		setUpTestUser(Role.CITIZEN);
		setUpSecondTestUser(Role.CITIZEN);
		setUpThirdTestUser();

		birthRecord = new BirthRecord();
		birthRecord.setFather(citizenRepository.findById(testUser.getPersonId()).get());
		birthRecord.setMother(citizenRepository.findById(secondTestUser.getPersonId()).get());
		birthRecord.setChild(citizenRepository.findById(thirdTestUser.getPersonId()).get());
		birthRecord.setRegionCode("IV");
		birthRecord.setRegistryCode("КЛ");

		birthRecord = birthRecordRepository.save(birthRecord);
	}

	void setUpDeathRegistration() {
		setUpTestUser(Role.CITIZEN);

		deathRegistration = new DeathRegistration();
		deathRegistration.setCitizen(citizenRepository.findById(testUser.getPersonId()).get());
		deathRegistration.setDeathDate(LocalDate.now().minusDays(1));
		deathRegistration.setCauseOfDeath("Причина");
		deathRegistration.setRegionCode("IV");
		deathRegistration.setRegistryCode("КЛ");

		deathRegistration = deathRegistrationRepository.save(deathRegistration);
	}

	void setUpMarriageRegistration() {
		setUpTestUser(Role.CITIZEN);
		setUpSecondTestUser(Role.CITIZEN);

		marriageRegistration = new MarriageRegistration();
		marriageRegistration.setHusband(citizenRepository.findById(testUser.getPersonId()).get());
		marriageRegistration.setWife(citizenRepository.findById(secondTestUser.getPersonId()).get());
		marriageRegistration.setRegistrationDate(LocalDate.now().minusDays(1));
		marriageRegistration.setRegionCode("IV");
		marriageRegistration.setRegistryCode("КЛ");

		marriageRegistration = marriageRegistrationRepository.save(marriageRegistration);
	}

	void setUpDivorceRegistration() {
		setUpMarriageRegistration();

		divorceRegistration = new DivorceRegistration();
		divorceRegistration.setMarriageRegistration(marriageRegistration);
		divorceRegistration.setDivorceDate(LocalDate.now());
		divorceRegistration.setReason("Причина");
		divorceRegistration.setRegionCode("IV");
		divorceRegistration.setRegistryCode("КЛ");

		divorceRegistrationRepository.save(divorceRegistration);
	}

	void setUpRequest() {
		setUpTestUser(Role.CITIZEN);
		setUpSecondTestUser(Role.EMPLOYEE);

		request = new Request();
		request.setApplicant(citizenRepository.findById(testUser.getPersonId()).get());
		request.setEmployee(employeeRepository.findById(secondTestUser.getPersonId()).get());
		request.setVisitDate(LocalDateTime.now());
		request.setOperation(Operation.BIRTH);
		request.setStatus(Status.WAIT);

		requestRepository.save(request);
	}

	@Test
	void whenAddUserThenIdNotZero() {
		setUpTestUser(Role.CITIZEN);
		assertNotEquals(0, testUser.getId());
	}

	@Test
	void whenAddUserWithNotNullFieldThenError() {
		assertThrows(Exception.class, () -> {
			userService.registerUser(testUser);
		});
	}

	@Test
	void whenAddUserThenPersonIdNotZero() {
		setUpTestUser(Role.CITIZEN);
		assertNotEquals(0, testUser.getPersonId());
	}

	@Test
	void whenAddBirthRecordThenIdNotZero() {
		setUpBirthRecord();
		assertNotEquals(0, birthRecord.getId());
	}

	@Test
	void whenAddBirthRecordWithNotNullFieldThenError() {
		assertThrows(Exception.class, () -> {
			birthRecordRepository.save(birthRecord);
		});
	}

	@Test
	void whenAddDeathRegistrationThenIdNotZero() {
		setUpDeathRegistration();
		assertNotEquals(0, deathRegistration.getId());
	}

	@Test
	void whenAddDeathRegistrationWithNotNullFieldThenError() {
		assertThrows(Exception.class, () -> {
			deathRegistrationRepository.save(deathRegistration);
		});
	}

	@Test
	void whenAddMarriageRegistrationThenIdNotZero() {
		setUpMarriageRegistration();
		assertNotEquals(0, marriageRegistration.getId());
	}

	@Test
	void whenAddMarriageRegistrationWithNotNullFieldThenError() {
		assertThrows(Exception.class, () -> {
			marriageRegistrationRepository.save(marriageRegistration);
		});
	}

	@Test
	void whenAddDivorceRegistrationThenIdNotZero() {
		setUpDivorceRegistration();
		assertNotEquals(0, divorceRegistration.getId());
	}

	@Test
	void whenAddDivorceRegistrationWithNotNullFieldThenError() {
		assertThrows(Exception.class, () -> {
			divorceRegistrationRepository.save(divorceRegistration);
		});
	}

	@Test
	void whenAddRequestThenListWithRequestOfApplicantNotEmpty() {
		setUpRequest();
		assertNotEquals(0, requestRepository.findByApplicant(citizenRepository.findById(testUser.getPersonId()).get()).size());
	}

	@Test
	void whenAddRequestWithNotNullFieldThenError() {
		assertThrows(Exception.class, () -> {
			requestRepository.save(request);
		});
	}
	
	@Test
	void whenAddUserWithRoleCITIZENThenCanFindCITIZEN() {
		setUpTestUser(Role.CITIZEN);
		assertFalse(citizenRepository.findById(testUser.getPersonId()).isEmpty());
	}

	@Test
	void whenAddUserWithRoleEMPLOYEEThenCanFindEMPLOYEE() {
		setUpTestUser(Role.EMPLOYEE);
		assertFalse(employeeRepository.findById(testUser.getPersonId()).isEmpty());
	}
}
