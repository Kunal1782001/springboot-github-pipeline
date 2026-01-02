package com.example.demo.dto;

public class ColumnOrderUpdateDto {
	
	 private Long id;
	 private Integer displayOrder;
	 
	 public ColumnOrderUpdateDto() {
		super();
		// TODO Auto-generated constructor stub
	 }
	 public ColumnOrderUpdateDto(Long id, Integer displayOrder) {
		super();
		this.id = id;
		this.displayOrder = displayOrder;
	 }
	 public Long getId() {
		 return id;
	 }
	 public void setId(Long id) {
		 this.id = id;
	 }
	 public Integer getDisplayOrder() {
		 return displayOrder;
	 }
	 public void setDisplayOrder(Integer displayOrder) {
		 this.displayOrder = displayOrder;
	 }

	 
}
