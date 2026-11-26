# Comparing Combinatorial, Categorical, and Metamorphic Test Coverages

This project uses JDK 17, Gradle 8.14, and Python. Required packages and versions are
defined in ```build.gradle```.

## Project Structure

The two case studies are placed in ```src/main/java/sysc```.

My test implementations are located in ```src/test/java/sysc``` and the resource files
used by the test files are located in ```src/test/resources```.

The test case generator is implemented in generator.py at the project root. The generator
is run as a separate step; first run generator.py to generate the test cases, then
run a test suite and view the reports to measure how well the generated cases achieved.

### Raw Data

The generated test cases are all placed in ```src/test/resources``` as JSON
files.

### Implemented Tests

Both test files (```DateHelperTest.java``` and ```QuadraticTest.java```)
contain metamorphic, combinatorial, and category-partition tests. These tests
are grouped within each file and the prefix of the method name indicates the
test type. Combinatorial and category-partition tests are parameterized; these
tests read from generated test cases in JSON files in ```src/test/resources```.

# Commands

## Test Case Generation

The test case generator is implemented in Python. Generate all test cases for
all tests by running

```shell
python generator.py
```

## Tests

The JaCoCo test report will be located at ```/build/reports/jacoco/test/html/index.html```.

To run tests with the coverage report on pitest mutations:

```shell
.\gradlew test jacocoTestReport pitest
```

### Metamorphic Test Suite

```shell
.\gradlew test --tests "sysc.DateHelperTest.testMetamorphic*"
```

### Combinatorial Test Suite

```shell
.\gradlew test --tests "sysc.DateHelperTest.testCombinatorial*"
```

## Tools

- Junit 5.11
- Junit Pioneer 2.3
- JaCoCo
- Pitest 1.19
- Sympy
