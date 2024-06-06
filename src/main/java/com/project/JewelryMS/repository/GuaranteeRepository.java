package com.project.JewelryMS.repository;


import com.project.JewelryMS.entity.Guarantee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GuaranteeRepository extends JpaRepository<Guarantee, Long> {
    List<Guarantee> findByStatus(boolean b);
}