package io.github.dongguabai.spring.bean.timestatistics;

import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * @author dongguabai
 * @date 2023-10-22 14:31
 */
public class LogInterceptor {

    public static HashMap<String, Log> logs = new HashMap<>();

    @RuntimeType
    public static Object intercept(@SuperCall Callable<?> callable) throws Exception {
        try {
            return callable.call();
        } finally {
            System.out.println("======================");
            System.out.println("BEANS TIME STATISTICS:");
            logs.values().stream().map(a -> {
                TimeDate timeDate = a.getTimeDate();
                timeDate.setInitializeTime(a.getEndInitializationTime() - a.getStartInitializationTime());
                timeDate.setTotalTime(timeDate.getInitializeTime() + timeDate.getInstantiateTime());
                return timeDate;
            }).sorted((o1, o2) -> (int) (o2.getTotalTime() - o1.getTotalTime())).forEach(System.out::println);
            System.out.println("======================");
        }
    }
}