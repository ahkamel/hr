package com.ahmed.hr.services;

import java.util.List;
import java.util.Optional;

import com.ahmed.hr.persistence.entities.Application;
import com.ahmed.hr.persistence.entities.Offer;

public interface ApplicationService {

	Application save(Application application);

	Optional<Application> get(Long id);

	List<Application> findAll();

	Optional<Application> findByOfferAndEmail(Offer offer, String email);

	List<Application> findByOffer(Offer offer);

	long count();

	Long countByOffer(Offer offer);
}
