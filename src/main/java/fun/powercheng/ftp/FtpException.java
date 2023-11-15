package fun.powercheng.ftp;

/**
 * @author hczs8
 */
public class FtpException extends RuntimeException {

    public FtpException() {
        super();
    }

    public FtpException(String message) {
        super(message);
    }

    public FtpException(String message, Throwable cause) {
        super(message, cause);
    }
}
