package com.example.ecommerce.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

        @Override
        public String uploadImage(MultipartFile image, String imageDirPath) throws IOException {

        String imageName= image.getOriginalFilename();
        System.out.println("imageName" + image);
        if (imageName == null || !imageName.contains(".")) {
            throw new IllegalArgumentException("Invalid file name");
        }
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(imageName.substring(imageName.lastIndexOf(".")));
        String filePath = imageDirPath + File.pathSeparator + fileName;

        // check folder exist
        File folder = new File(imageDirPath);
        if(!folder.exists()){
            folder.mkdir();
        }

        //  upload to server
        Files.copy(image.getInputStream(), Paths.get(filePath));

        return fileName;
    }
}
