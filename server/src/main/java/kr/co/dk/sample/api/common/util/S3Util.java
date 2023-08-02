package kr.co.dk.sample.api.common.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import kr.co.dk.sample.api.app.board.controller.BoardController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static kr.co.dk.sample.api.common.RestKey.*;

@Component("S3Util")
public class S3Util {
    public static final String CLOUD_FRONT_DOMAIN_NAME = "dq582wpwqowa9.cloudfront.net";
    private static final Logger log = LogManager.getLogger(BoardController.class);
    private final AmazonS3 s3Client;
    @Value("momobuild")
    private String bucketName;

    //아마존 연결하는 것
    public S3Util() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(ADMIN_ACCESS_KEY, ADMIN_SECRET_KEY);
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(RESION_NAME)
                .build();
    }

    /*
     * 파일 s3 직접 업로드
     * 파일이 작은 단순 이미지등의 파일만 올린다.
     */
    public String uploadSingleFile(MultipartFile file, String path) throws IOException {

        log.info(file.getOriginalFilename() + "3");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String tmpName = file.getOriginalFilename();
        long milliSecond = System.currentTimeMillis();
        String milName = String.valueOf(milliSecond).substring(10, 13);
        String fileExt = tmpName.substring(tmpName.lastIndexOf(".") + 1);
        String s3FilePath = "file/" + path + CommonUtil.getDate("file_date") + "." + fileExt;

        s3Client.putObject(new PutObjectRequest(bucketName, s3FilePath, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.Private));
//        S3Object fullObject = amazonClientS3.getObject(bucketName, s3FilePath);

        return s3FilePath;
    }

    public List<String> uploadMultiFile(List<MultipartFile> files, String path) throws IOException {
        List<String> imgUrlList = new ArrayList<>();
        // forEach 구문을 통해 multipartFile로 넘어온 파일들 하나씩 fileNameList에 추가
        for (MultipartFile file : files) {
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                s3Client.putObject(new PutObjectRequest(bucketName, "file/" + path + fileName, inputStream, null)
                        .withCannedAcl(CannedAccessControlList.Private));
                imgUrlList.add("file/" + path + fileName);
            } catch(IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return imgUrlList;
    }

    // 이미지파일명 중복 방지
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 유효성 검사
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public String createSignedUrl(String path) { //파일명(경로포함), 다운받을수 있는 시간
        try {

            // Generate the presigned URL.
            log.info("Generating pre-signed URL.");
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, path)
                            .withMethod(HttpMethod.GET);
            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
            log.info("Pre-Signed URL: " + url.toString());

            return url.toString();
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }

        return "fail";
    }


    public String fileUrl(String filekey) {
        String url = s3Client.getUrl(bucketName, filekey).toString();
        return url;
    }

    public long getFileSize(String filekey) {
        long size = s3Client.getObjectMetadata(bucketName, filekey).getContentLength();
        return size;
    }

    public String getFileMime(String filekey) {
        String mime = s3Client.getObjectMetadata(bucketName, filekey).getContentType();
        return mime;
    }

    public void deleteFile(String filekey) {
        s3Client.deleteObject(bucketName, filekey);
    }

    public String copyFile(String sourceKey, String destinationKey) {
        CopyObjectRequest copyObjRequest = new CopyObjectRequest(bucketName, sourceKey, bucketName, destinationKey);
        CopyObjectResult a = s3Client.copyObject(copyObjRequest);
        return a.toString();
    }


    //끝 부분
}
