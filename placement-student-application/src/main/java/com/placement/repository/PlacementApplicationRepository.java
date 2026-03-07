package com.placement.repository;

import com.placement.domain.PlacementApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlacementApplicationRepository extends JpaRepository<PlacementApplication, Long> {

    List<PlacementApplication> findByStudentId(Long studentId);
    List<PlacementApplication> findByPlacementOfferId(Long offerId);
    Optional<PlacementApplication> findByStudentIdAndPlacementOfferId(Long studentId, Long offerId);
    boolean existsByStudentIdAndPlacementOfferId(Long studentId, Long offerId);
}
