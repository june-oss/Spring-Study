package springbook.learningtest.junit;

import org.assertj.core.error.ShouldBeAtSameInstant;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig
public class JUnitTest {
    @Autowired
    ApplicationContext context;

//    static JUnitTest testObject;
    static Set<JUnitTest> testObject = new HashSet<>();
    static ApplicationContext contextObject = null;

    @Test
    public void test1(){
//        Assertions.assertNotEquals(this, is(sameInstance(testObject)));
//        testObject = this;
        Assertions.assertNotEquals(testObject, hasItem(this));
        testObject.add(this);

        Assertions.assertEquals(contextObject == null || contextObject == this.context, true);
        contextObject = this.context;
    }

    @Test
    public void test2(){
//        Assertions.assertNotEquals(this, is(sameInstance(testObject)));
//        testObject = this;
        Assertions.assertNotEquals(testObject, hasItem(this));
        testObject.add(this);

        Assertions.assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }

    @Test
    public void test3(){
//        Assertions.assertNotEquals(this, is(sameInstance(testObject)));
//        testObject = this;
        Assertions.assertNotEquals(testObject, hasItem(this));
        testObject.add(this);
        MatcherAssert.assertThat(contextObject, either(nullValue()).or(is(this.context)));
        contextObject = this.context;
    }
}
