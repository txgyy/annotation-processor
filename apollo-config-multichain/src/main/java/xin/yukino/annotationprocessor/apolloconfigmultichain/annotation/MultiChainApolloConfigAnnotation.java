package xin.yukino.annotationprocessor.apolloconfigmultichain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface MultiChainApolloConfigAnnotation {

    Class key() default Long.class;

    Class value() default BigDecimal.class;
}
