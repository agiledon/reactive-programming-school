package zhangyi.training.school.reactiveprogramming.rxjava;

import io.reactivex.Observable;
import org.junit.Test;
import org.omg.CORBA.Object;

import java.util.concurrent.TimeUnit;

import static io.reactivex.Observable.interval;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
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

    @Test
    public void should_use_map_and_filter() {
        Observable
                .just(8, 9, 10)
                .doOnNext(i -> System.out.println("A: " + i))
                .filter(i -> i % 3 > 0)
                .doOnNext(i -> System.out.println("B: " + i))
                .map(i -> "#" + i * 10)
                .doOnNext(s -> System.out.println("C: " + s))
                .filter(s -> s.length() < 4)
                .subscribe(s -> System.out.println("D: " + s));
    }

    @Test
    public void calculate_cartesian_product() {
        Observable<Integer> oneToEight = Observable.range(1, 8);
        Observable<String> ranks = oneToEight.map(i -> i.toString());
        Observable<String> files = oneToEight
                .map(x -> 'a' + x - 1)
                .map(ascii -> (char) ascii.intValue())
                .map(ch -> Character.toString(ch));

        Observable<String> square = files.flatMap(file -> ranks.map(rank -> file + rank));
        square.subscribe(System.out::println);
    }

    @Test
    public void using_zip() throws InterruptedException {
        Observable<Long> red = interval(10, MILLISECONDS);
        Observable<Long> green = interval(100, MILLISECONDS);
        Observable.zip(
                red.timestamp(),
                green.timestamp(),
                (r, g) -> r.time() - g.time()
        ).subscribe(System.out::println);
    }

    @Test
    public void should_use_combineLatest() throws InterruptedException {
        Observable.combineLatest(
                interval(17, MILLISECONDS).map(x -> "S" + x),
                interval(10, MILLISECONDS).map(x -> "F" + x),
                (s, f) -> f + ":" + s
        ).forEach(System.out::println);

        Thread.sleep(100);
    }

}
