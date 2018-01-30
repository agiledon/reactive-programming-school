package zhangyi.training.school.rp.rxjava.flowables;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import org.junit.Ignore;
import org.junit.Test;

public class BackpressureTest {

    public static final int COUNT = 1_000_000;

    @Test
    @Ignore
    public void should_test_publish_processor() throws InterruptedException {
        PublishProcessor<Integer> source = PublishProcessor.create();

        source
                .observeOn(Schedulers.computation())
                .subscribe(v -> compute(v), Throwable::printStackTrace);

        for (int i = 0; i < COUNT; i++) {
            source.onNext(i);
        }

        Thread.sleep(10_000);
    }
    
    @Test
    @Ignore
    public void should_use_observable() throws InterruptedException {
        Observable
                .create(e -> {
                    for (int i = 0; i < COUNT; i++) {
                        e.onNext(i);
                    }
                    e.onComplete();
                })
                .observeOn(Schedulers.computation())
                .subscribe(v -> compute(v), Throwable::printStackTrace);

        Thread.sleep(10_000);
    }

    @Test
    @Ignore
    public void should_use_flowable() throws InterruptedException {
        Flowable.range(1, COUNT)
                .observeOn(Schedulers.computation())
                .subscribe(v -> compute(v), Throwable::printStackTrace);

        Thread.sleep(10_000);
    }

    private void compute(Object v) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(v);
    }
}
