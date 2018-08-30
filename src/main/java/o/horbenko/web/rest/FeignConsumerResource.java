package o.horbenko.web.rest;


import com.codahale.metrics.annotation.Timed;
import com.sun.org.apache.bcel.internal.generic.FRETURN;
import o.horbenko.feign.FeignProducerClient;
import o.horbenko.feign.FooDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consume")
public class FeignConsumerResource {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final FeignProducerClient feignProducerClient;

    public FeignConsumerResource(FeignProducerClient feignProducerClient) {
        this.feignProducerClient = feignProducerClient;
    }


    @GetMapping("/consume-without-exception")
    @Timed
    public FooDTO produceSomeEntityWitoutException() {

        FooDTO result = feignProducerClient.produceSomeEntityWitoutException();
        log.debug(" --- FEIGN-PRODUCER RETURNED {}", result);

        return result;
    }

    @GetMapping("/consume-custom-exception")
    @Timed
    public FooDTO produceCustomRuntimeException() {
        try {

            return feignProducerClient.produceCustomException();

        } catch (CustomRuntimeException e) {
            log.error(" --- produceCustomRuntimeException catch CustomRuntimeException ={} ", e);
            log.error(" --- message {}", e.getMessage());
            return null;
        }
    }

    @GetMapping("/consume-not-registered-err")
    @Timed
    public FooDTO produceRealCustomException() {
        return feignProducerClient.produceNotAuthorizedException();
    }


}
