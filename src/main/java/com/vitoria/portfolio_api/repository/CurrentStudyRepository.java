package com.vitoria.portfolio_api.repository;
import com.vitoria.portfolio_api.entity.CurrentStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentStudyRepository extends JpaRepository<CurrentStudy, Integer> {}