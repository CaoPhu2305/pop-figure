package com.caophu2305.popfigure.repository;

import com.caophu2305.popfigure.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
