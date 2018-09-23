package com.ahmed.hr.services;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.ahmed.hr.persistence.entities.Application;
import com.ahmed.hr.persistence.entities.Offer;
import com.ahmed.hr.persistence.repositories.ApplicationRepository;

@RunWith(SpringRunner.class)
public class ApplicationServiceTest {
	@TestConfiguration
	static class ApplicaitonServiceImplTestContextConfiguration {
		@Bean
		public ApplicationService applicationService() {
			return new ApplicationServiceImpl();
		}
	}

	@Autowired
	private ApplicationService applicationService;

	@MockBean
	private ApplicationRepository applicationRepository;

	private Offer offer;
	private Application application1;

	@Before
	public void setUp() {
		application1 = new Application();
		application1.setId(1L);
		application1.setEmail("john@gmail.com");

		offer = new Offer();
		offer.setId(5L);
		Application application2 = new Application();
		application2.setEmail("ahmed@gmail.com");
		application2.setOffer(offer);
		Application application3 = new Application();
		application2.setOffer(offer);
		List<Application> allApplications = Arrays.asList(application1, application2, application3);
		List<Application> allApplication2 = Arrays.asList(application2, application3);

		Mockito.when(applicationRepository.findById(1L)).thenReturn(Optional.of(application1));
		Mockito.when(applicationRepository.findAll()).thenReturn(allApplications);
		Mockito.when(applicationRepository.findByOfferAndEmail(offer, "ahmed@gmail.com"))
				.thenReturn(Optional.ofNullable(application2));
		Mockito.when(applicationRepository.findByOffer(offer)).thenReturn(allApplication2);
		Mockito.when(applicationRepository.findById(-99L)).thenReturn(Optional.empty());
		Mockito.when(applicationRepository.count()).thenReturn(3L);
		Mockito.when(applicationRepository.countByOffer(offer)).thenReturn(2L);
		Mockito.when(applicationRepository.save(application1)).thenReturn(application1);
	}

	@Test
	public void testGetApplication() {
		Optional<Application> found = applicationService.get(1L);

		assertEquals(true, found.isPresent());
		assertEquals("john@gmail.com", found.get().getEmail());
	}

	@Test
	public void testFindAllApplications() {
		List<Application> applications = applicationService.findAll();

		assertEquals(3, applications.size());
	}

	@Test
	public void testFindByOfferAndEmail() {
		Optional<Application> application = applicationService.findByOfferAndEmail(offer, "ahmed@gmail.com");
		assertEquals(true, application.isPresent());
		assertEquals("ahmed@gmail.com", application.get().getEmail());

	}

	@Test
	public void testSave() {
		Application savedApplication = applicationService.save(application1);
		assertEquals("john@gmail.com", savedApplication.getEmail());

	}

	@Test
	public void testFindByOffer() {
		List<Application> applications = applicationService.findByOffer(offer);
		assertEquals(2, applications.size());

	}

	@Test
	public void testCountByOffer() {
		long count = applicationService.countByOffer(offer);
		assertEquals(2, count);

	}

	@Test
	public void testCount() {
		long count = applicationService.count();
		assertEquals(3, count);

	}
}
