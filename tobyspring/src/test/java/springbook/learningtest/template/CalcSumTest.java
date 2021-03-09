package springbook.learningtest.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CalcSumTest {
    Calculator calculator;
    String numFilepath;

    @BeforeEach
    public  void setup(){
        this.calculator = new Calculator();
        this.numFilepath = getClass().getResource("/numbers.txt").getPath();
    }

    @Test
    public void sumOfNumbers() throws IOException{

        Assertions.assertEquals(calculator.calcSum(this.numFilepath), 10);
    }

    @Test
    public void multiplyOfNumbers() throws IOException{
        Assertions.assertEquals(calculator.calcMultiply(this.numFilepath), 24);
    }

    @Test
    public void concatenateStrings() throws IOException{
        Assertions.assertEquals(calculator.concatenate(numFilepath), "1234");
    }
}
