package com.ezuazu.mutant.service;

import org.springframework.stereotype.Service;

import com.ezuazu.mutant.dto.DnaInput;

@Service
public interface MutantService {

	public boolean isMutant(String[] dna) throws Exception;

	public boolean isMutant(DnaInput dnaInput) throws Exception;
}
