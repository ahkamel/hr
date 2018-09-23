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

import com.ahmed.hr.persistence.entities.Offer;
import com.ahmed.hr.persistence.repositories.OfferRepository;

@RunWith(SpringRunner.class)
public class OfferServiceTest {
	private static final String SENIOR_SOFTWARE_ENGINEER = "Senior Software Engineer";

	@TestConfiguration
	static class OfferServiceImplTestContextConfiguration {
		@Bean
		public OfferService offerService() {
			return new OfferServiceImpl();
		}
	}

	@Autowired
	private OfferService offerService;

	@MockBean
	private OfferRepository offerRepository;

	private Offer offer1;
	private Offer offer2;

	@Before
	public void setUp() {

		offer1 = new Offer();
		offer1.setId(5L);
		offer2 = new Offer();
		offer2.setId(8L);
		offer2.setJobTitle(SENIOR_SOFTWARE_ENGINEER);

		List<Offer> offers = Arrays.asList(offer1, offer2);
		Mockito.when(offerRepository.save(offer1)).thenReturn(offer1);
		Mockito.when(offerRepository.findById(5L)).thenReturn(Optional.of(offer1));
		Mockito.when(offerRepository.findByJobTitle(SENIOR_SOFTWARE_ENGINEER)).thenReturn(Optional.of(offer2));
		Mockito.when(offerRepository.findAll()).thenReturn(offers);
	}

	@Test
	public void testSaveOffer() {
		Offer offer = offerService.save(offer1);
		assertEquals(5L, offer.getId().longValue());
	}

	@Test
	public void testFindOffer() {
		Optional<Offer> offer = offerService.findById(5L);
		assertEquals(true, offer.isPresent());
		assertEquals(5L, offer.get().getId().longValue());
	}

	@Test
	public void testFindByJobTitle() {
		Optional<Offer> offer = offerService.findByJobTitle(SENIOR_SOFTWARE_ENGINEER);
		assertEquals(true, offer.isPresent());
		assertEquals(8L, offer.get().getId().longValue());
	}

	@Test
	public void testFindAll() {
		List<Offer> offers = offerService.findAll();
		assertEquals(2, offers.size());
	}

	@Test
	public void testIsOfferExist() {
		Boolean found = offerService.isOfferExist(offer2);
		assertEquals(true, found);
	}
}
