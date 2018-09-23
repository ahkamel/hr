package com.ahmed.hr.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahmed.hr.persistence.entities.Offer;
import com.ahmed.hr.persistence.repositories.OfferRepository;

@Service
@Transactional(rollbackFor = Throwable.class)
public class OfferServiceImpl implements OfferService {

	@Autowired
	private OfferRepository offerRepository;

	@Override
	public Offer save(Offer offer) {
		return offerRepository.save(offer);
	}

	@Override
	public Optional<Offer> findById(Long id) {
		return offerRepository.findById(id);
	}

	@Override
	public Optional<Offer> findByJobTitle(String jobTitle) {
		return offerRepository.findByJobTitle(jobTitle);
	}

	@Override
	public List<Offer> findAll() {
		return offerRepository.findAll();
	}

	@Override
	public boolean isOfferExist(@Valid Offer offer) {
		return findByJobTitle(offer.getJobTitle()).isPresent();
	}

}
