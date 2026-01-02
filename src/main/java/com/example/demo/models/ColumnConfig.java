package com.example.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "column_config")
public class ColumnConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "table_id")
	private TableConfig table;

	private String columnName;
	private String columnLabel;
	private Boolean isVisible;
	private Integer displayOrder;

	private Boolean allowSorting;
	private Boolean allowFiltering;
	private Boolean allowGrouping;
	private Boolean showHeaderFilter;
	private Boolean allowEditing;
	 private Boolean rowFilter;
	private Boolean columnTemplate;
	
	@Column(columnDefinition = "LONGTEXT")
	private String templatedata;
	
	public ColumnConfig() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public ColumnConfig(Long id, TableConfig table, String columnName, String columnLabel, Boolean isVisible,
			Integer displayOrder, Boolean allowSorting, Boolean allowFiltering, Boolean allowGrouping,
			Boolean showHeaderFilter, Boolean allowEditing, Boolean columnTemplate, String templatedata,Boolean rowFilter) {
		super();
		this.id = id;
		this.table = table;
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







	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TableConfig getTable() {
		return table;
	}

	public void setTable(TableConfig table) {
		this.table = table;
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
