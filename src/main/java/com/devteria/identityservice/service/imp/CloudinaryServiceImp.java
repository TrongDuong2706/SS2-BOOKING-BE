package com.devteria.identityservice.service.imp;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryServiceImp {
    public String uploadFile(MultipartFile file) throws IOException;
    public void deleteImage(String publicId);
}
