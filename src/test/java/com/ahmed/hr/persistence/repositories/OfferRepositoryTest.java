package com.ahmed.hr.persistence.repositories;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Optional;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.ahmed.hr.persistence.entities.Offer;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OfferRepositoryTest {

	@Autowired
	private OfferRepository offerRepository;

	@Test
	public void testFindByJobTitle() {
		Optional<Offer> notFoundOffer = offerRepository.findByJobTitle("Senior python devleoper");
		assertEquals(false, notFoundOffer.isPresent());
		Offer offer = new Offer();
		offer.setJobTitle("Senior Java Developer");
		offer.setStartDate(Calendar.getInstance());
		offerRepository.save(offer);

		Optional<Offer> foundOffer = offerRepository.findByJobTitle("Senior Java Developer");

		assertEquals(foundOffer.isPresent(), true);
		assertEquals("Senior Java Developer", foundOffer.get().getJobTitle());
	}

	@After
	public void cleanUp() {
		offerRepository.deleteAll();
	}

}
