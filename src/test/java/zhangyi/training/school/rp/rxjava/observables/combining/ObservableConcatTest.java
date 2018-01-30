package zhangyi.training.school.rp.rxjava.observables.combining;

import org.junit.Test;
import rx.Observable;

public class ObservableConcatTest {
    @Test
    public void testContact() {
        Observable.concat(Observable.just("Hello"),
                Observable.just("reactive"),
                Observable.just("world"))
                .subscribe(System.out::println);
    }
}
