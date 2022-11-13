package com.example.hsap.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Upload {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    private String getPathByUseDate(LocalDateTime useDate) {
        return useDate.getYear() + "/" + useDate.getMonth() + "/";
    }

    public String upload(MultipartFile multipartFile , LocalDateTime useDate, String department) throws IOException {
        String s3FileName = UUID.randomUUID().toString();
        long size = multipartFile.getSize();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType(multipartFile.getContentType());
        objMeta.setContentLength(size);

        String currentFilePath = "receipts/" + department + "/" + getPathByUseDate(useDate) + s3FileName;
        amazonS3.putObject(
                new PutObjectRequest(bucket, currentFilePath, multipartFile.getInputStream(), objMeta)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        String imagePath = amazonS3.getUrl(bucket, currentFilePath).toString(); // 접근 가능한 url 가져오기

        return imagePath;
    }

    public String update(MultipartFile multipartFile, LocalDateTime useDate, List<String> urlList) throws IOException {
        String s3FileName = UUID.randomUUID().toString();
        long size = multipartFile.getSize();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType(multipartFile.getContentType());
        objMeta.setContentLength(size);

        // 기존 이미지 삭제
        if (urlList != null) {
            for (String url : urlList) {
                String path = url.substring("https://hsap-bucket.s3.ap-northeast-2.amazonaws.com/".length());
                amazonS3.deleteObject(bucket, path);
            }
        }

        // s3 업로드
        String currentFilePath = "receipts/" + getPathByUseDate(useDate) + s3FileName;
        amazonS3.putObject(
                new PutObjectRequest(bucket, currentFilePath, multipartFile.getInputStream(), objMeta)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        String imagePath = amazonS3.getUrl(bucket, currentFilePath).toString(); // 접근 가능한 url 가져오기

        return imagePath;
    }

    public void remove(List<String> urlList) {
        // 기존 이미지 삭제
        if (urlList == null) return;
        for (String url : urlList) {
            String path = url.substring("https://hsap-bucket.s3.ap-northeast-2.amazonaws.com/".length());
            amazonS3.deleteObject(bucket, path);
        }
    }

}