package com.example.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "table_config")
public class TableConfig {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String schemaName;
	   
	    private String tableName;
	    private String displayName;
	    private Boolean isActive;
	    
	     
	    private Boolean allowSorting;
	    private Boolean enableSearch;
	    
	    private Boolean allowColumnReordering;
	    private Boolean rowFilter;
	    private Boolean showHeaderFilter;
	    private Boolean pagination;
	    
	    @Column(name = "allow_grouping")
	    private Boolean grouping;
	    
	    private Boolean columnChooser;
		    
	    private Boolean exportData;
		   
		  
		public TableConfig() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		
        


		public TableConfig(Long id, String schemaName, String tableName, String displayName, Boolean isActive,
				Boolean allowSorting, Boolean enableSearch, Boolean allowColumnReordering, Boolean showHeaderFilter,Boolean pagination,Boolean rowFilter,
				Boolean columnChooser,Boolean grouping,Boolean exportData) {
			super();
			this.id = id;
			this.schemaName = schemaName;
			this.tableName = tableName;
			this.displayName = displayName;
			this.isActive = isActive;
			this.allowSorting = allowSorting;
			this.enableSearch = enableSearch;
			this.allowColumnReordering = allowColumnReordering;
			this.showHeaderFilter = showHeaderFilter;
			this.pagination=pagination;
			this.rowFilter=rowFilter;
			this.columnChooser=columnChooser;
			this.grouping=grouping;
			this.exportData= exportData;
		}









		public Boolean getExportData() {
			return exportData;
		}





		public void setExportData(Boolean exportData) {
			this.exportData = exportData;
		}





		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getSchemaName() {
			return schemaName;
		}
		public void setSchemaName(String schemaName) {
			this.schemaName = schemaName;
		}
		
		public String getTableName() {
			return tableName;
		}
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		public String getDisplayName() {
			return displayName;
		}
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
		public Boolean getIsActive() {
			return isActive;
		}
		public void setIsActive(Boolean isActive) {
			this.isActive = isActive;
		}





		public Boolean getAllowSorting() {
			return allowSorting;
		}





		public void setAllowSorting(Boolean allowSorting) {
			this.allowSorting = allowSorting;
		}





		public Boolean getEnableSearch() {
			return enableSearch;
		}





		public void setEnableSearch(Boolean enableSearch) {
			this.enableSearch = enableSearch;
		}





		public Boolean getAllowColumnReordering() {
			return allowColumnReordering;
		}





		public void setAllowColumnReordering(Boolean allowColumnReordering) {
			this.allowColumnReordering = allowColumnReordering;
		}





		public Boolean getShowHeaderFilter() {
			return showHeaderFilter;
		}





		public void setShowHeaderFilter(Boolean showHeaderFilter) {
			this.showHeaderFilter = showHeaderFilter;
		}





		public Boolean getPagination() {
			return pagination;
		}





		public void setPagination(Boolean pagination) {
			this.pagination = pagination;
		}





		public Boolean getRowFilter() {
			return rowFilter;
		}





		public void setRowFilter(Boolean rowFilter) {
			this.rowFilter = rowFilter;
		}





		public Boolean getGrouping() {
			return grouping;
		}





		public void setGrouping(Boolean grouping) {
			this.grouping = grouping;
		}





		public Boolean getColumnChooser() {
			return columnChooser;
		}





		public void setColumnChooser(Boolean columnChooser) {
			this.columnChooser = columnChooser;
		}


       
		
	    
	    

}
