package io.github.dongguabai.spring.bean.timestatistics;

/**
 * @author dongguabai
 * @date 2023-10-22 14:39
 */
public class TimeDate {

    private String beanName;

    private long totalTime;

    private long instantiateTime;

    private long initializeTime;

    public TimeDate(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public String toString() {
        return "【" +
                beanName + "->" +
                " totalTime=" + totalTime +
                ", instantiateTime=" + instantiateTime +
                ", initializeTime=" + initializeTime +
                '】';
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getInstantiateTime() {
        return instantiateTime;
    }

    public void setInstantiateTime(long instantiateTime) {
        this.instantiateTime = instantiateTime;
    }

    public long getInitializeTime() {
        return initializeTime;
    }

    public void setInitializeTime(long initializeTime) {
        this.initializeTime = initializeTime;
    }
}