package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.custRepo.CustomRepo;
import com.example.demo.custRepo.PageResponse;
import com.example.demo.dto.ColumnOrderUpdateDto;
import com.example.demo.dto.ColumnResponseDto;
import com.example.demo.models.ColumnConfig;
import com.example.demo.repos.ColumnConfigRepository;

import jakarta.transaction.Transactional;

@Service
public class ColumnConfigService {

    @Autowired
    private ColumnConfigRepository repo;
    
    @Autowired
    ColumnConfigRepository columnConfigRepository;
    
    @Autowired
    CustomRepo customRepo;
    
    

    public List<ColumnResponseDto> getByTable(Long tableId) {
        return customRepo.getByTable(tableId);
    }
    
    
//    public PageResponse<ColumnResponseDto> getBypagedTable(
//            Long tableId,
//            int skip,
//            int take
//    ) {
//        return customRepo.getBypagedTable(tableId, skip, take);
//    }
    
    
    @Transactional
    public ColumnConfig updateColumn(Long id, ColumnConfig updatedData) {

        ColumnConfig existing = columnConfigRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Column not found with id " + id));

        
       
        existing.setColumnLabel(updatedData.getColumnLabel());
        existing.setIsVisible(updatedData.getIsVisible());
//        existing.setDisplayOrder(updatedData.getDisplayOrder());

        
        existing.setAllowSorting(updatedData.getAllowSorting());
        existing.setAllowGrouping(updatedData.getAllowGrouping());
        existing.setAllowFiltering(updatedData.getAllowFiltering());
        existing.setAllowEditing(updatedData.getAllowEditing());
        existing.setShowHeaderFilter(updatedData.getShowHeaderFilter());
        existing.setRowFilter(updatedData.getRowFilter());
        existing.setColumnTemplate(updatedData.getColumnTemplate());
        existing.setTemplatedata(updatedData.getTemplatedata());
       
        

        return repo.save(existing);
    }
    
    
    
    @Transactional
    public void updateOrder(List<ColumnOrderUpdateDto> list) {

    	 for (ColumnOrderUpdateDto dto : list) {
    	        ColumnConfig column = columnConfigRepository
    	            .findById(dto.getId())
    	            .orElseThrow(() ->
    	                new RuntimeException("Column not found: " + dto.getId()));

    	        column.setDisplayOrder(dto.getDisplayOrder());
    	        columnConfigRepository.save(column);
    	    }

    	    columnConfigRepository.flush(); // ✅ ensures DB update
    }
}
