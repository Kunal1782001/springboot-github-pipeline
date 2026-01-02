package com.example.demo.controllers
;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.custRepo.PageResponse;
import com.example.demo.services.DataExplorerService;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/data")
@CrossOrigin("*")
public class DataExplorerController {

    @Autowired
    private DataExplorerService service;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    


    
//    @GetMapping("/{schemaName}/{tableName}")
//    public List<Map<String, Object>> getTableData(
//            @PathVariable String schemaName,
//            @PathVariable String tableName) {
//
//        String sql = String.format(
//            "SELECT * FROM %s.%s",
//            schemaName,
//            tableName
//        );
//
//        return jdbcTemplate.queryForList(sql);
//    }
    
    @GetMapping("/paged/{schema}/{table}")
    public Map<String, Object> getPagedTableData(
            @PathVariable String schema,
            @PathVariable String table,
            @RequestParam int skip,
            @RequestParam int take,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String sort
    ) {

        StringBuilder whereClause = new StringBuilder(" WHERE 1=1 ");
        Map<String, Object> params = new HashMap<>();

        /* ================= FILTER ================= */
        if (filter != null && !filter.isBlank()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode filterNode = mapper.readTree(filter);
                applyFilter(filterNode, whereClause, params);
            } catch (Exception e) {
                throw new RuntimeException("Invalid filter format", e);
            }
        }

        /* ================= SORT ================= */
        String orderBy = "";
        if (sort != null && !sort.isBlank()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(sort);

                String field = node.get(0).get("selector").asText();
                boolean desc = node.get(0).get("desc").asBoolean();

                orderBy = " ORDER BY " + field + (desc ? " DESC" : " ASC");

            } catch (Exception e) {
                throw new RuntimeException("Invalid sort format", e);
            }
        }

        /* ================= SQL ================= */
        String dataSql =
                "SELECT * FROM " + schema + "." + table +
                whereClause +
                orderBy +
                " LIMIT :take OFFSET :skip";

        String countSql =
                "SELECT COUNT(*) FROM " + schema + "." + table +
                whereClause;

        params.put("skip", skip);
        params.put("take", take);

        List<Map<String, Object>> data =
                namedJdbcTemplate.queryForList(dataSql, params);

        Integer total =
                namedJdbcTemplate.queryForObject(countSql, params, Integer.class);

        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("totalCount", total);

        return response;
    }

    
    private void applyFilter(
            JsonNode node,
            StringBuilder whereClause,
            Map<String, Object> params
    ) {

        if (!node.isArray()) return;

      
        if (node.size() == 3 && node.get(0).isTextual()) {

            String field = node.get(0).asText();
            String operator = node.get(1).asText();
            JsonNode valueNode = node.get(2);

            String param = field.replace(".", "_") + params.size();

            switch (operator) {

                case "=" -> {
                    whereClause.append(" AND ").append(field)
                               .append(" = :").append(param);
                    params.put(param, valueNode.asText());
                }

                case "<>" -> {
                    whereClause.append(" AND ").append(field)
                               .append(" <> :").append(param);
                    params.put(param, valueNode.asText());
                }

                case "contains" -> {
                    whereClause.append(" AND LOWER(").append(field)
                               .append(") LIKE :").append(param);
                    params.put(param, "%" + valueNode.asText().toLowerCase() + "%");
                }

                case "notcontains" -> {
                    whereClause.append(" AND LOWER(").append(field)
                               .append(") NOT LIKE :").append(param);
                    params.put(param, "%" + valueNode.asText().toLowerCase() + "%");
                }

                case "startswith" -> {
                    whereClause.append(" AND LOWER(").append(field)
                               .append(") LIKE :").append(param);
                    params.put(param, valueNode.asText().toLowerCase() + "%");
                }

                case "endswith" -> {
                    whereClause.append(" AND LOWER(").append(field)
                               .append(") LIKE :").append(param);
                    params.put(param, "%" + valueNode.asText().toLowerCase());
                }
            }
        }
        // Nested AND  OR filters
        else {
            for (JsonNode child : node) {
                if (child.isTextual()) continue;
                applyFilter(child, whereClause, params);
            }
        }
    }


    @GetMapping("/{schema}/{table}/distinct/{column}")
    public List<String> getDistinctValues(
            @PathVariable String schema,
            @PathVariable String table,
            @PathVariable String column) {

        String sql = String.format(
            "SELECT DISTINCT %s FROM %s.%s ORDER BY %s",
            column, schema, table, column
        );

        return jdbcTemplate.queryForList(sql, String.class);
    }


}

