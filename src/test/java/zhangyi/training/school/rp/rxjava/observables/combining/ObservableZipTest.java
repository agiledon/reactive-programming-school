package zhangyi.training.school.rp.rxjava.observables.combining;

import org.junit.Test;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public class ObservableZipTest {
    @Test
    public void testZip() {
        long start = System.currentTimeMillis();
        Observable.zip(obString(), obString1(), obString2(), (s, s2, s3) -> s.concat(s2)
                .concat(s3))
                .subscribe(result -> showResult("Sync in:", start, result));
    }


    public void showResult(String transactionType, long start, String result) {
        System.out.println(result + " " +
                transactionType + String.valueOf(System.currentTimeMillis() - start));
    }

    public Observable<String> obString() {
        return Observable.just("")
                .doOnNext(val -> {
                    System.out.println("Thread " + Thread.currentThread()
                            .getName());
                })
                .map(val -> "Hello");
    }

    public Observable<String> obString1() {
        return Observable.just("")
                .doOnNext(val -> {
                    System.out.println("Thread " + Thread.currentThread()
                            .getName());
                })
                .map(val -> " World");
    }

    public Observable<String> obString2() {
        return Observable.just("")
                .doOnNext(val -> {
                    System.out.println("Thread " + Thread.currentThread()
                            .getName());
                })
                .map(val -> "!");
    }


    private Scheduler scheduler;
    private Scheduler scheduler1;
    private Scheduler scheduler2;

    /**
     * Since every observable into the zip is created to subscribeOn a different thread, it´s means all of them will run in parallel.
     * By default Rx is not async, only if you explicitly use subscribeOn.
     */
    @Test
    public void testAsyncZip() {
        scheduler = Schedulers.newThread();
        scheduler1 = Schedulers.newThread();
        scheduler2 = Schedulers.newThread();
        long start = System.currentTimeMillis();
        Observable.zip(obAsyncString(), obAsyncString1(), obAsyncString2(), (s, s2, s3) -> s.concat(s2)
                .concat(s3))
                .subscribe(result -> showResult("Async in:", start, result));
    }

    private Observable<String> obAsyncString() {
        return Observable.just("")
                .observeOn(scheduler)
                .doOnNext(val -> {
                    System.out.println("Thread " + Thread.currentThread()
                            .getName());
                })
                .map(val -> "Hello");
    }

    private Observable<String> obAsyncString1() {
        return Observable.just("")
                .observeOn(scheduler1)
                .doOnNext(val -> {
                    System.out.println("Thread " + Thread.currentThread()
                            .getName());
                })
                .map(val -> " World");
    }

    private Observable<String> obAsyncString2() {
        return Observable.just("")
                .observeOn(scheduler2)
                .doOnNext(val -> {
                    System.out.println("Thread " + Thread.currentThread()
                            .getName());
                })
                .map(val -> "!");
    }

    class Pair {
        String a;
        Integer b;

        Pair(String a, Integer b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "a='" + a + '\'' +
                    ", b=" + b +
                    '}';
        }
    }

    @Test
    public void testZipDifferentTypes() {
        Observable.zip(obA(), obB(), Pair::new)
                .subscribe(s -> System.out.println(s.toString()));
    }

    private Observable<String> obA() {
        return Observable.just("hello");
    }

    private Observable<Integer> obB() {
        return Observable.just(1);
    }
}
