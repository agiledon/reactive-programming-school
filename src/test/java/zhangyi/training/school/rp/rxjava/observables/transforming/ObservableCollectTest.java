package zhangyi.training.school.rp.rxjava.observables.transforming;

import org.junit.Test;
import rx.Observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Collect operator include a function where we define the init value, and a BiConsumer function where we
 * receive the accumulator to mutate and the item emitted, which we should add into the accumulator
 */
public class ObservableCollectTest {

    @Test
    public void collectObservableList() {
        Observable.from(Arrays.asList(1, 2))
                .flatMap(item -> getFirstList())
                .collect(ArrayList<Integer>::new, ArrayList::addAll)
                .subscribe(System.out::println);

    }

    private Observable<List<Integer>> getFirstList() {
        return Observable.just(Arrays.asList(1, 2, 3, 4));
    }


}
