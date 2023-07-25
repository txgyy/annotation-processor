package xin.yukino.annotationprocessor.apolloconfigmultichain;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import xin.yukino.annotationprocessor.apolloconfigmultichain.annotation.MultiChainApolloConfigAnnotation;

import java.math.BigDecimal;

@Getter
public class ApolloConfig {

    @MultiChainApolloConfigAnnotation
    @Value("${abc:1}")
    private BigDecimal a;
}
