package com.vitoria.portfolio_api.repository;
import com.vitoria.portfolio_api.entity.ProjectHighlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectHighlightRepository extends JpaRepository<ProjectHighlight, Integer> {}