package zhangyi.training.school.rp.rxjava.observables.errors;

import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;


/**
 * Observable pipeline does not allow Exception, just runtimeExceptions,
 * that´s why if your code run into pipeline can throw an exception your observable wont compile.
 * In order to fix it you will have to pass through the pipeline runtime exceptions
 */
public class ObservableExceptionsTest {

    /**
     * Here is a silly example how in order to make your pipeline compile you must catch the exception and parse it as Runtime exception
     */
    @Test
    public void observableException() {
        Integer[] numbers = {0, 1, 2, 3, 4, 5};

        Observable.from(numbers)
                .doOnNext(number -> {
                    if (number > 3) {
                        try {
                            throw new IllegalArgumentException();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                })
                .doOnError(t -> System.out.println("Expecting illegal argument exception:" + t.getMessage()))
                .subscribe(System.out::println, Throwable::printStackTrace);

    }

    int count = 0;

    /**
     * Here we can see how onErrorResumeNext works and emit an item in case that an error occur in the pipeline and an exception is propagated
     */
    @Test
    public void observableOnErrorResumeNext() {
        Subscription subscription = Observable.just(null)
                .map(Object::toString)
                .doOnError(failure -> System.out.println("Error:" + failure.getCause()))
                .retryWhen(errors -> errors.doOnNext(o -> count++)
                                .flatMap(t -> count > 3 ? Observable.error(t) :
                                        Observable.just(null).delay(100, TimeUnit.MILLISECONDS)),
                        Schedulers.newThread())
                .onErrorResumeNext(t -> {
                    System.out.println("Error after all retries:" + t.getCause());
                    return Observable.just("I save the world for extinction!");
                })
                .subscribe(s -> System.out.println(s));
        new TestSubscriber((Observer) subscription).awaitTerminalEvent(500, TimeUnit.MILLISECONDS);
    }

    /**
     * Here is a silly example how runtimeExceptions are not needed
     */
    @Test
    public void observableRuntimeException() {
        Integer[] numbers = {0, 1, 2, 3, 4, 5};

        Observable.from(numbers)
                .doOnNext(number -> throwRuntimeException())
                .doOnError(t -> System.out.println("Expecting illegal argument exception:" + t.getMessage()))
                .subscribe(System.out::println, Throwable::printStackTrace);
    }

    @Test
    public void observableOnErrorResumeException() {
        Integer[] numbers = {0, 1, 2, 3, 4, 5};

        Observable.from(numbers)
                .doOnNext(number -> {
                    if (number > 3) {
                        try {
                            throw new IllegalArgumentException();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                })
                .onErrorResumeNext(t -> Observable.just(666))
                .subscribe(System.out::println);

    }

    @Test
    public void observableOnError() {
        Integer[] numbers = {0, 1, 2, 3, 4, 5};

        Observable.from(numbers)
                .doOnNext(number -> {
                    if (number > 3) {
                        try {
                            throw new IllegalArgumentException();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                })
                .doOnError(t -> System.out.println("Exception happens " + t.getMessage()))
                .subscribe(n -> System.out.println("onNext:" + n), e -> System.out.println("onRrror:" + e.getMessage()), System.out::println);

    }

    private void throwRuntimeException() {
        throw new RuntimeException();
    }

    @Test
    public void onErrorResumeNext() {
        Observable.from(Arrays.asList(1, 2, 3, 4))
                .flatMap(number -> Observable.just(number)
                        .doOnNext(n -> {
                            if (n == 2) {
                                throw new NullPointerException();
                            }
                        })
                        .onErrorResumeNext(t -> Observable.just(666)))
                .subscribe(n -> System.out.println("number:" + n));
    }

    /**
     * This retry since is after a flatMap it will retry the creation of the flatMap operation
     */
    @Test
    public void retryInFlatMap() {
        Observable.from(Arrays.asList(1, 2, 3, 4))
                .flatMap(number -> Observable.just(number)
                        .doOnNext(n -> {
                            if (n == 2) {
                                throw new NullPointerException();
                            }
                        }))
                .retry(3)
                .subscribe(n -> System.out.println("number:" + n), Throwable::printStackTrace);
    }

    private int cont = 0;

    /**
     * This retry since is after a map it wont retry
     */
    @Test
    public void retryInMap() {
        Observable.from(Arrays.asList(1, 2, 3, 4))
                .map(number -> {
                    if (cont == 2) {
                        throw new NullPointerException();
                    }
                    cont++;
                    return number;
                })
                .retry(3)
                .subscribe(n -> System.out.println("number:" + n), Throwable::printStackTrace);
    }


    @Test
    public void retryWhenConnectionError() {
        Subscription subscription = Observable.just(null)
                .map(connection -> {
                    System.out.println("Trying to open connection");
                    connection.toString();
                    return connection;
                })
                .retryWhen(errors -> errors.doOnNext(o -> count++)
                                .flatMap(t -> count > 3 ? Observable.error(t) :
                                        Observable.just(null).delay(100, TimeUnit.MILLISECONDS)),
                        Schedulers.newThread())
                .subscribe(s -> System.out.println(s));
        new TestSubscriber((Observer) subscription).awaitTerminalEvent(500, TimeUnit.MILLISECONDS);
    }

}
