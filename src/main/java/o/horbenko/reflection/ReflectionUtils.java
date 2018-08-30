package o.horbenko.reflection;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class ReflectionUtils {

    public static Object createInstance(String className) throws ClassNotFoundException {
        return BeanUtils.instantiateClass(
            Class.forName(className)
        );
    }

    public static Optional<RuntimeException> instaniateRuntimeException(String className, String ctorArgument) {
        try {

            String constructorStringArgument = ctorArgument == null ? "" : ctorArgument;

            Class<?> clazz = Class.forName(className);
            Constructor<?> ctor = clazz.getConstructor(String.class);

            Object maybeThrowable = ctor.newInstance(constructorStringArgument);

            if (maybeThrowable instanceof RuntimeException)
                return Optional.of((RuntimeException) maybeThrowable);
            else
                return Optional.empty();


        } catch (InstantiationException
            | IllegalAccessException
            | ClassNotFoundException
            | InvocationTargetException
            | NoSuchMethodException
            | RuntimeException e) {

            return Optional.empty();
        }
    }


}
