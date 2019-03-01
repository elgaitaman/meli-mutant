package com.ezuazu.mutant.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ezuazu.mutant.enums.DnaType;

@Document(collection = "dna_record")
public class DnaRecord {

	@Id
	private String id;

	@Indexed
	private DnaType type;

	private String[] dna;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getDna() {
		return dna;
	}

	public void setDna(String[] dna) {
		this.dna = dna;
	}

	public DnaType getType() {
		return type;
	}

	public void setType(DnaType type) {
		this.type = type;
	}

	public DnaRecord(DnaType type, String[] dna) {
		super();
		this.setType(type);
		this.dna = dna;
	}
	
}
