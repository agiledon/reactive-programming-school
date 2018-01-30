package zhangyi.training.school.rp.rxjava.service;/*                                                                      *\
**                                                                      **
**      __  __ _________ _____          ©Mort BI                        **
**     |  \/  / () | () |_   _|         (c) 2015                        **
**     |_|\/|_\____|_|\_\ |_|           http://www.bigeyedata.com       **
**                                                                      **
\*                                                                      */

import io.reactivex.Flowable;
import zhangyi.training.school.rp.rxjava.service.ErpService;

public class MockErpService implements ErpService {
    @Override
    public Flowable<String> getDemandAsync(int inventoryId) {
        System.out.println("start to invoke erp service");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Flowable.just(String.format("demand with inventory id: %s", inventoryId));
    }
}
