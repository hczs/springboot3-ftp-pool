package fun.powercheng.ftp;

import cn.hutool.extra.ftp.FtpMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author hczs8
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ftp")
public class FtpConfig {
    /**
     * ftp服务器地址
     */
    private String host;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 登录用户名
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 是否是主动模式，默认是 false
     */
    private FtpMode ftpMode;
}