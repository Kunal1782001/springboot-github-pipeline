package com.example.demo.repos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.models.TableConfig;

public interface TableConfigRepository extends JpaRepository<TableConfig, Long>, JpaSpecificationExecutor<TableConfig> {
	
	
    @Query("SELECT DISTINCT t.displayName FROM TableConfig t")
    List<String> findDistinctTableNames();
}