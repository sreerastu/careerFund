package com.example.Foundation.service;

import com.example.Foundation.modal.Articles;
import com.example.Foundation.modal.Blog;
import com.example.Foundation.repositories.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class BlogServiceImpl {

    private static String UPLOADS_DIR = "./src/main/resources/static/uploads/";

    @Autowired
    private BlogRepository blogRepository;

    public Blog saveBlog(Blog blog, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            // Save image file to uploads directory
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            Path path = Paths.get(UPLOADS_DIR + fileName);
            Files.write(path, file.getBytes());
            blog.setImage(fileName);
        }
        return blogRepository.save(blog);
    }

    public Optional<Blog> getBlogById(int blogId) {
        return blogRepository.findById(blogId);
    }

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public Blog updateBlog(int blogId, Blog blog) throws IOException {
        Optional<Blog> optionalArticle = blogRepository.findById(blogId);
        if (optionalArticle.isPresent()) {
            Blog existingBlog = optionalArticle.get();
            existingBlog.setTitle(blog.getTitle());
            existingBlog.setDescription(blog.getDescription());
            if (blog.getImage() != null) {
                blog.setImage(blog.getImage());
            }
            return blogRepository.save(existingBlog);
        } else {
            throw new IllegalArgumentException("Article not found with ID: " + blogId);
        }
    }

}
