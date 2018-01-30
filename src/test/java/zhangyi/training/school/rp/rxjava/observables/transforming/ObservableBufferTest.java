package zhangyi.training.school.rp.rxjava.observables.transforming;

import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.observers.TestSubscriber;

import java.util.concurrent.TimeUnit;


public class ObservableBufferTest {

    /**
     * In this example we use buffer(count) which will buffer items until it will take the count number set or end of items.
     * Shall print
     * Group size:3
     * Group size:2
     */
    @Test
    public void bufferCountObservable() {
        Integer[] numbers = {0, 1, 2, 3, 4};
        Observable.from(numbers)
                .buffer(3)
                .subscribe(list -> System.out.println("Group size:" + list.size()));

    }

    /**
     * This buffer will wait 50ms after emit the items, since the interval is every 100 ms, we should see a group size of 0, then the next time 1 and so on.
     *
     * @throws InterruptedException
     */
    @Test
    public void bufferTimeStampObservable() throws InterruptedException {
        Subscription subscription = Observable.interval(100, TimeUnit.MILLISECONDS)
                .buffer(50, TimeUnit.MILLISECONDS)
                .doOnNext(
                        list -> System.out.println("Group size " + list.size()))
                .subscribe();
        new TestSubscriber((Observer) subscription).awaitTerminalEvent(1000, TimeUnit.MILLISECONDS);


    }

    @Test
    public void stringBuffer() {
        Integer[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Observable.from(numbers)
                .map(number -> "uniqueKey=" + number)
                .buffer(4)
                .map(ns -> String.join("&", ns))
                .subscribe(System.out::println);

    }


}
