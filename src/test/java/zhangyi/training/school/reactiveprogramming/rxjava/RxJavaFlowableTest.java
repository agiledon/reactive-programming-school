package zhangyi.training.school.reactiveprogramming.rxjava;

import akka.stream.javadsl.JavaFlowSupport;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;
import zhangyi.training.school.reactiveprogramming.rxjava.domain.Inventory;
import zhangyi.training.school.reactiveprogramming.rxjava.service.ErpService;
import zhangyi.training.school.reactiveprogramming.rxjava.service.MockErpService;
import zhangyi.training.school.reactiveprogramming.rxjava.service.MockWareHouseService;
import zhangyi.training.school.reactiveprogramming.rxjava.service.WareHouseService;

public class RxJavaFlowableTest {

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
    public void should_map_numbers_in_parallel_via_easy_way() {
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
}