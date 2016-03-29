import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Vector;

public class Main {

	public static void main(String[] args) {
		// Read matrix from input file
		BigDecimal[][] initial = readMatrix(args[0]);

		int targetPower = Integer.parseInt(args[2]);
		int size = initial.length;
		
		// Calculate nearest (ceiling) power of 2 based on target
		int upperBound = (int)Math.pow(2, Math.ceil(Math.log(targetPower)/Math.log(2)));

		BigDecimal[][][] matrixMap = new BigDecimal[upperBound][size][size];
		matrixMap[1] = initial;

		fillTable(upperBound, matrixMap[1], matrixMap);

		binarySearch(upperBound/2, upperBound, targetPower, matrixMap);
		output(args[1], matrixMap[targetPower]);
	}

	public static void binarySearch(int begin, int end, int target, BigDecimal[][][] matrixMap){
		int middle = (begin + end)/2;

		// Calculate middle matrix
		BigDecimal[][] halfwayMatrix = multiplyMatrices(matrixMap[begin], matrixMap[(end-begin)/2]);
		matrixMap[middle] = halfwayMatrix;
		
		if(middle == target) // Target found, return
			return;
		
		else if(middle < target) // Go for upper half
			binarySearch(middle, end, target, matrixMap);
		
		else if(middle > target) // Go for lower half
			binarySearch(begin, middle, target, matrixMap);
		
		return;
	}

	public static void fillTable(int targetPower, BigDecimal[][] matrix, BigDecimal[][][] matrixMap){
		// Fill powers of 2 in table. Pre-computation phase.
		for(int power = 2; power < targetPower; power*=2){
			BigDecimal[][] next = multiplyMatrices(matrix, matrix);
			matrixMap[power] = next;
			matrix = next;
		}

	}

	// Standard matrix multiplication, can be improved by Strassen's algorithm

	public static BigDecimal[][] multiplyMatrices(BigDecimal[][] m1, BigDecimal[][] m2){
		
		int size = m1.length;
		
		BigDecimal[][] next = new BigDecimal[size][size];
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				BigDecimal current = new BigDecimal(0);
				for(int k = 0; k < size; k++)
					current.add(m1[i][k].multiply(m2[k][j]));

				next[i][j] = current;
			}
		}
		return next;
	}


	// For debug purposes only. Prints entire matrix onto standard output.
	public static void printMatrix(BigDecimal[][] m){
		for(int i = 0; i < m.length; i++){
			for(int j = 0; j < m[0].length; j++)
				System.out.print(m[i][j]+"\t");

			System.out.println();
		}
		System.out.println();
	}


	// Read and verify the input file. Throws exception if not a square matrix.
	public static BigDecimal[][] readMatrix(String filePath){
		try {

			FileReader fileReader = new FileReader(filePath);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = "";
			Vector<Vector<BigDecimal>> initialVector = new Vector<>();
			int finalDimension = 0;
			boolean dimensionSet = false;
			while((line = bufferedReader.readLine()) != null){
				Vector<BigDecimal> thisRow = new Vector<>();
				int thisDimension = 0;
				for(String s: line.split(" ")){
					thisRow.add(new BigDecimal(s));
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
			BigDecimal[][] initialMatrix = new BigDecimal[size][size];

			for(Vector<BigDecimal> v: initialVector){
				for(BigDecimal l: v)
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
	public static void output(String filePath, BigDecimal[][] m){
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
