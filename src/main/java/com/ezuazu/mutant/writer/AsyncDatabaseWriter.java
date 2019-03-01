package com.ezuazu.mutant.writer;

import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import com.ezuazu.mutant.enums.DnaType;
import com.ezuazu.mutant.model.DnaRecord;
import com.ezuazu.mutant.repository.DnaRepository;

@Component
public class AsyncDatabaseWriter {

	final static Logger log = LoggerFactory.getLogger(AsyncDatabaseWriter.class);

	private LinkedBlockingQueue<DnaRecord> queue = new LinkedBlockingQueue<>();

	private volatile boolean terminate = false;

	@Autowired
	private DnaRepository dnaRepository;

	@Async
	public void run() {
		while (!terminate) {
			try {
				DnaRecord dnaRecord = queue.take();
				if (dnaRecord.getId() == null) {
					dnaRecord.setId(DigestUtils.md5DigestAsHex(String.join("", dnaRecord.getDna()).getBytes()));
				}

				if (!dnaRepository.existsById(dnaRecord.getId())) {
					dnaRepository.save(dnaRecord);
					log.info("Saved DNA with id={}", dnaRecord.getId());
				} else {
					log.info("DNA already saved with id={}", dnaRecord.getId());
				}
			} catch (InterruptedException e) {
				log.error("Thread {} got interrupted while wating for the queue.", Thread.currentThread().getName());
				break;
			}
		}
	}

	public void scheduleWrite(String[] dna, boolean isMutant) {
		queue.add(new DnaRecord(isMutant ? DnaType.MUTANT : DnaType.HUMAN, dna));
	}

}
