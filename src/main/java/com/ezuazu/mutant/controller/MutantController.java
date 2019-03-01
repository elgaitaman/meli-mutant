package com.ezuazu.mutant.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ezuazu.mutant.dto.DnaInput;
import com.ezuazu.mutant.dto.DnaStatReport;
import com.ezuazu.mutant.exception.InvalidDnaException;
import com.ezuazu.mutant.service.StatService;
import com.ezuazu.mutant.service.impl.MutantServiceOnePassImpl;

@RestController
@RequestMapping("/api/v1")
public class MutantController {

	final static Logger log = LoggerFactory.getLogger(MutantController.class);

	@Autowired
	private MutantServiceOnePassImpl mutantService;

	@Autowired
	private StatService statService;

	@RequestMapping(path = { "/mutant", "/mutant/" }, method = RequestMethod.POST)
	public ResponseEntity<?> isMutant(@RequestBody DnaInput dnaRecord) {
		try {
			if (mutantService.isMutant(dnaRecord)) {
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		} catch (InvalidDnaException e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<String>("ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(path = { "/stats", "/stats/" }, method = RequestMethod.GET)
	public ResponseEntity<?> getStats() {
		try {
			return new ResponseEntity<DnaStatReport>(statService.getStats(), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<String>("ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(path = { "/stats", "/stats/" }, method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteStats() {
		try {
			statService.resetStats();
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<String>("ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
