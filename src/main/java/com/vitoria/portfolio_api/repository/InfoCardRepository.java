package com.vitoria.portfolio_api.repository;
import com.vitoria.portfolio_api.entity.InfoCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoCardRepository extends JpaRepository<InfoCard, Integer> {}