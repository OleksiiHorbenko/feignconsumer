package o.horbenko.aop.feign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import feign.FeignException;
import o.horbenko.reflection.ReflectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@SuppressWarnings("Duplicates")
@Aspect
@Component
public class FeignExceptionsAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    @Around("@annotation(o.horbenko.feign.FeignExceptionTranslapable)")
    public Object aroundFeignClient(ProceedingJoinPoint joinPoint) throws Throwable {
        try {

            return joinPoint.proceed();

        } catch (HystrixRuntimeException e) {
            throw getParsedExceptionOrOriginal(e);
        }
    }

    private RuntimeException getParsedExceptionOrOriginal(HystrixRuntimeException originalHystrixException) {

        Map<String, String> jsonMap = getJsonValuesMapFromCauseOrThrowOriginal(originalHystrixException);

        String exceptionTypeName = jsonMap.get("exceptionType");
        String exceptionMessage = jsonMap.get("exceptionMessage");

        if (null == exceptionTypeName)
            throw originalHystrixException;

        log.debug(" parsed exceptionTypeName {}", exceptionTypeName);
        log.debug(" parsed exceptionMessage {} ", exceptionMessage);

        return ReflectionUtils.instaniateRuntimeException(exceptionTypeName, exceptionMessage)
            .orElseThrow(() -> originalHystrixException);
    }

    private Map<String, String> getJsonValuesMapFromCauseOrThrowOriginal(HystrixRuntimeException originalHystrixException) {

        if (originalHystrixException.getCause() == null
            || !(originalHystrixException.getCause() instanceof FeignException)) {
            throw originalHystrixException;
        }

        try {

            String json = originalHystrixException
                .getCause()     // get FeignException
                .getMessage();  // get JSON of initial Problem

            // because Problem contains header meta info about status
            String jsonPayload = json.substring(json.indexOf("{"));

            return OBJECT_MAPPER.readValue(jsonPayload, new TypeReference<Map<String, String>>() {});

        } catch (IOException | RuntimeException e) {
            log.error(" IOException {}", e.getMessage());
            throw originalHystrixException;
        }
    }
}
