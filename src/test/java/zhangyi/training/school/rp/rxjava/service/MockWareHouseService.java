package zhangyi.training.school.rp.rxjava.service;/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */

import io.reactivex.Flowable;
import zhangyi.training.school.rp.rxjava.domain.Inventory;
import zhangyi.training.school.rp.rxjava.service.WareHouseService;

public class MockWareHouseService implements WareHouseService {
    @Override
    public Flowable<Inventory> getInventoryAsync() {
        System.out.println("start to invoke warehouse service");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Flowable.just(new Inventory(1, "first item"));
    }
}
