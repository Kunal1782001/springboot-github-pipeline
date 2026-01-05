package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.custRepo.PageResponse;
import com.example.demo.dto.ColumnOrderUpdateDto;
import com.example.demo.dto.ColumnResponseDto;
import com.example.demo.models.ColumnConfig;
import com.example.demo.services.ColumnConfigService;

@RestController
@RequestMapping("/api/column-config")
@CrossOrigin("*")
public class ColumnConfigController {

    @Autowired
    private ColumnConfigService service;

    @GetMapping("/{tableId}")
    public List<ColumnResponseDto> getColumns(@PathVariable Long tableId) {
        //this is my updated file
        return service.getByTable(tableId);
    }
    
    
    @PutMapping("/{id}")
    public ColumnConfig update(
            @PathVariable Long id,
            @RequestBody ColumnConfig config) {

        return service.updateColumn(id, config);
    }
 
    
    @PutMapping("/reorder")
    public void reorderColumns(@RequestBody List<ColumnOrderUpdateDto> list) {
    	   service.updateOrder(list);
    }
}
