package blog.dongguabai.spring.bean.timestatistics;

/**
 * @author dongguabai
 * @date 2023-10-24 10:43
 */
public class Log {

    private TimeDate timeDate;

    private long startInitializationTime;

    private long endInitializationTime;

    public TimeDate getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(TimeDate timeDate) {
        this.timeDate = timeDate;
    }

    public long getStartInitializationTime() {
        return startInitializationTime;
    }

    public void setStartInitializationTime(long startInitializationTime) {
        this.startInitializationTime = startInitializationTime;
    }

    public long getEndInitializationTime() {
        return endInitializationTime;
    }

    public void setEndInitializationTime(long endInitializationTime) {
        this.endInitializationTime = endInitializationTime;
    }

    public Log(String beanName) {
        this.timeDate = new TimeDate(beanName);
    }

    public Log() {
    }
}