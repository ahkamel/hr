package com.ahmed.hr.persistence.repositories;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.ahmed.hr.persistence.entities.Application;
import com.ahmed.hr.persistence.entities.Offer;
import com.ahmed.hr.persistence.enums.ApplicationStatus;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ApplicationRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private ApplicationRepository applicationRepository;
	@Autowired
	private OfferRepository offerRepository;

	@Test
	public void testFindByOffer() {

		Offer savedOffer = initApplication();
		clearSession();
		List<Application> applications = applicationRepository.findByOffer(savedOffer);
		assertEquals(1, applications.size());

		assertEquals("ahmed.dohair@gmail.com", applications.get(0).getEmail());

	}

	@Test
	public void testFindByOfferAndEmail() {

		Offer savedOffer = initApplication();
		clearSession();
		Optional<Application> application = applicationRepository.findByOfferAndEmail(savedOffer,
				"ahmed.dohair@gmail.com");
		assertEquals(true, application.isPresent());

	}

	@Test
	public void testCountByOffer() {

		Offer savedOffer = initApplication();
		clearSession();
		long count = applicationRepository.countByOffer(savedOffer);
		assertEquals(1, count);

	}

	private Offer initApplication() {
		Offer offer = new Offer();
		offer.setJobTitle("Senior Java Developer");
		offer.setStartDate(Calendar.getInstance());
		Offer savedOffer = offerRepository.save(offer);
		clearSession();
		Application application = new Application();
		application.setEmail("ahmed.dohair@gmail.com");
		application.setOffer(savedOffer);
		application.setResume("CV...");
		application.setStatus(ApplicationStatus.APPLIED);

		applicationRepository.save(application);
		return savedOffer;
	}

	public void clearSession() {
		entityManager.flush();
		entityManager.clear();
	}

	@After
	public void cleanUp() {
		offerRepository.deleteAll();
		applicationRepository.deleteAll();
	}
}
