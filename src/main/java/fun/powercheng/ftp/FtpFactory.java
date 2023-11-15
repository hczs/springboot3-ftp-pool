package fun.powercheng.ftp;

import cn.hutool.extra.ftp.Ftp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author hczs8
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FtpFactory extends BasePooledObjectFactory<Ftp> {

    private final FtpConfig ftpConfig;

    @Override
    public Ftp create() throws InterruptedException {
        log.info("FTP连接中... FTP配置信息: {}", ftpConfig);
        // 模拟连接耗时
        Thread.sleep(2_000);
        Ftp ftp = new Ftp(ftpConfig.getHost(), ftpConfig.getPort(), ftpConfig.getUsername(), ftpConfig.getPassword());
        ftp.setMode(ftpConfig.getFtpMode());
        // 执行完毕后回到主目录
        ftp.setBackToPwd(true);
        log.info("FTP连接已创建");
        return ftp;
    }

    @Override
    public PooledObject<Ftp> wrap(Ftp ftp) {
        return new DefaultPooledObject<>(ftp);
    }

    @Override
    public void destroyObject(PooledObject<Ftp> pooledObject) throws Exception {
        log.info("FTP连接销毁");
        if (pooledObject == null) {
            return;
        }
        Ftp ftp = pooledObject.getObject();
        ftp.close();
    }

    @Override
    public boolean validateObject(PooledObject<Ftp> pooledObject) {
        Ftp ftp = pooledObject.getObject();
        FTPClient client = ftp.getClient();
        try {
            return client.sendNoOp();
        } catch (IOException e) {
            log.error("验证FTP连接失败，FTP连接不可用错误信息：{}", e.getMessage(), e);
        }
        return false;
    }
}
