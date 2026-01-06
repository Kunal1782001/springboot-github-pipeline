package com.example.demo.services;

import java.util.Arrays;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;




import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.custRepo.CustomRepo;
import com.example.demo.custRepo.PageResponse;
import com.example.demo.models.ColumnConfig;
import com.example.demo.models.TableConfig;
import com.example.demo.repos.ColumnConfigRepository;
import com.example.demo.repos.TableConfigRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import org.json.JSONArray;

@Service
public class TableConfigService {

	@Autowired
	private TableConfigRepository repo;

	@Autowired
	ColumnConfigRepository columnConfigRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	EntityManager entityManager;

	@Autowired
	CustomRepo customRepo;

	public TableConfig save(TableConfig config) {

		TableConfig savedTable = repo.save(config);

		// Fetch columns from DB metadata
		List<String> columnNames = getColumnsFromDB(savedTable.getSchemaName(), savedTable.getTableName());

		int displayOrder = 1;

		for (String colName : columnNames) {
			ColumnConfig column = new ColumnConfig();
			column.setTable(savedTable);
			column.setColumnName(colName);
			column.setColumnLabel(toTitleCase(colName));
			column.setDisplayOrder(displayOrder++);
			column.setIsVisible(savedTable.getIsActive());

			column.setAllowSorting(Boolean.TRUE);
			column.setAllowFiltering(Boolean.TRUE);
			column.setAllowGrouping(Boolean.TRUE);
			column.setShowHeaderFilter(Boolean.TRUE);
			column.setAllowEditing(Boolean.TRUE);
			column.setRowFilter(Boolean.TRUE);
			column.setColumnTemplate(Boolean.FALSE);
			column.setTemplatedata(null);

			columnConfigRepository.save(column);
		}

		return savedTable;
	}

	public List<TableConfig> getAllActiveTabels() {
		return repo.findAll();
	}


	
	public PageResponse<TableConfig> getAllActive(
	        int skip,
	        int take,
	        String filter,
	        String sort) {

	    return customRepo.getPagedTableConfig(skip, take, filter, sort);
	}


// this is the helper method 
	
	private Predicate buildPredicate(
	        CriteriaBuilder cb,
	        Root<TableConfig> root,
	        String field,
	        String operator,
	        List<String> values) {

	    switch (operator) {

	        case "=":
	            if (values.size() == 1) {
	                return cb.equal(root.get(field), values.get(0));
	            }
	            // MULTI SELECT → IN clause
	            return root.get(field).in(values);

	        case "contains":
	            return cb.or(
	                values.stream()
	                      .map(v -> cb.like(
	                              cb.lower(root.get(field)),
	                              "%" + v.toLowerCase() + "%"))
	                      .toArray(Predicate[]::new)
	            );

	        default:
	            return null;
	    }
	}

	


	public List<String> getSchemas() {
		String sql = """
				    SELECT schema_name
				    FROM information_schema.schemata
				    WHERE schema_name NOT IN (
				        'information_schema',
				        'mysql',
				        'performance_schema',
				        'sys'
				    )
				    ORDER BY schema_name
				""";

		return jdbcTemplate.queryForList(sql, String.class);
	}

	public List<String> getTableByschemas(String schemas) {
		return customRepo.getTablesBySchema(schemas);
	}

	private List<String> getColumnsFromDB(String schemaName, String tableName) {
		String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = :schema AND TABLE_NAME = :table ORDER BY ORDINAL_POSITION";
		return entityManager.createNativeQuery(sql).setParameter("schema", schemaName).setParameter("table", tableName)
				.getResultList();
	}

	private String toTitleCase(String input) {
		return Arrays.stream(input.split("_")).map(w -> w.substring(0, 1).toUpperCase() + w.substring(1).toLowerCase())
				.collect(Collectors.joining(" "));
	}

	@Transactional
	public void deleteTable(Long tableId) {
		// Delete columns first
		columnConfigRepository.findByTable_IdOrderByDisplayOrder(tableId).forEach(columnConfigRepository::delete);

		// Delete table
		repo.deleteById(tableId);
	}


	@Transactional
	public TableConfig updateTable(Long id, TableConfig updatedData) {

		TableConfig existing = repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Table not found with id " + id));

		if (updatedData.getDisplayName() != null) {
			existing.setDisplayName(updatedData.getDisplayName());
		}

		if (updatedData.getIsActive() != null) {
			existing.setIsActive(updatedData.getIsActive());
		}

		if (updatedData.getAllowSorting() != null) {
			existing.setAllowSorting(updatedData.getAllowSorting());
		}

		if (updatedData.getEnableSearch() != null) {
			existing.setEnableSearch(updatedData.getEnableSearch());
		}

		if (updatedData.getAllowColumnReordering() != null) {
			existing.setAllowColumnReordering(updatedData.getAllowColumnReordering());
		}

		if (updatedData.getShowHeaderFilter() != null) {
			existing.setShowHeaderFilter(updatedData.getShowHeaderFilter());
		}

		if (updatedData.getRowFilter() != null) {
			existing.setRowFilter(updatedData.getRowFilter());
		}

		if (updatedData.getPagination() != null) {
			existing.setPagination(updatedData.getPagination());
		}

		if (updatedData.getColumnChooser() != null) {
			existing.setColumnChooser(updatedData.getColumnChooser());
		}
		
		if (updatedData.getGrouping() != null) {
			existing.setGrouping(updatedData.getGrouping());;
		}
		
		if (updatedData.getExportData() != null) {
			existing.setExportData(updatedData.getExportData());;
		}

		return repo.save(existing);
	}

	public List<String> getDistinctTableNames() {
		return repo.findDistinctTableNames();
	}
}
