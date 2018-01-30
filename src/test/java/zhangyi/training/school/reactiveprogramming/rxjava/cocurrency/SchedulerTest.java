package zhangyi.training.school.reactiveprogramming.rxjava.cocurrency;


import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import static zhangyi.training.school.reactiveprogramming.rxjava.cocurrency.Logging.log;

public class SchedulerTest {
    @Test
    public void should_not_use_scheduler() {
        log("Starting");
        final Observable<String> obs = simple();
        log("Created");
        final Observable<String> obs2 = obs
                .map(x -> x)
                .filter(x -> true);
        log("Transformed");
        obs2.subscribe(
                x -> log("Got" + x),
                Throwable::printStackTrace,
                () -> log("Completed")
        );
        log("Exiting");
    }

    @Test
    public void should_subscribe_on_scheduler() {
        log("Starting");
        final Observable<String> obs = simple();
        log("Created");
        final Observable<String> obs2 = obs
                .map(x -> x)
                .filter(x -> true);
        log("Transformed");
        obs2
                .subscribeOn(Schedulers.io())
                .subscribe(
                x -> log("Got" + x),
                Throwable::printStackTrace,
                () -> log("Completed")
        );
        log("Exiting");
    }
    
    
    private Observable<String> simple() {
        return Observable.create(emitter -> {
            log("Subscribed");
            emitter.onNext("A");
            emitter.onNext("B");
            emitter.onComplete();
        });
    }
}
