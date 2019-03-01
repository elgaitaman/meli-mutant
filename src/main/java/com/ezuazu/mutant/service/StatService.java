package com.ezuazu.mutant.service;

import org.springframework.stereotype.Service;

import com.ezuazu.mutant.dto.DnaStatReport;
import com.ezuazu.mutant.model.DnaRecord;

@Service
public interface StatService {

	public void resetStats() throws Exception;

	public void addDna(DnaRecord dnaRecord) throws Exception;

	public DnaStatReport getStats() throws Exception;

}
