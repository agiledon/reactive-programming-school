package zhangyi.training.school.rp.rxjava.flowables;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.PublishSubject;
import org.junit.Test;
import zhangyi.training.school.rp.rxjava.domain.Inventory;
import zhangyi.training.school.rp.rxjava.service.ErpService;
import zhangyi.training.school.rp.rxjava.service.MockErpService;
import zhangyi.training.school.rp.rxjava.service.MockWareHouseService;
import zhangyi.training.school.rp.rxjava.service.WareHouseService;

import java.util.concurrent.TimeUnit;

public class FlowableTest {

    @Test
    public void should_run_in_background_with_non_blocking() throws InterruptedException {
        Flowable.fromCallable(() -> {
            Thread.sleep(1000);
            return "Done";
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(System.out::println, Throwable::printStackTrace);

        System.out.println("no blocking");
        Thread.sleep(2000);
    }

    @Test
    public void should_map_numbers_sequentially() {
        Flowable.range(1, 10)
                .observeOn(Schedulers.computation())
                .map(v -> {Thread.sleep(1000); return v * v;})
                .blockingSubscribe(System.out::println);
    }

    @Test
    public void should_flatmap_numbers_in_parallel() {
        Flowable.range(1, 10)
                .flatMap(v ->
                        Flowable.just(v)
                        .subscribeOn(Schedulers.computation())
                        .map(w -> {Thread.sleep(1000); return w * w; })
                )
                .blockingSubscribe(System.out::println);
    }

    @Test
    public void should_map_numbers_in_parallel_via_parallel() {
        Flowable.range(1, 10)
                .parallel()
                .runOn(Schedulers.computation())
                .map(v -> {Thread.sleep(1000); return v * v;})
                .sequential()
                .blockingSubscribe(System.out::println);
    }

    @Test
    public void should_invoke_two_async_method_using_flatMap() {
        WareHouseService wareHouse = new MockWareHouseService();
        ErpService erp = new MockErpService();

        Flowable<Inventory> inventorySource = wareHouse.getInventoryAsync();
        inventorySource.flatMap(inventory ->
                erp.getDemandAsync(inventory.getId())
                .map(demand -> "Item " + inventory.getName() + " has demand " + demand)
        )
        .subscribe(System.out::println);
    }

    @Test
    public void should_be_empty_or_single_element_using_Maybe() {
        Maybe.just(1)
                .map(v -> v + 1)
                .filter(v -> v == 1)
                .defaultIfEmpty(2)
                .test()
                .assertResult(2);
    }

    @Test
    public void should_use_TestScheduler() {
        TestScheduler scheduler = new TestScheduler();

        PublishSubject<Integer> ps = PublishSubject.create();

        TestObserver<Integer> ts = ps.delay(1000, TimeUnit.MILLISECONDS, scheduler).test();

        ts.assertEmpty();

        ps.onNext(1);

        scheduler.advanceTimeBy(999, TimeUnit.MILLISECONDS);

        ts.assertEmpty();

        scheduler.advanceTimeBy(1, TimeUnit.MILLISECONDS);

        ts.assertValue(1);
    }
}