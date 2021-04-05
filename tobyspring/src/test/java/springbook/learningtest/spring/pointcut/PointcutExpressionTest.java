package springbook.learningtest.spring.pointcut;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import static org.junit.jupiter.api.Assertions.*;

public class PointcutExpressionTest {

    @Test
    public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(public int " +
                "springbook.learningtest.spring.pointcut.Target.minus(int,int) " +
                "throws java.lang.RuntimeException)");

        //Target.minus()
        assertTrue(pointcut.getClassFilter().matches(Target.class) &&
                pointcut.getMethodMatcher().matches(
                        Target.class.getMethod("minus", int.class, int.class),null)
                );

        //Target.plus()
        assertFalse(pointcut.getClassFilter().matches(Target.class) &&
                pointcut.getMethodMatcher().matches(
                        Target.class.getMethod("plus", int.class, int.class),null)
        );

        //Bean.method()
        assertFalse(pointcut.getClassFilter().matches(Target.class) &&
                pointcut.getMethodMatcher().matches(
                        Target.class.getMethod("method"),null)
        );
    }
}