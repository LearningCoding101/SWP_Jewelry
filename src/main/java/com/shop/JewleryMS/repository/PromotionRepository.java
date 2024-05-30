package com.shop.JewleryMS.repository;

import com.shop.JewleryMS.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByStatus(boolean b);
    Optional<Promotion> findByCode(String n);
}
