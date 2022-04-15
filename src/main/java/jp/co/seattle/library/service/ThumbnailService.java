package jp.co.seattle.library.service;

import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import jp.co.seattle.library.config.MinioConfig;

/**
 * サムネイルサービス
 * 
 * サムネイルに関してS3とのやりとりの処理を実装する
 */
@Controller
public class ThumbnailService {
    final static Logger logger = LoggerFactory.getLogger(ThumbnailService.class);

    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioConfig minioConfig;

    /**
     * サムネイル画像をアップロードする
     * @param thumbnailName サムネイルファイル名
     * @param file サムネイルファイル
     * @return アップロードファイル名
     * @throws Exception
     */
    public String uploadThumbnail(String thumbnailName, MultipartFile file)
            throws Exception {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String extension = thumbnailName.substring(thumbnailName.lastIndexOf("."));

        //ファイル名をタイムスタンプの値にリネームする
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestampStr = (sdf.format(timestamp));
        String fileName = timestampStr + extension;

        // S3にサムネイル画像をアップロード
        InputStream inputStream = file.getInputStream();
        @SuppressWarnings("unused")
		ObjectWriteResponse owr = minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(minioConfig.getMinioInfo("s3.bucket-name"))
                        .object(fileName)
                        .stream(inputStream, -1, 10485760)
                        .build());

        return fileName;

    }

    /**
     * URL取得
     * @param fileName サムネイルファイル名
     * @return ファイルのURL
     * @throws Exception
     */
    public String getURL(String fileName) throws Exception {
        String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(minioConfig.getMinioInfo("s3.bucket-name"))
                        .object(fileName)
                        .build());

        return url;

    }

}
