package zhangyi.training.school.rp.rxjava;

import io.reactivex.Flowable;

public class DemoMain {
    public static final void main(String[] args) throws InterruptedException {
        Flowable.just("hello world").subscribe(System.out::println);
    }
}
