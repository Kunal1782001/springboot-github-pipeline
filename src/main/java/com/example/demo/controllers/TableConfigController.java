package com.example.demo.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.custRepo.PageResponse;
import com.example.demo.models.TableConfig;
import com.example.demo.services.TableConfigService;

@RestController
@RequestMapping("/api/table-config")
@CrossOrigin("*")
public class TableConfigController {

    @Autowired
    private TableConfigService service;

    @PostMapping
    public TableConfig save(@RequestBody TableConfig config) {
        return service.save(config);
    }

    @GetMapping("/tablelist")
    public List<TableConfig> list() {
        return service.getAllActiveTabels();
    }
    
    @GetMapping("")
    public Map<String, Object> getTableConfig(
            @RequestParam int skip,
            @RequestParam int take,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String sort) {

    	PageResponse<TableConfig> page = service.getAllActive(skip, take,filter,sort);

        Map<String, Object> response = new HashMap<>();
        response.put("data", page.getData());
        response.put("totalCount", page.getTotalCount());

        return response;
    }
    
    
    
    @GetMapping("/schemas")
    public List<String> getAllSchemas() {
        return service.getSchemas();
    }
    
    @GetMapping("/tables/{schemas}")
    public List<String> getAllTabelsBySchemas(@PathVariable String schemas) {
        return service.getTableByschemas(schemas);
    }
    
 // Update table
    @PutMapping("/{id}")
    public TableConfig update(@PathVariable Long id, @RequestBody TableConfig config) {
        return service.updateTable(id, config);
    }

    // Delete table
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteTable(id);
    }

    
    @GetMapping("/table-names")
    public List<String> getAllTableNames() {
        return service.getDistinctTableNames();
    }
   
}

