import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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

    public String upload(MultipartFile multipartFile , LocalDateTime useDate) throws IOException {
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        long size = multipartFile.getSize();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType(multipartFile.getContentType());
        objMeta.setContentLength(size);

//        objMeta.setContentLength(multipartFile.getInputStream().available());
//        amazonS3.putObject(bucket, "receipts/" + s3FileName, multipartFile.getInputStream(), objMeta)
        // s3 업로드
        String currentFilePath = "receipts/" + getPathByUseDate(useDate) + s3FileName;
        amazonS3.putObject(
                new PutObjectRequest(bucket, currentFilePath, multipartFile.getInputStream(), objMeta)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        System.out.println(amazonS3.doesObjectExist(bucket, currentFilePath));
        amazonS3.deleteObject(bucket, currentFilePath);

        String imagePath = amazonS3.getUrl(bucket, currentFilePath).toString(); // 접근 가능한 url 가져오기

        return imagePath;
    }

    public void remove(String path) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, "/receipts/627610ef-9509-4bf9-92bf-7d87dbd84395-래서펜더.webp"));
//        Regions clientRegion = Regions.DEFAULT_REGION;
//        String bucketName = "*** Bucket name ***";
//        String keyName = "*** Key name ****";
//
//        try {
//            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
//                    .withCredentials(new ProfileCredentialsProvider())
//                    .withRegion(clientRegion)
//                    .build();
//
//            s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
//        } catch (AmazonServiceException e) {
//            // The call was transmitted successfully, but Amazon S3 couldn't process
//            // it, so it returned an error response.
//            e.printStackTrace();
//        } catch (SdkClientException e) {
//            // Amazon S3 couldn't be contacted for a response, or the client
//            // couldn't parse the response from Amazon S3.
//            e.printStackTrace();
//        }
//    }
    }

}