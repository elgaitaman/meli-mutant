package com.ezuazu.mutant.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezuazu.mutant.dto.DnaInput;
import com.ezuazu.mutant.exception.InvalidDnaException;
import com.ezuazu.mutant.service.MutantService;
import com.ezuazu.mutant.writer.AsyncDatabaseWriter;

@Service
public class MutantServiceOnePassImpl implements MutantService {

	private static final int NITROSEC_QTY_B0 = 3;

	private static final String ALLOWED_NITRO_BASES = "[ATCG]+";

	@Autowired
	public AsyncDatabaseWriter asyncWriter;

	/*
	 * Metodo que analiza y agrega en la qeue un DNA para ser guardado
	 */
	public boolean isMutant(DnaInput dnaInput) throws Exception {
		boolean isMutant = isMutant(dnaInput.getDna());
		asyncWriter.scheduleWrite(dnaInput.getDna(), isMutant);
		return isMutant;
	}

	/*
	 * Metodo que analiza un DNA
	 */
	public boolean isMutant(String[] dna) throws Exception {
		validateDna(dna);

		boolean resultIsMutant = false;

		int verticalSecQty = 0;
		int horizontalSecQty = 0;
		int diagonalSecQty = 0;

		int matrixLength = dna.length;

		String[] previousRow = null;
		int[] verticalControl = new int[matrixLength];
		int[] diagonalControl = new int[matrixLength];

		int rowIndex = 0;
		for (String row : dna) {
			validateDnaRow(matrixLength, row);

			String previousNitroBase = null;
			int horizontalQty = 0;
			int[] previousDiagonal = diagonalControl.clone();
			int columnIndex = 0;

			for (String nitroBase : row.split("")) {
				// Horizontal
				if (columnIndex > 0) {
					if (previousNitroBase.equals(nitroBase)) {
						horizontalQty++;
						if (columnIndex == (matrixLength - 1) && horizontalQty == NITROSEC_QTY_B0) {
							horizontalSecQty++;
						}
					} else {
						if (horizontalQty == NITROSEC_QTY_B0) {
							horizontalSecQty++;
						}
						horizontalQty = 0;
					}
				}
				previousNitroBase = nitroBase;

				if (rowIndex > 0) {
					// Vertical
					if (nitroBase.equals(previousRow[columnIndex])) {
						verticalControl[columnIndex]++;
					} else {
						if (verticalControl[columnIndex] == 3) {
							verticalSecQty++;
						}
						verticalControl[columnIndex] = 0;
					}
					if (rowIndex == (matrixLength - 1)) {
						if (verticalControl[columnIndex] == 3) {
							verticalSecQty++;
						}
						verticalControl[columnIndex] = 0;
					}

					// Diagonal
					if (columnIndex > 0) {
						if (nitroBase.equals(previousRow[columnIndex - 1])) {
							diagonalControl[columnIndex] = previousDiagonal[columnIndex - 1] + 1;
						} else {
							if (previousDiagonal[columnIndex - 1] == 3) {
								diagonalSecQty++;
							}
							diagonalControl[columnIndex] = 0;
						}
						if (columnIndex == (matrixLength - 1)) {
							if (diagonalControl[columnIndex] == 3) {
								diagonalSecQty++;
							}
							diagonalControl[columnIndex] = 0;
						}
					}
				}
				columnIndex++;
			}

			if ((verticalSecQty + horizontalSecQty + diagonalSecQty) > 1) {
				resultIsMutant = true;
				break;
			}

			rowIndex++;
			previousRow = row.split("");
		}

		return resultIsMutant;
	}

	private void validateDna(String[] dna) throws Exception {
		Objects.requireNonNull(dna);

		if (dna.length == 0) {
			throw new InvalidDnaException("Invalid length, DNA could not be zero");
		}

		if (dna.length == 0 || dna.length != dna[0].length()) {
			throw new InvalidDnaException("Invalid format, DNA must be squard");
		}

	}

	private void validateDnaRow(int length, String dna) throws Exception {
		Objects.requireNonNull(dna);

		if (dna.length() != length) {
			throw new InvalidDnaException("Invalid DNA row length");
		}

		if (!dna.matches(ALLOWED_NITRO_BASES)) {
			throw new InvalidDnaException("Invalid DNA nitrogenous base");
		}
	}

}
