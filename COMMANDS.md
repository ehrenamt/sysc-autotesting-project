# Commands

Commands for manual testing.


To run all JUnit tests
```shell
.\gradlew.bat test
```

To run all JUnit tests and generate a Jacoco coverage report (HTML) 
```shell
.\gradlew.bat test jacocoTestReport
```

The JaCoCo test report will be located at ```/build/reports/jacoco/test/html/index.html```.

To run tests with the coverage report on pitest mutations:

```shell
.\gradlew.bat test jacocoTestReport pitest
```


To run combinatorial tests only along with a coverage report

```shell
.\gradlew.bat test --tests 'sysc.QuadraticTest.testComplex'
```

To run multiple tests

```shell
.\gradlew.bat test --tests "sysc.QuadraticTest.testQuadraticMainParam" --tests "sysc.QuadraticTest.testQuadraticCombinatorialParameterized"
```

### Other Commands

Check to ensure the project compiles without tests.

```shell
gradlew.bat --refresh-dependencies compileTestJava
```
