package com.vitoria.portfolio_api.repository;
import com.vitoria.portfolio_api.entity.AboutMe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AboutMeRepository extends JpaRepository<AboutMe, Integer> {}