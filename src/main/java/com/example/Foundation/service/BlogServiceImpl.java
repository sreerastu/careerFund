package com.example.Foundation.service;

import com.example.Foundation.modal.Blog;
import com.example.Foundation.modal.Enquire;
import com.example.Foundation.repositories.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BlogServiceImpl {

    @Autowired
    private BlogRepository blogRepository;

    public Blog saveBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    public Optional<Blog> getBlogById(int blogId) {
        return blogRepository.findById(blogId);
    }

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public byte[] compressImage(byte[] originalImageBytes) throws IOException {
        // Convert the byte array to a BufferedImage
        ByteArrayInputStream bis = new ByteArrayInputStream(originalImageBytes);
        BufferedImage bufferedImage = ImageIO.read(bis);

        // Create a ByteArrayOutputStream to write the compressed image
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // Compress the image
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(0.5f); // Adjust the quality as needed

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(bos)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(bufferedImage, null, null), param);
        }

        // Get the compressed image bytes
        byte[] compressedImageBytes = bos.toByteArray();

        // Close streams
        bis.close();
        bos.close();

        return compressedImageBytes;
    }

}
