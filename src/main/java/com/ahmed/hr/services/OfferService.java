package com.ahmed.hr.services;

import java.util.List;
import java.util.Optional;

import com.ahmed.hr.persistence.entities.Offer;

public interface OfferService {

	Offer save(Offer offer);

	Optional<Offer> findById(Long id);

	Optional<Offer> findByJobTitle(String jobTitle);

	List<Offer> findAll();

	boolean isOfferExist(Offer offer);
}
