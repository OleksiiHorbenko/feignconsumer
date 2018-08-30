package o.horbenko.feign;

import o.horbenko.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.PutMapping;


@AuthorizedFeignClient(name = "feignproducer")
public interface FeignProducerClient {

    @PutMapping("/api/produce/produce-exception")
    @FeignExceptionTranslapable
    FooDTO produceException();

    @PutMapping("/api/produce/produce-custom-exception")
    @FeignExceptionTranslapable
    FooDTO produceCustomException();

    @PutMapping("/api/produce/produce-without-exception")
    @FeignExceptionTranslapable
    FooDTO produceSomeEntityWitoutException();

    @PutMapping("/api/produce/produce-another-exception")
    @FeignExceptionTranslapable
    FooDTO produceNotAuthorizedException();

}
