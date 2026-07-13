package com.vitoria.portfolio_api.repository;
import com.vitoria.portfolio_api.entity.TechCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechCategoryRepository extends JpaRepository<TechCategory, Integer> {}