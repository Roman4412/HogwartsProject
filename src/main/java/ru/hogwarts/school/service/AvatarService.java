package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepo;
import ru.hogwarts.school.repository.StudentRepo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;


@Service
@Transactional
public class AvatarService {
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private final StudentRepo studentRepo;
    private final AvatarRepo avatarRepo;

    public AvatarService(StudentRepo studentRepo, AvatarRepo avatarRepo) {
        this.studentRepo = studentRepo;
        this.avatarRepo = avatarRepo;
    }


    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        Student student = studentRepo.getReferenceById(studentId);
        Path filePath = Path.of(avatarsDir, student + "." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepo.save(avatar);
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Avatar findAvatar(long studentId) {
        return avatarRepo.findByStudentId(studentId).orElseThrow();
    }

}