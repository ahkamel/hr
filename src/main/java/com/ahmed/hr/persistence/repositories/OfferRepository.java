package com.ahmed.hr.persistence.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ahmed.hr.persistence.entities.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

	Optional<Offer> findByJobTitle(String jobTitle);
}
