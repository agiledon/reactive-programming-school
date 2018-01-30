package zhangyi.training.school.rp.rxjava.observables.combining;

import org.junit.Test;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;
import zhangyi.training.school.rp.rxjava.observables.transforming.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ObservableMergeTest {
    @Test
    public void testMerge() {
        Observable.merge(obPerson(), obPerson1())
                .subscribe(result -> showResult(result.toString()));
    }

    @Test
    public void testMergeLists() {
        Observable.merge(Observable.from(Arrays.asList(2, 1, 13, 11, 5)), Observable.from(Arrays.asList(10, 4, 12, 3, 14, 15)))
                .collect(ArrayList<Integer>::new, ArrayList::add)
                .doOnNext(Collections::sort)
                .subscribe(System.out::println);

    }

    @Test
    public void testMergeDelayError() {
        Scheduler scheduler = Schedulers.newThread();
        Observable.mergeDelayError(
                Observable.error(new RuntimeException())
                        .observeOn(scheduler)
                        .subscribeOn(Schedulers.io()),
                Observable.just("Hello")
                        .observeOn(scheduler)
                        .subscribeOn(Schedulers.io()))
                .finallyDo(() -> System.out.println("Finally action"))
                .subscribe(System.out::println,
                        System.out::println,
                        () -> System.out.println("On complete it should never happen"));
    }


    private void showResult(String s) {
        System.out.println(s);
    }

    private Observable<Person> obPerson() {
        return Observable.just(new Person("pablo", 34, null));
    }

    private Observable<Person> obPerson1() {
        return Observable.just(new Person(null, 25, "male"));
    }

    @Test
    public void testMergeMaxConcurrency() {
        Observable.merge(Observable.just(
                Observable.just(3),
                Observable.just(5),
                Observable.just(1),
                Observable.just(4),
                Observable.just(2)), 2)
                .collect(ArrayList<Integer>::new, ArrayList::add)
                .doOnNext(Collections::sort)
                .subscribe(System.out::println);

    }
}
