package zhangyi.training.school.rp.rxjava.observables.scheduler;


import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;
import zhangyi.training.school.rp.rxjava.utils.Logging;

public class SchedulerTest {
    @Test
    public void should_not_use_scheduler() {
        Logging.log("Starting");
        final Observable<String> obs = simple();
        Logging.log("Created");
        final Observable<String> obs2 = obs
                .map(x -> x)
                .filter(x -> true);
        Logging.log("Transformed");
        obs2.subscribe(
                x -> Logging.log("Got" + x),
                Throwable::printStackTrace,
                () -> Logging.log("Completed")
        );
        Logging.log("Exiting");
    }

    @Test
    public void should_subscribe_on_scheduler() {
        Logging.log("Starting");
        final Observable<String> obs = simple();
        Logging.log("Created");
        final Observable<String> obs2 = obs
                .map(x -> x)
                .filter(x -> true);
        Logging.log("Transformed");
        obs2
                .subscribeOn(Schedulers.io())
                .subscribe(
                x -> Logging.log("Got" + x),
                Throwable::printStackTrace,
                () -> Logging.log("Completed")
        );
        Logging.log("Exiting");
    }
    
    
    private Observable<String> simple() {
        return Observable.create(emitter -> {
            Logging.log("Subscribed");
            emitter.onNext("A");
            emitter.onNext("B");
            emitter.onComplete();
        });
    }
}
