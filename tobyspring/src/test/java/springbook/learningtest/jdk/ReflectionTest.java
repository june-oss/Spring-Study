package springbook.learningtest.jdk;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class ReflectionTest {

    @Test
    public void invokeMethod() throws  Exception{
        String name = "Spring";

        //length()
        assertEquals(name.length(), 6);

        Method lengthMethod = String.class.getMethod("length");
        assertEquals((Integer) lengthMethod.invoke(name), 6);

        // charAt()
        assertEquals(name.charAt(0), 'S');

        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertEquals((Character)charAtMethod.invoke(name,0), 'S');
    }
}
