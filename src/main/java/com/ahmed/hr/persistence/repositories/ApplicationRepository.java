package com.ahmed.hr.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ahmed.hr.persistence.entities.Application;
import com.ahmed.hr.persistence.entities.Offer;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

	Optional<Application> findByOfferAndEmail(Offer offer, String email);

	List<Application> findByOffer(Offer offer);

	Long countByOffer(Offer offer);
}
