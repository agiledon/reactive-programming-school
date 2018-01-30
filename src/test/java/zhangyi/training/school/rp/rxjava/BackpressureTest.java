package zhangyi.training.school.rp.rxjava;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

public class BackpressureTest {
    @Test
    public void should_test_publish_processor() throws InterruptedException {
        PublishProcessor<Integer> source = PublishProcessor.create();

        source
                .observeOn(Schedulers.computation())
                .subscribe(v -> compute(v), Throwable::printStackTrace);

        for (int i = 0; i < 1_000_000; i++) {
            source.onNext(i);
        }

        Thread.sleep(10_000);
    }
    
    @Test
    public void should_use_observable() throws InterruptedException {
        Observable
                .create(e -> {
                    for (int i = 0; i < 1_000_000_000; i++) {
                        e.onNext(i);
                    }
                    e.onComplete();
                })
                .observeOn(Schedulers.computation())
                .subscribe(v -> compute(v), Throwable::printStackTrace);

        Thread.sleep(10_000);
    }

    @Test
    public void should_use_flowable() throws InterruptedException {
        Flowable.range(1, 1_000_000)
                .observeOn(Schedulers.computation())
                .subscribe(v -> compute(v), Throwable::printStackTrace);

        Thread.sleep(10_000);
    }

    private void compute(Object v) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(v);
    }
}
