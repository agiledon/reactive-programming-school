package zhangyi.training.school.reactiveprogramming.rxjava.utils;/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */

public final class Logger {
    private Logger() {

    }

    public static void log(Object msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }
}
