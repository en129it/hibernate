package com.ddv.test.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionId;

@Entity
@Table(name="TRANSACTION")
public class Transaction {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	public Long id;
	
	@Column(name="TYPE")
	public String type;

	@ElementCollection
	@JoinTable(name="TRANSACTION_EXT", joinColumns=@JoinColumn(name= "TRANSACTION_ID"))
	@Column(name="LONG_TYPE")
	public List<String> longTypes;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getLongTypes() {
		return longTypes;
	}

	public void setLongTypes(List<String> longTypes) {
		this.longTypes = longTypes;
	}
	
	
}
