package zhangyi.training.school.reactiveprogramming.rxjava.cocurrency;

public class Logging {
    public static void log(Object label) {
        System.out.println(
                        System.currentTimeMillis() + "\t|" +
                        Thread.currentThread().getName() + "\t|" +
                        label
        );
    }
}
