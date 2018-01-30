package zhangyi.training.school.rp.rxjava;/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */

import io.reactivex.Observable;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ObservableVsFutureTest {

    public static final int SIZE = 1000000;

    @Test
    //it take almost 13821
    public void access_large_list_via_future() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Integer>> future = executorService.submit(() -> largeList(SIZE));
        logPerformance(() -> future.get().forEach(System.out::println));
    }

    @Test
    //it take almost 4250
    public void access_large_list_via_observable() throws ExecutionException, InterruptedException {
        Observable<Integer> source = Observable.fromIterable(largeList(SIZE));
        logPerformance(() -> source.subscribe(System.out::println));
    }

    private List<Integer> largeList(int size) {
        List<Integer> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
//            System.out.println(i + " is added.");
            result.add(i);
        }
        return result;
    }

    private void logPerformance(Command command) throws ExecutionException, InterruptedException {
        long startTime = System.nanoTime();
        command.execute();
        long endTime = System.nanoTime();
        long cost = (endTime - startTime) / SIZE;
        System.out.println("It cost time: " + cost);
    }

    private interface Command {
        void execute() throws ExecutionException, InterruptedException;
    }


}
