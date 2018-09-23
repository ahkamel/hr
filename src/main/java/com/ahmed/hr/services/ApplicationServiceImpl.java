package com.ahmed.hr.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahmed.hr.persistence.entities.Application;
import com.ahmed.hr.persistence.entities.Offer;
import com.ahmed.hr.persistence.repositories.ApplicationRepository;

@Service
@Transactional(rollbackFor = Throwable.class)
public class ApplicationServiceImpl implements ApplicationService {

	@Autowired
	private ApplicationRepository applicationRepository;

	@Override
	public Application save(Application application) {
		return applicationRepository.save(application);
	}

	@Override
	public Optional<Application> get(Long id) {
		return applicationRepository.findById(id);
	}

	@Override
	public List<Application> findAll() {
		return applicationRepository.findAll();
	}

	@Override
	public Optional<Application> findByOfferAndEmail(Offer offer, String email) {
		return applicationRepository.findByOfferAndEmail(offer, email);
	}

	@Override
	public List<Application> findByOffer(Offer offer) {
		return applicationRepository.findByOffer(offer);
	}

	@Override
	public long count() {
		return applicationRepository.count();
	}

	@Override
	public Long countByOffer(Offer offer) {
		return applicationRepository.countByOffer(offer);
	}

}
