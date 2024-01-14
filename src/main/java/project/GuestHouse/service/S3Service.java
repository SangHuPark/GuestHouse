package project.GuestHouse.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.GuestHouse.domain.entity.UserImage;
import project.GuestHouse.domain.entity.User;
import project.GuestHouse.exception.errorCode.UserErrorCode;
import project.GuestHouse.exception.exception.ApiException;
import project.GuestHouse.repository.UserImageRepository;
import project.GuestHouse.repository.UserRepository;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /*public String saveImages(List<MultipartFile> MultipartFiles) throws IOException {
        List<String> imageList = new ArrayList<>();

        for (MultipartFile multipartFile : MultipartFiles) {
            String value = saveImage(multipartFile, "testEmail");
            imageList.add(value);
        }

        return imageList.toString();
    }*/

    public String saveImage(MultipartFile multipartFile, String email) {
        String originalName = multipartFile.getOriginalFilename();
        UserImage userImage = new UserImage(originalName);
        String filename = generateFileName(originalName);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_EMAIL_NOT_FOUND));

        userImage.setStoredName(filename);
        userImage.setUser(user);
        log.info("fileName: " + filename);

        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getInputStream().available());

            amazonS3.putObject(bucket, filename, multipartFile.getInputStream(), objectMetadata);

            String accessUrl = amazonS3.getUrl(bucket, filename).toString();
            userImage.setAccessUrl(accessUrl);
        } catch (IOException e) {

        }

        userImageRepository.save(userImage);

        return userImage.getAccessUrl();
    }

    // 이미지 파일의 이름을 저장하기 위한 이름으로 변환하는 메소드
    private String generateFileName(String originName) {
        // LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        return UUID.randomUUID() + extractExtension(originName);
    }

    // 이미지 파일 확장자 추출 메소드
    public String extractExtension(String originName) {
        int index = originName.lastIndexOf('.');

        return originName.substring(index, originName.length());
    }

}
