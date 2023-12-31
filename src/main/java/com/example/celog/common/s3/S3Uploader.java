package com.example.celog.common.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader implements Uploader{

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile);
        return upload(uploadFile, dirName);
    }

    @Override
    public void delete(String key) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));
    }

    @Override
    public Resource downloadResource(String key) {
        S3Object object = amazonS3Client.getObject(bucket, key);
        S3ObjectInputStream objectContent = object.getObjectContent();
        return new InputStreamResource(objectContent);
    }

    private String upload(File uploadFile, String dirName) {
        log.info(dirName);
        String fileName = dirName + "/" + FileUtil.getRandomFileName(uploadFile.getName());
        log.info(fileName);
        String uploadImageUrl = putS3(uploadFile, fileName);
        log.info(uploadImageUrl);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일 삭제 성공");
        } else {
            log.info("파일 삭제");
        }
    }

    public static File convert(MultipartFile file) throws IOException {

        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();

        return convFile;

    }
}