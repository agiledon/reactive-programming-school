package zhangyi.training.school.rp.rxjava.observables.creating;/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */

import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static zhangyi.training.school.rp.rxjava.utils.Logger.log;

public class TimerAndIntervalTest {
    @Test
    public void should_wait_one_second() throws InterruptedException {
        Observable.timer(1, TimeUnit.SECONDS)
                .subscribe((Long zero) -> log(zero));
        Thread.sleep(2000);
    }

    @Test
    public void should_execute_each_one_second() throws InterruptedException {
        Observable.interval(1_000_000 / 60, TimeUnit.MICROSECONDS)
                .subscribe((Long i) -> log(i));
        Thread.sleep(10000);
    }
}
