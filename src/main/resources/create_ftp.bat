:: 使用：https://hub.docker.com/r/fauria/vsftpd/ 镜像进行搭建 用户名 ftpuser 密码 123456
docker run -d -v D:\dev\ftp\data:/home/vsftpd -p 20:20 -p 21:21 -p 21100-21110:21100-21110 -e FTP_USER=ftpuser -e FTP_PASS=123456 -e PASV_ADDRESS=127.0.0.1 -e PASV_MIN_PORT=21100 -e PASV_MAX_PORT=21110 --name vsftpd --restart=always fauria/vsftpd