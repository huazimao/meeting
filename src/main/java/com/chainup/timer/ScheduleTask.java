package com.chainup.timer;

import com.chainup.wechat.AccessTokenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author QX
 * @date 2020/10/14
 */
@Component
public class ScheduleTask {
    @Autowired
    private AccessTokenApi accessTokenApi;

    //项目启动后执行一次，每隔1h执行
    @Scheduled(fixedRate = 60 * 1000 * 60)
    public void secondScheduledTasks(){
        accessTokenApi.getToken();
    }
}
