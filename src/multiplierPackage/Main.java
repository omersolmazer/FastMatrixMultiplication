package multiplierPackage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;


public class Main {

	public static void main(String[] args) {
		// Read matrix from input file
		long[][] initial = readMatrix(args[0]);

		int targetPower = Integer.parseInt(args[2]);
		int size = initial.length;
		
		// Calculate nearest (ceiling) power of 2 based on target
		int upperBound = (int)Math.pow(2, Math.ceil(Math.log(targetPower)/Math.log(2)));

		long[][][] matrixMap = new long[upperBound][size][size];
		matrixMap[1] = initial;

		fillTable(upperBound, matrixMap[1], matrixMap);

		binarySearch(upperBound/2, upperBound, targetPower, matrixMap);
		output(args[1], matrixMap[targetPower]);
	}

	public static void binarySearch(int begin, int end, int target, long[][][] matrixMap){
		int middle = (begin + end)/2;

		// Calculate middle matrix
		long[][] halfwayMatrix = multiplyMatrices(matrixMap[begin], matrixMap[(end-begin)/2]);
		matrixMap[middle] = halfwayMatrix;
		
		if(middle == target) // Target found, return
			return;
		
		else if(middle < target) // Go for upper half
			binarySearch(middle, end, target, matrixMap);
		
		else if(middle > target) // Go for lower half
			binarySearch(begin, middle, target, matrixMap);
		
		return;
	}

	public static void fillTable(int targetPower, long[][] matrix, long[][][] matrixMap){
		// Fill powers of 2 in table. Pre-computation phase.
		for(int power = 2; power < targetPower; power*=2){
			long[][] next = multiplyMatrices(matrix, matrix);
			matrixMap[power] = next;
			matrix = next;
		}

	}

	// Standard matrix multiplication, can be improved by Strassen's algorithm

	public static long[][] multiplyMatrices(long[][] m1, long[][] m2){
		
		int size = m1.length;
		
		long[][] next = new long[size][size];
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				long current = 0;
				for(int k = 0; k < size; k++)
					current += m1[i][k] * m2[k][j];

				next[i][j] = current;
			}
		}
		return next;
	}


	// For debug purposes only. Prints entire matrix onto standard output.
	public static void printMatrix(long[][] m){
		for(int i = 0; i < m.length; i++){
			for(int j = 0; j < m[0].length; j++)
				System.out.print(m[i][j]+"\t");

			System.out.println();
		}
		System.out.println();
	}


	// Read and verify the input file. Throws exception if not a square matrix.
	public static long[][] readMatrix(String filePath){
		try {

			FileReader fileReader = new FileReader(filePath);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = "";
			Vector<Vector<Long>> initialVector = new Vector<>();
			int finalDimension = 0;
			boolean dimensionSet = false;
			while((line = bufferedReader.readLine()) != null){
				Vector<Long> thisRow = new Vector<>();
				int thisDimension = 0;
				for(String s: line.split(" ")){
					thisRow.add(Long.parseLong(s));
					thisDimension++;
					if(!dimensionSet)
						finalDimension++;
				}
				if(dimensionSet && thisDimension != finalDimension){
					bufferedReader.close();
					fileReader.close();
					throw new DimensionMismatch();
				}
				dimensionSet = true;

				initialVector.add(thisRow);
			}
			int size = initialVector.size(), row = 0, column = 0;
			long[][] initialMatrix = new long[size][size];

			for(Vector<Long> v: initialVector){
				for(long l: v)
					initialMatrix[row][column++] = l;

				row++;
				column = 0;
			}

			bufferedReader.close();
			fileReader.close();

			return initialMatrix;

		}

		catch (DimensionMismatch d){
			System.out.println("Matrix dimensions do not match!");
			System.exit(-1);
		}

		catch (FileNotFoundException e) {
			System.out.println("File " + filePath + " not found!");
			System.exit(-1);
		}

		catch (IOException e) {
			System.out.println("IO exception while reading file " + filePath);
			System.exit(-1);
		}
		return null;
	}


	// Output the matrix into a file.
	public static void output(String filePath, long[][] m){
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(filePath);
			for(int i = 0; i < m.length; i++){
				for(int j = 0; j < m[0].length; j++)
					fileWriter.append(m[i][j]+"\t");

				fileWriter.append("\n");
			}
			fileWriter.flush();
			fileWriter.close();
		}
		catch (IOException e) {
			System.out.println("File writer error!");
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
