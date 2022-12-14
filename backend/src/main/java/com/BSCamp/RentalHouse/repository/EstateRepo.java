package com.BSCamp.RentalHouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.BSCamp.RentalHouse.entity.Category;
import com.BSCamp.RentalHouse.entity.Estate;

@EnableJpaRepositories
public interface EstateRepo extends JpaRepository<Estate, Integer> {
	public List<Estate> findByCategory(Category category);

}
