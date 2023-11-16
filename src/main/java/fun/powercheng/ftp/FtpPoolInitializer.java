package fun.powercheng.ftp;

import cn.hutool.extra.ftp.Ftp;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * @author hczs8
 */
@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class FtpPoolInitializer {

    private GenericObjectPool<Ftp> ftpPool;

    private final FtpFactory ftpFactory;

    @PostConstruct
    public void init() {
        GenericObjectPoolConfig<Ftp> poolConfig = new GenericObjectPoolConfig<>();
        // 借出和归还的时候都进行有效性验证
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        // 多长时间进行一次后台清理
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofMinutes(1L));
        // 后台清理时，不能通过有效性检查的对象将回收
        poolConfig.setTestWhileIdle(true);
        // 最大空闲数
        poolConfig.setMaxIdle(10);
        // 最小空闲数
        poolConfig.setMinIdle(3);
        this.ftpPool = new GenericObjectPool<>(ftpFactory, poolConfig);
        // 异步初始化连接池，不占用项目启动时间
        CompletableFuture.supplyAsync(() -> {
            try {
                ftpPool.preparePool();
            } catch (Exception e) {
                log.error("ftp连接池初始化异常，异常信息：{}", e.getMessage(), e);
                return false;
            }
            log.info("FTP连接池 初始化完成");
            return true;
        });
    }

}
