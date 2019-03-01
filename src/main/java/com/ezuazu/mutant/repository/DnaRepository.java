package com.ezuazu.mutant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ezuazu.mutant.enums.DnaType;
import com.ezuazu.mutant.model.DnaRecord;

public interface DnaRepository extends MongoRepository<DnaRecord, String> {

	public Long countByType(DnaType type);
}
