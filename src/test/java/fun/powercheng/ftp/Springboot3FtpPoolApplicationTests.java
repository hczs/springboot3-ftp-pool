package fun.powercheng.ftp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Springboot3FtpPoolApplicationTests {

    @Autowired
    private FtpTemplate ftpTemplate;

    @Test
    @Order(1)
    void testFtpUpload() {
        boolean uploadResult = ftpTemplate.upload("/test_dir", "hello.txt",
                new ByteArrayInputStream("file upload test".getBytes(StandardCharsets.UTF_8)));
        Assertions.assertTrue(uploadResult, "测试FTP文件上传");
    }

    @Test
    @Order(2)
    void testDownload() {
        byte[] downloadContent = ftpTemplate.download("/test_dir/hello.txt");
        String content = new String(downloadContent, StandardCharsets.UTF_8);
        log.info("download file content: {}", content);
        Assertions.assertTrue(StringUtils.isNotBlank(content), "测试FTP文件下载");
    }

}
