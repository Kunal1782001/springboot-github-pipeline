package com.example.demo.dto;

import com.example.demo.models.TableConfig;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class ColumnResponseDto {
	
	
	
	    private Long id;
	    private String schemaName;
	    private String tableName;
	    private String displayName;
	    private Boolean isActive;
	    private Long cid;
	    private String  columnName;
	    private String  columnLabel;
	    private Boolean isVisible;
	    private Integer displayOrder;
	    
	    private Boolean allowSorting;
		private Boolean allowFiltering;
		private Boolean allowGrouping;
		private Boolean showHeaderFilter;
		private Boolean rowFilter;
		private Boolean allowEditing;
		private Boolean columnTemplate;
		private String templatedata;
		private Boolean pagination;
		private Boolean enableSearch;
	    
		public ColumnResponseDto() {
			super();
			// TODO Auto-generated constructor stub
		}

		

		

		public ColumnResponseDto(
			    Long id,
			    String schemaName,
			    String tableName,
			    String displayName,
			    Boolean isActive,
			    Boolean pagination,
			    Boolean enableSearch,
			    Long cid,
			    String columnName,
			    String columnLabel,
			    Boolean isVisible,
			    Integer displayOrder,
			    Boolean allowSorting,
			    Boolean allowFiltering,
			    Boolean allowGrouping,
			    Boolean showHeaderFilter,
			    Boolean allowEditing,
			    Boolean columnTemplate,
			    String templatedata,
			    Boolean rowFilter
			) {
			    this.id = id;
			    this.schemaName = schemaName;
			    this.tableName = tableName;
			    this.displayName = displayName;
			    this.isActive = isActive;
			    this.pagination = pagination;
			    this.enableSearch = enableSearch;
			    this.cid = cid;
			    this.columnName = columnName;
			    this.columnLabel = columnLabel;
			    this.isVisible = isVisible;
			    this.displayOrder = displayOrder;
			    this.allowSorting = allowSorting;
			    this.allowFiltering = allowFiltering;
			    this.allowGrouping = allowGrouping;
			    this.showHeaderFilter = showHeaderFilter;
			    this.allowEditing = allowEditing;
			    this.columnTemplate = columnTemplate;
			    this.templatedata = templatedata;
			    this.rowFilter=rowFilter;
			}





		public Boolean getPagination() {
			return pagination;
		}





		public void setPagination(Boolean pagination) {
			this.pagination = pagination;
		}





		public Boolean getEnableSearch() {
			return enableSearch;
		}





		public void setEnableSearch(Boolean enableSearch) {
			this.enableSearch = enableSearch;
		}





		public Long getid() {
			return id;
		}

		public void setTid(Long id) {
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

		public Long getCid() {
			return cid;
		}

		public void setCid(Long cid) {
			this.cid = cid;
		}

		

		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		public String getColumnLabel() {
			return columnLabel;
		}

		public void setColumnLabel(String columnLabel) {
			this.columnLabel = columnLabel;
		}

		public Boolean getIsVisible() {
			return isVisible;
		}

		public void setIsVisible(Boolean isVisible) {
			this.isVisible = isVisible;
		}

		public Integer getDisplayOrder() {
			return displayOrder;
		}

		public void setDisplayOrder(Integer displayOrder) {
			this.displayOrder = displayOrder;
		}



		public Long getId() {
			return id;
		}



		public void setId(Long id) {
			this.id = id;
		}



		public Boolean getAllowSorting() {
			return allowSorting;
		}



		public void setAllowSorting(Boolean allowSorting) {
			this.allowSorting = allowSorting;
		}



		public Boolean getAllowFiltering() {
			return allowFiltering;
		}



		public void setAllowFiltering(Boolean allowFiltering) {
			this.allowFiltering = allowFiltering;
		}



		public Boolean getAllowGrouping() {
			return allowGrouping;
		}



		public void setAllowGrouping(Boolean allowGrouping) {
			this.allowGrouping = allowGrouping;
		}



		public Boolean getShowHeaderFilter() {
			return showHeaderFilter;
		}



		public void setShowHeaderFilter(Boolean showHeaderFilter) {
			this.showHeaderFilter = showHeaderFilter;
		}



		public Boolean getAllowEditing() {
			return allowEditing;
		}



		public void setAllowEditing(Boolean allowEditing) {
			this.allowEditing = allowEditing;
		}





		public Boolean getColumnTemplate() {
			return columnTemplate;
		}





		public void setColumnTemplate(Boolean columnTemplate) {
			this.columnTemplate = columnTemplate;
		}





		public String getTemplatedata() {
			return templatedata;
		}





		public void setTemplatedata(String templatedata) {
			this.templatedata = templatedata;
		}





		public Boolean getRowFilter() {
			return rowFilter;
		}





		public void setRowFilter(Boolean rowFilter) {
			this.rowFilter = rowFilter;
		}
	    
	    
	    

}
