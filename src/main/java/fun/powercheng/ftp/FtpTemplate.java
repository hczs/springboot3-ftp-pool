package fun.powercheng.ftp;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.extra.ftp.Ftp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author hczs8
 */
@Component
@Slf4j
public class FtpTemplate {

    private final GenericObjectPool<Ftp> ftpPool;

    public FtpTemplate(FtpPoolInitializer ftpPoolInitializer) {
        this.ftpPool = ftpPoolInitializer.getFtpPool();
    }

    private  <R> R usePooledFtpConnection(Function<Ftp, R> ftpConsumer) {
        Ftp ftp = null;
        try {
            ftp = ftpPool.borrowObject();
            return ftpConsumer.apply(ftp);
        } catch (Exception e) {
            log.error("从连接池获取 ftp 连接异常，异常信息：{}", e.getMessage(), e);
            throw new FtpException(e.getMessage(), e);
        } finally {
            Optional.ofNullable(ftp).ifPresent(ftpPool::returnObject);
        }
    }

    public boolean upload(String destPath, String fileName, InputStream inputStream) {
        log.info("正在上传文件... 目标路径：{} 文件名称：{}", destPath, fileName);
        return usePooledFtpConnection(ftp -> ftp.upload(destPath, fileName, inputStream));
    }

    public byte[] download(String filePath) {
        log.info("正在下载文件... 文件路径：{}", filePath);
        String fileName = FileUtil.getName(filePath);
        String dir = CharSequenceUtil.removeSuffix(filePath, fileName);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        return usePooledFtpConnection(ftp -> {
            ftp.download(dir, fileName, out);
            return out.toByteArray();
        });
    }

}
