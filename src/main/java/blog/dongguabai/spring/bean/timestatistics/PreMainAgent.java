package blog.dongguabai.spring.bean.timestatistics;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

/**
 * @author dongguabai
 * @date 2023-10-22 14:19
 */
public class PreMainAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        new AgentBuilder.Default().type(ElementMatchers.hasSuperType(ElementMatchers.named("org.springframework.beans.factory.support.InstantiationStrategy")))
                .transform((builder, typeDescription, classLoader, javaModule) -> builder
                        .method(ElementMatchers.named("instantiate"))
                        .intercept(MethodDelegation.to(BeanLifeInterceptor.class)))
                .type(ElementMatchers.hasSuperType(ElementMatchers.named("org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter"))).
                transform((builder, typeDescription, classLoader, javaModule) -> builder
                        .method(ElementMatchers.named("postProcessBeforeInitialization").or(ElementMatchers.named("postProcessAfterInitialization")))
                        .intercept(MethodDelegation.to(BeanLifeInterceptor.class)))
                .installOn(inst);
        new AgentBuilder.Default().type(ElementMatchers.named(agentArgs.split("#")[0]))
                .transform((builder, typeDescription, classLoader, javaModule) -> builder
                        .method(ElementMatchers.named(agentArgs.split("#")[1]))
                        .intercept(MethodDelegation.to(LogInterceptor.class)))
                .installOn(inst);
    }
}