package com.ezuazu.mutant.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezuazu.mutant.dto.DnaStatReport;
import com.ezuazu.mutant.enums.DnaType;
import com.ezuazu.mutant.model.DnaRecord;
import com.ezuazu.mutant.repository.DnaRepository;
import com.ezuazu.mutant.service.StatService;

@Service
public class StatServiceImpl implements StatService {

	private final static Logger log = LoggerFactory.getLogger(StatServiceImpl.class);

	@Autowired
	private DnaRepository dnaRepository;

	
	@Override
	public void addDna(DnaRecord dnaRecord) throws Exception {
		dnaRepository.save(dnaRecord);
	}

	@Override
	public DnaStatReport getStats() throws Exception {
		DnaStatReport result = new DnaStatReport();
		result.setCountHumanDna(dnaRepository.countByType(DnaType.HUMAN));
		result.setCountMutantDna(dnaRepository.countByType(DnaType.MUTANT));
		return result;
	}

	@Override
	public void resetStats() throws Exception {
		log.info("Resetting statistics...");
		dnaRepository.deleteAll();
	}

}
