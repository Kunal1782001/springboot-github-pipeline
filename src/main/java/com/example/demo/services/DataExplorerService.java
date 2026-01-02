package com.example.demo.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.custRepo.PageResponse;

@Service
public class DataExplorerService {
	
	    @Autowired
	    private JdbcTemplate jdbcTemplate;

	    public List<Map<String, Object>> getTableColumns(String schema, String tableName) {

	        schema = schema.trim();
	        tableName = tableName.trim();

	        String sql = """
	            SELECT column_name, data_type
	            FROM information_schema.columns
	            WHERE table_schema = ?
	              AND table_name = ?
	            ORDER BY ordinal_position
	        """;

	        List<Map<String, Object>> cols = jdbcTemplate.queryForList(sql, schema, tableName);
	        System.out.println("Columns fetched: " + cols); // debug
	        return cols;
	    }


	    
	    
	

	    

}
