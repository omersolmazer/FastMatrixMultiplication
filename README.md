# Fast Matrix Multiplication
Fast matrix multiplication using binary method. Written in Java.

### Instructions for compiling and running:

#### 1. Compiling:
```
javac DimensionMismatch.java Main.java 
```

#### 2. Running:

Program expects 3 arguments: input file path, output file path, and the power of matrix to be calculated.

(X: the power of matrix to calculate)

```
java Main input.txt output.txt X
```

Input file has the following format:

Every line is a row,

Numbers in every row are separated by the space character,

Numbers can be anything supported by BigDecimal class of Java Math library ( https://docs.oracle.com/javase/7/docs/api/java/math/BigDecimal.html )

#### 3. Output:

If there is a problem in matrix dimensions, the program will produce an error.

When program finishes, it creates a file with same format as input in the current directory.

