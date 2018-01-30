package zhangyi.training.school.rp.utils;

public class Logging {
    public static void log(Object label) {
        System.out.println(
                        System.currentTimeMillis() + "\t|" +
                        Thread.currentThread().getName() + "\t|" +
                        label
        );
    }
}
