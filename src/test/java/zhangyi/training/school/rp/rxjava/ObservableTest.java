package zhangyi.training.school.rp.rxjava;

import io.reactivex.Observable;
import io.reactivex.observables.GroupedObservable;
import org.junit.Test;
import zhangyi.training.school.rp.rxjava.domain.House;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.reactivex.Observable.interval;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static zhangyi.training.school.rp.rxjava.utils.Logger.log;

public class ObservableTest {
    @Test
    public void should_test_create() {
        Observable.create(emitter -> {
            try {
                if (!emitter.isDisposed()) {
                    for (int i = 1; i < 5; i++) {
                        emitter.onNext(i);
                    }
                    emitter.onComplete();
                }
            } catch (Exception ex) {
                emitter.onError(ex);
            }
        }).subscribe(
                i -> System.out.println(i),
                ex -> System.out.println(ex.getMessage()),
                () -> System.out.println("Done."));
    }

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
        Observable<String> a = Observable.create(s -> new Thread(() -> {
            s.onNext("first");
            s.onNext("second");
            s.onComplete();
        }).start());

        Observable<String> b = Observable.create(s -> new Thread(() -> {
            s.onNext("three");
            s.onNext("four");
            s.onComplete();
        }).start());

        Observable<String> c = Observable.merge(a, b);
        c.subscribe(System.out::println);
    }

    @Test
    public void should_be_unblocking() {
        Observable<Integer> iOb = Observable.<Integer>create(e -> {
            e.onNext(1);
            e.onNext(2);
            e.onComplete();
        }).map(i -> i * i);
        Observable<Integer> strOb = Observable.<String>create(e -> {
            e.onNext("first");
            e.onNext("second");
            e.onNext("third");
            e.onComplete();
        }).map(s -> s.length());

        iOb.subscribe(System.out::println);
        strOb.subscribe(System.out::println);
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
    public void should_test_from_operator() {
        Integer[] items = { 0, 1, 2, 3, 4, 5 };
        Observable observable = Observable.fromArray(items)
                .map(i -> i * i)
                .filter(i -> i > 10);
        observable.subscribe(System.out::println);
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
    
    @Test
    public void should_test_scan() {
        Observable.just(1, 2, 3, 4, 5)
                .scan((x, y) -> x + y)
                .subscribe(System.out::println);
    }

    @Test
    public void should_test_groupBy() {
        Observable<GroupedObservable<String, House>> groupByCommunityNameObservable =
                Observable.fromIterable(getHouses())
                .groupBy(h -> h.getCommunityName());

        Observable.concat(groupByCommunityNameObservable).subscribe(h -> System.out.println(h.getCommunityName()));
    }

    @Test
    public void should_test_join() throws InterruptedException {
        List<House> houses = getHouses();
        Observable<House> houseObservable = Observable.interval(1, TimeUnit.SECONDS)
                .map(index -> houses.get(index.intValue()))
                .take(houses.size());

        Observable<Long> tictoc = Observable.interval(1, TimeUnit.SECONDS);

        houseObservable.join(
                tictoc,
                h -> Observable.timer(2, TimeUnit.SECONDS),
                l -> Observable.timer(0, TimeUnit.SECONDS),
                (h, l) -> l + "--->" + h.getCommunityName()).subscribe(System.out::println);

        Thread.sleep(10000);

    }

    private List<House> getHouses() {
        List<House> houses = new ArrayList<>();
        houses.add(new House("中粮·海景壹号", "中粮海景壹号新出大平层！总价4500W起"));
        houses.add(new House("竹园新村", "满五唯一，黄金地段"));
        houses.add(new House("中粮·海景壹号", "毗邻汤臣一品"));
        houses.add(new House("竹园新村", "顶层户型，两室一厅"));
        houses.add(new House("中粮·海景壹号", "南北通透，豪华五房"));
        return houses;
    }

    @Test
    public void should_create_observable_immediatly_or_lazyly() {

//        Observable<Integer> eagerOb = Observable.fromIterable(getIntegers());

        Observable<Integer> lazyOb = Observable.defer(() -> Observable.fromIterable(getIntegers()));

        lazyOb.subscribe(System.out::println);


    }

    private List<Integer> getIntegers() {
        List<Integer> l = new ArrayList();
        for (int i = 0; i <10; i++) {
            System.out.println(i);
            l.add(i);
        }
        return l;
    }

}
