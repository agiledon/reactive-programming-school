package zhangyi.training.school.reactiveprogramming.rxjava.service;/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */

import io.reactivex.Flowable;
import zhangyi.training.school.reactiveprogramming.rxjava.domain.Inventory;

public interface WareHouseService {
    Flowable<Inventory> getInventoryAsync();
}
