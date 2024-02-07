package io.github.dongguabai.spring.bean.timestatistics;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @author dongguabai
 * @date 2023-10-22 14:18
 */
public class BeanLifeInterceptor {

    @RuntimeType
    public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable, @Argument(1) String beaName, @AllArguments Object[] args, @This Object obj) throws Exception {
        long start = System.currentTimeMillis();
        try {
            return callable.call();
        } finally {
            long end = System.currentTimeMillis();
            if (Objects.equals(method.getName(), "instantiate")) {
                instantiateLog(beaName, end - start);
            }
            if (Objects.equals(method.getName(), "postProcessBeforeInitialization")) {
                beforeInitializationLog(beaName, start);
            }
            if (Objects.equals(method.getName(), "postProcessAfterInitialization")) {
                afterInitializationLog(beaName, end);
            }
        }
    }

    private synchronized static void instantiateLog(String beaName, long time) {
        Log log = LogInterceptor.logs.get(beaName);
        if (log == null) {
            log = new Log(beaName);
        }
        TimeDate timeDate = log.getTimeDate();
        timeDate.setBeanName(beaName);
        timeDate.setInstantiateTime(time);
        LogInterceptor.logs.put(beaName, log);
    }

    private synchronized static void beforeInitializationLog(String beaName, long time) {
        Log log = LogInterceptor.logs.get(beaName);
        if (log == null) {
            log = new Log(beaName);
        }
        log.setStartInitializationTime(Math.min(log.getStartInitializationTime() == 0L ? Long.MAX_VALUE : log.getStartInitializationTime(), time));
        LogInterceptor.logs.put(beaName, log);
    }

    private synchronized static void afterInitializationLog(String beaName, long time) {
        Log log = LogInterceptor.logs.get(beaName);
        if (log == null) {
            log = new Log(beaName);
        }
        log.setEndInitializationTime(Math.max(time, log.getEndInitializationTime()));
        LogInterceptor.logs.put(beaName, log);
    }
}