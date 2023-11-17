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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    private final UserRepository userRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> saveImages(List<MultipartFile> MultipartFiles) throws IOException {
        List<String> imageList = new ArrayList<>();

        for(MultipartFile multipartFile : MultipartFiles) {
            String value = saveImage(multipartFile);
            imageList.add(value);
        }

        return imageList;
    }

    public String saveImage(MultipartFile multipartFile) throws IOException {
        String fileName = generateFileName(multipartFile);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), objectMetadata);

        String accessUrl = amazonS3.getUrl(bucket, fileName).toString();
        return accessUrl;
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
    }

}
