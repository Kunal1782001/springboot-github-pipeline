package com.example.demo.custRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.ColumnResponseDto;
import com.example.demo.models.TableConfig;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Repository
public class CustomRepo {

	@Autowired
	EntityManager entityManager;
	
	 @Autowired
	  private JdbcTemplate jdbcTemplate;
	
	public List<ColumnResponseDto> getByTable(Long tableId) {

	    String jpql = """
	        SELECT new com.example.demo.dto.ColumnResponseDto(
	            t.id,
	            t.schemaName,
	            t.tableName,
	            t.displayName,
	            t.isActive,
	            t.pagination,
                t.enableSearch,
	            c.id,
	            c.columnName,
	            c.columnLabel,
	            c.isVisible,
	            c.displayOrder,
	            c.allowSorting,
	            c.allowFiltering,
	            c.allowGrouping,
	            c.showHeaderFilter,
	            c.allowEditing,
	            c.columnTemplate,
                c.templatedata,
                c.rowFilter
                
	        )
	        FROM ColumnConfig c
	        JOIN c.table t
	        WHERE t.id = :tableId
	        ORDER BY c.displayOrder
	    """;

	    TypedQuery<ColumnResponseDto> query =
	            entityManager.createQuery(jpql, ColumnResponseDto.class);

	    query.setParameter("tableId", tableId);

	    return query.getResultList();
	}
	
	

	
	
	
	public PageResponse<TableConfig> getPagedTableConfig(

	        int skip,
	        int take,
	        String filter,
	        String sort) {

	    StringBuilder whereClause = new StringBuilder(" WHERE 1=1 ");
	    Map<String, Object> params = new HashMap<>();

	    //FILTER
	    if (filter != null && !filter.isBlank()) {
	        try {
	            ObjectMapper mapper = new ObjectMapper();
	            JsonNode root = mapper.readTree(filter);

	            // MULTI SELECT HEADER FILTER
	            if (root.isArray() && root.size() > 3) {

	                String field = null;
	                List<String> values = new ArrayList<>();

	                for (JsonNode node : root) {
	                    if (node.isArray()) {
	                        field = node.get(0).asText();
	                        values.add(node.get(2).asText());
	                    }
	                }

	                whereClause.append(" AND t.")
	                           .append(field)
	                           .append(" IN :values");

	                params.put("values", values);

	            } 
	            // SINGLE CONDITION
	            else {

	                String field = root.get(0).asText();
	                String operator = root.get(1).asText();
	                String value = root.get(2).asText();

	                String param = field + "_param";

	                switch (operator) {

	                    case "=" -> {
	                        whereClause.append(" AND t.")
	                                   .append(field)
	                                   .append(" = :")
	                                   .append(param);
	                        params.put(param, value);
	                    }

	                    case "<>" -> {
	                        whereClause.append(" AND t.")
	                                   .append(field)
	                                   .append(" <> :")
	                                   .append(param);
	                        params.put(param, value);
	                    }

	                    case "contains" -> {
	                        whereClause.append(" AND LOWER(t.")
	                                   .append(field)
	                                   .append(") LIKE :")
	                                   .append(param);
	                        params.put(param, "%" + value.toLowerCase() + "%");
	                    }

	                    case "notcontains" -> {
	                        whereClause.append(" AND LOWER(t.")
	                                   .append(field)
	                                   .append(") NOT LIKE :")
	                                   .append(param);
	                        params.put(param, "%" + value.toLowerCase() + "%");
	                    }

	                    case "startswith" -> {
	                        whereClause.append(" AND LOWER(t.")
	                                   .append(field)
	                                   .append(") LIKE :")
	                                   .append(param);
	                        params.put(param, value.toLowerCase() + "%");
	                    }

	                    case "endswith" -> {
	                        whereClause.append(" AND LOWER(t.")
	                                   .append(field)
	                                   .append(") LIKE :")
	                                   .append(param);
	                        params.put(param, "%" + value.toLowerCase());
	                    }
	                }
	            }

	        } catch (Exception e) {
	            throw new RuntimeException("Invalid filter format", e);
	        }
	    }

	    // SORT 
	    String orderBy = "";
	    if (sort != null && !sort.isBlank()) {
	        try {
	            ObjectMapper mapper = new ObjectMapper();
	            JsonNode node = mapper.readTree(sort);

	            String field = node.get(0).get("selector").asText();
	            boolean desc = node.get(0).get("desc").asBoolean();

	            orderBy = " ORDER BY t." + field + (desc ? " DESC" : " ASC");

	        } catch (Exception e) {
	            throw new RuntimeException("Invalid sort format", e);
	        }
	    }

	    // DATA QUERY 
	    String dataJpql = """
	        SELECT t
	        FROM TableConfig t
	        """ + whereClause + orderBy;

	    TypedQuery<TableConfig> dataQuery =
	            entityManager.createQuery(dataJpql, TableConfig.class);

	    params.forEach(dataQuery::setParameter);
	    dataQuery.setFirstResult(skip);
	    dataQuery.setMaxResults(take);

	    List<TableConfig> data = dataQuery.getResultList();

	    // COUNT QUERY
	    String countJpql = """
	        SELECT COUNT(t)
	        FROM TableConfig t
	        """ + whereClause;

	    TypedQuery<Long> countQuery =
	            entityManager.createQuery(countJpql, Long.class);

	    params.forEach(countQuery::setParameter);

	    Long totalCount = countQuery.getSingleResult();

	    return new PageResponse<>(data, totalCount);
	}


	

	
	
	
	public List<String> getTablesBySchema(String schemaName) {
		String sql = """
			    SELECT t.table_name
			    FROM information_schema.tables t
			    WHERE t.table_schema = ?
			      AND t.table_type = 'BASE TABLE'
			      AND NOT EXISTS (
			          SELECT 1
			          FROM table_config tc
			          WHERE LOWER(tc.table_name) = LOWER(t.table_name)
			            AND LOWER(tc.schema_name) = LOWER(?)
			      )
			    ORDER BY t.table_name
			""";
	    return jdbcTemplate.queryForList(sql, String.class, schemaName, schemaName);
	}

}
