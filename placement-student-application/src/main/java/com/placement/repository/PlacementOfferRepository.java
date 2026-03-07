package com.placement.repository;

import com.placement.domain.PlacementOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlacementOfferRepository extends JpaRepository<PlacementOffer, Long> {
}
