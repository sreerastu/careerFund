package com.example.Foundation.service;

import com.example.Foundation.modal.Blog;
import com.example.Foundation.repositories.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BlogServiceImpl {

    @Autowired
    private S3Service s3Service; // Injecting the S3Service

    //   private static String UPLOADS_DIR = "./src/main/resources/static/uploads/";

    @Autowired
    private BlogRepository blogRepository;

    public Blog saveBlog(Blog blog, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            blog.setImage(fileName);
            // Upload the image to S3
            s3Service.uploadImageToS3(fileName, file);
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
