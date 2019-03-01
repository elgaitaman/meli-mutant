package com.ezuazu.mutant;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.ezuazu.mutant.controller.MutantController;
import com.ezuazu.mutant.dto.DnaInput;
import com.ezuazu.mutant.dto.DnaStatReport;
import com.ezuazu.mutant.exception.InvalidDnaException;
import com.ezuazu.mutant.service.StatService;
import com.ezuazu.mutant.service.impl.MutantServiceOnePassImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MutantApplicationTests {

	private final static Logger log = LoggerFactory.getLogger(MutantApplicationTests.class);

	@Autowired
	private MutantServiceOnePassImpl service;

	@Autowired
	private StatService statService;

	@Autowired
	private MutantController mutantController;

	private String[] dnaMutante = new String[] { "ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG" };

	private String[] dnaNoMutante = new String[] { "ATGCAA", "CTGTGC", "TTATGT", "AGAAGG", "ACCCTA", "TCACTG" };

	// No mutante 1 diagonal final
	private String[] dnaNoMutante1D = new String[] { "CTGCGA", "CACTGC", "TTGTGT", "AGAGTG", "ACCCGA", "TCACTG" };

	// Mutante con cadena en las tres direcciones
	private String[] dnaMutante3Directions = new String[] { "CTGCGA", "CACTGC", "TTGTGG", "AGAGTG", "ACCCGG", "TCGGGG" };

	// ADN con letras incorrectas
	private String[] dnaMalformed = new String[] { "PTGCGA", "CACTGC", "TTGTGG", "AGAGTG", "ACCCGG", "TCGGGG" };

	@Test
	public void testMutant() throws Exception {
		long start = System.currentTimeMillis();
		boolean res = service.isMutant(dnaMutante);
		log.info("Test Mutation in {} millis...", (System.currentTimeMillis() - start));
		log.info("DNA to test: {}", Arrays.toString(dnaMutante));
		log.info("DNA is {}", (res ? "mutante" : "No mutante"));

		Assert.assertTrue(res);
	}

	@Test
	public void testNoMutant() throws Exception {

		long start = System.currentTimeMillis();
		boolean res = service.isMutant(dnaNoMutante);
		log.info("Test Mutation in {} millis...", (System.currentTimeMillis() - start));
		log.info("DNA to test: {}", Arrays.toString(dnaNoMutante));
		log.info("DNA is {}", (res ? "mutante" : "No mutante"));

		Assert.assertFalse(res);
	}

	@Test
	public void testNoMutant1Diagonal() throws Exception {

		long start = System.currentTimeMillis();
		boolean res = service.isMutant(dnaNoMutante1D);
		log.info("Test Mutation in {} millis...", (System.currentTimeMillis() - start));
		log.info("DNA to test: {}", Arrays.toString(dnaNoMutante1D));
		log.info("DNA is {}", (res ? "mutante" : "No mutante"));

		Assert.assertFalse(res);
	}

	@Test
	public void testMutant3Directions() throws Exception {

		long start = System.currentTimeMillis();
		boolean res = service.isMutant(dnaMutante3Directions);
		log.info("Test Mutation in {} millis...", (System.currentTimeMillis() - start));
		log.info("DNA to test: {}", Arrays.toString(dnaMutante3Directions));
		log.info("DNA is {}", (res ? "mutante" : "No mutante"));

		Assert.assertTrue(res);
	}

	@Test
	public void testMalformedDna() throws Exception {

		long start = System.currentTimeMillis();
		try {
			service.isMutant(dnaMalformed);
			fail();
		} catch (InvalidDnaException e) {
			assertThat(e.getMessage(), containsString("Invalid DNA"));
		}
		log.info("Test Mutation in {} millis...", (System.currentTimeMillis() - start));
	}

	@Test
	public void testMutantRepeat() throws Exception {

		long start = System.currentTimeMillis();
		boolean res = false;
		for (int i = 0; i < 10000; i++) {
			res = service.isMutant(dnaMutante);
		}
		log.info("Test Mutation in {} millis...", (System.currentTimeMillis() - start));
		log.info("DNA to test: {}", Arrays.toString(dnaMutante));
		log.info("DNA is {}", (res ? "mutante" : "No mutante"));

		Assert.assertTrue(res);
	}

	@Test
	public void testStats() throws Exception {
		DnaStatReport stats = statService.getStats();
		log.info("Stat before deletion: {}", stats);
		statService.resetStats();
		stats = statService.getStats();
		log.info("Stat after deletion: {}", stats);

		long start = System.currentTimeMillis();
		boolean res = service.isMutant(new DnaInput(dnaMutante));
		log.info("Test Mutation in {} millis...", (System.currentTimeMillis() - start));
		log.info("DNA to test: {}", Arrays.toString(dnaMutante));
		log.info("DNA is {}", (res ? "mutante" : "No mutante"));

		Assert.assertTrue(res);

		Thread.sleep(500);

		stats = statService.getStats();
		log.info("Statistics : {}", stats);
		Assert.assertTrue(stats.getCountMutantDna() == 1);
		Assert.assertTrue(stats.getCountHumanDna() == 0);
	}

	@Test
	public void testMutantControllerResOK() {

		ResponseEntity<?> response = mutantController.isMutant(new DnaInput(dnaMutante));

		Assert.assertTrue(HttpStatus.OK.equals(response.getStatusCode()));
	}

	@Test
	public void testMutantControllerResForbidden() {

		ResponseEntity<?> response = mutantController.isMutant(new DnaInput(dnaNoMutante));

		Assert.assertTrue(HttpStatus.FORBIDDEN.equals(response.getStatusCode()));
	}

	@Test
	public void testMutantControllerStats() {

		ResponseEntity<?> response = mutantController.getStats();

		Assert.assertTrue(HttpStatus.OK.equals(response.getStatusCode()));
	}

	@Test
	public void testMutantControllerDeleteStats() {

		ResponseEntity<?> response = mutantController.deleteStats();

		Assert.assertTrue(HttpStatus.OK.equals(response.getStatusCode()));
	}

}
