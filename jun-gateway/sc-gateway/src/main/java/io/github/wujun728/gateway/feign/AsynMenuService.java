package io.github.wujun728.gateway.feign;

import io.github.wujun728.common.model.SysMenu;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Future;

/**
 * 异步Menu服务
 *
 * @author zlt
 * @version 1.0
 * @date 2021/8/8
 * <p>
 * Blog: https://zlt2000.gitee.io
 * Github: https://github.com/zlt2000
 */
@Component
public class AsynMenuService {
    @Lazy
    @Resource
    private MenuService menuService;

    @Async
    public Future<List<SysMenu>> findByRoleCodes(String roleCodes) {
        List<SysMenu> result = menuService.findByRoleCodes(roleCodes);
        return new AsyncResult<>(result);
    }
}
