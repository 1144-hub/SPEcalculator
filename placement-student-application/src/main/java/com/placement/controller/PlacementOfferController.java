package com.placement.controller;

import com.placement.domain.PlacementOffer;
import com.placement.dto.PlacementOfferDto;
import com.placement.repository.PlacementOfferRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/placement-offers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PlacementOfferController {

    private final PlacementOfferRepository offerRepository;

    @GetMapping
    public List<PlacementOfferDto> getAllOffers() {
        return offerRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlacementOfferDto> getOffer(@PathVariable Long id) {
        return offerRepository.findById(id)
                .map(o -> ResponseEntity.ok(toDto(o)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PlacementOfferDto> createOffer(@Valid @RequestBody PlacementOfferDto dto) {
        PlacementOffer offer = toEntity(dto);
        offer = offerRepository.save(offer);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(offer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlacementOfferDto> updateOffer(@PathVariable Long id, @Valid @RequestBody PlacementOfferDto dto) {
        return offerRepository.findById(id)
                .map(o -> {
                    o.setCompanyName(dto.getCompanyName());
                    o.setRequiredDomain(dto.getRequiredDomain());
                    o.setRequiredSpecialization(dto.getRequiredSpecialization());
                    o.setMinCreditsRequired(dto.getMinCreditsRequired());
                    o.setMinCumulativeGradeRequired(dto.getMinCumulativeGradeRequired());
                    o.setDescription(dto.getDescription());
                    return ResponseEntity.ok(toDto(offerRepository.save(o)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        if (offerRepository.existsById(id)) {
            offerRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private PlacementOfferDto toDto(PlacementOffer o) {
        return PlacementOfferDto.builder()
                .id(o.getId())
                .companyName(o.getCompanyName())
                .requiredDomain(o.getRequiredDomain())
                .requiredSpecialization(o.getRequiredSpecialization())
                .minCreditsRequired(o.getMinCreditsRequired())
                .minCumulativeGradeRequired(o.getMinCumulativeGradeRequired())
                .description(o.getDescription())
                .build();
    }

    private PlacementOffer toEntity(PlacementOfferDto dto) {
        return PlacementOffer.builder()
                .companyName(dto.getCompanyName())
                .requiredDomain(dto.getRequiredDomain())
                .requiredSpecialization(dto.getRequiredSpecialization())
                .minCreditsRequired(dto.getMinCreditsRequired())
                .minCumulativeGradeRequired(dto.getMinCumulativeGradeRequired())
                .description(dto.getDescription())
                .build();
    }
}
