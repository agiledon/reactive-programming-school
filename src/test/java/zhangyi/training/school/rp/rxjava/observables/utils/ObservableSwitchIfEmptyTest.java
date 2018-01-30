package zhangyi.training.school.rp.rxjava.observables.utils;

import org.junit.Test;
import rx.Observable;

import java.util.Random;

/**
 * SwitchIfEmpty is a powerful operator that gets handy when in your pipeline a filter block the emissions
 * but still you need to do a compensation or emit something else
 */
public class ObservableSwitchIfEmptyTest {


    @Test
    public void ifEmpty() throws InterruptedException {
        Observable.just(getDataFromDatabase())
                .filter(value -> !value.isEmpty())
                .switchIfEmpty(Observable.just("No data in database so I go shopping"))
                .subscribe(System.out::println);
    }

    private String getDataFromDatabase() {
        if(new Random().nextBoolean()){
            return "data";
        }
        return "";
    }


}
