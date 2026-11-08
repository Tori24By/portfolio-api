package com.vitoria.portfolio_api.repository;

import com.vitoria.portfolio_api.entity.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Integer> {
}
