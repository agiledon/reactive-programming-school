package zhangyi.training.school.rp.rxjava.observables.transforming;

import org.junit.Test;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;


public class ObservableWindowTest {


    /**
     * In this example since we set the window in 3 items, it will create two observables.
     * First one will emit 0,1,2 item, and second will emit 3,4
     */
    @Test
    public void windowCountObservable() {
        Integer[] numbers = {0, 1, 2, 3, 4};

        Observable.from(numbers)
                .window(3)
                .flatMap(o -> {
                    System.out.println("New Observable");
                    return o;
                })
                .subscribe(number -> System.out.println("Number:" + number));

    }

    @Test
    public void stringBuffer() {
        List<String> elements = new ArrayList<>();
        Integer[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Observable.from(numbers)
                .window(4)
                .flatMap(ns -> ns
                        .map(number -> "uniqueKey=" + number + "&")
                        .reduce("", String::concat))
                .map(query -> query.substring(0, query.length() - 1))
                .subscribe(elements::add);
        System.out.println(elements);
    }

}
