package zhangyi.training.school.reactiveprogramming.rxjava;

import io.reactivex.Observable;
import org.junit.Test;

import static zhangyi.training.school.reactiveprogramming.rxjava.utils.Logger.log;

public class ObservableTest {
    @Test
    public void should_execute_synchronized() {
        Observable.create(s -> {
            s.onNext(1);
            s.onNext(2);
            s.onNext(3);
            s.onComplete();
        }).map(i -> "Number " + i).subscribe(System.out::println);
    }

    @Test
    public void two_streams_should_be_running_concurrently() {
        Observable<String> a = Observable.create(s -> {
            new Thread(() -> {
                s.onNext("first");
                s.onNext("second");
                s.onComplete();
            }).start();
        });

        Observable<String> b = Observable.create(s -> {
            new Thread(() -> {
                s.onNext("three");
                s.onNext("four");
                s.onComplete();
            }).start();
        });

        Observable<String> c = Observable.merge(a, b);
        c.subscribe(System.out::println);
    }

    @Test
    public void should_be_lazy() {
        Observable<Integer> ints = Observable.create(s -> {
            log("Create");
            s.onNext(1);
            s.onNext(2);
            s.onNext(3);
            s.onComplete();
            log("Completed");
        });

        log("Starting");
        ints.subscribe(i -> log(i));
        log("Exit");
    }
}
