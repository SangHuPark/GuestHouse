package project.GuestHouse.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.GuestHouse.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveImages(List<MultipartFile> MultipartFiles, String imgName) throws IOException {
        List<String> imageList = new ArrayList<>();
        imgName = imgName + "Post";

        for(MultipartFile multipartFile : MultipartFiles) {
            String value = saveImage(multipartFile, imgName);
            imageList.add(value);
        }

        return imageList.toString();
    }

    public String saveImage(MultipartFile multipartFile, String imgName) throws IOException {
        String fileName = generateFileName(imgName);
        log.info("fileName: " + fileName);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), objectMetadata);

        String accessUrl = amazonS3.getUrl(bucket, fileName).toString();
        return accessUrl;
    }

    private String generateFileName(String imgName) {
        String fileName = LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "-" + imgName;
        return fileName;
    }

}
