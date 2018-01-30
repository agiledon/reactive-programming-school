package zhangyi.training.school.rp.rxjava.observables.utils;

import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.observers.TestSubscriber;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;


/**
 * Delay operator will delay transformation of our Observable from lazy to eager the time that you specify
 */
public class ObservableDelayTest {

    /**
     * Using the delay operator we delay the creation of the pipeline from lazy to eager.
     * But once start emitting the delay operator does not affect the items emitted
     */
    @Test
    public void delayCreation() {
        long start = System.currentTimeMillis();
        Subscription subscription = Observable.just("hello reactive world")
                .delay(200, TimeUnit.MICROSECONDS)
                .subscribe(n -> System.out.println("time:" + (System.currentTimeMillis() - start)));
        new TestSubscriber((Observer) subscription).awaitTerminalEvent(1000, TimeUnit.MILLISECONDS);
    }

    /**
     * If we want to delay the every single item emitted in the pipeline we will need a hack,
     * one possible hack is use zip operator and combine every item emitted with an interval so every item emitted has to wait until interval emit the item.
     * Shall print
     * <p>
     * time:586
     * time:783
     * time:982
     */
    @Test
    public void delayWithZipAndInterval() {
        long start = System.currentTimeMillis();
        Subscription subscription =
                Observable.zip(Observable.from(Arrays.asList(1, 2, 3)), Observable.interval(200, TimeUnit.MILLISECONDS),
                               (i, t) -> i)
                        .subscribe(n -> System.out.println("time:" + (System.currentTimeMillis() - start)));
        new TestSubscriber((Observer) subscription).awaitTerminalEvent(3000, TimeUnit.MILLISECONDS);
    }

    /**
     * Another elegant solution it would be to create an observable with the list of items, and then use
     * concatMap to pass all items from the first observable to the second, then this second observable
     * can be created used delay operator afterwards.
     */
    @Test
    public void delayWithConcatMap() {
        Observable.from(Arrays.asList(1, 2, 3, 4, 5))
                .concatMap(s -> Observable.just(s).delay(100, TimeUnit.MILLISECONDS))
                .subscribe(n -> System.out.println(n + " just came..."),
                           e -> {
                           },
                           () -> System.out.println("Everybody came!"));
        new TestSubscriber().awaitTerminalEvent(1000, TimeUnit.MILLISECONDS);

    }

    /**
     * Another elegant solution it would be to create an observable with the list of items, and then use
     * concatMap to pass all items from the first observable to the second, then this second observable
     * can be created used delay operator afterwards.
     */
    @Test
    public void delayObservablesWithConcatMap() {
        Observable.from(Arrays.asList(Observable.just(1), Observable.just(2),Observable.just(3)))
                .concatMap(s -> s.delay(100, TimeUnit.MILLISECONDS))
                .subscribe(n -> System.out.println(n + " just came..."),
                        e -> {
                        },
                        () -> System.out.println("Everybody came!"));
        new TestSubscriber().awaitTerminalEvent(1000, TimeUnit.MILLISECONDS);

    }
}
