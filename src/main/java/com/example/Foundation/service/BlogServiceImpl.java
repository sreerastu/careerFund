package com.example.Foundation.service;

import com.example.Foundation.modal.Blog;
import com.example.Foundation.repositories.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BlogServiceImpl {

    @Autowired
    private S3Service s3Service; // Injecting the S3Service

    @Autowired
    private BlogRepository blogRepository;

    @Value("${aws.s3.BlogFolder}")
    private String folderName;

    public Blog saveBlog(Blog blog, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            blog.setImage(fileName);
            // Upload the image to S3
            s3Service.uploadImageToS3(folderName,fileName, file);
        }
        return blogRepository.save(blog);
    }

    public Optional<Blog> getBlogById(int blogId) {
        return blogRepository.findById(blogId);
    }

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public Blog updateBlog(int blogId, Blog blog, MultipartFile file) throws IOException {
        Blog blogX = blogRepository.findById(blogId).orElseThrow(() -> new RuntimeException("blog not found"));
        if (blog.getTitle() != null) {
            blogX.setTitle(blog.getTitle());
        }
        if (blog.getDescription() != null) {
            blogX.setDescription(blog.getDescription());
        }
        if (blog.getPostedDateTime() != null) {
            blogX.setPostedDateTime(blog.getPostedDateTime());
        }
        if (blog.getUpcomingDateTime() != null) {
            blogX.setUpcomingDateTime(blog.getUpcomingDateTime());
        }
        if (file != null && !file.isEmpty()) {
            String oldImageName = blog.getImage();
            String newImageName = file.getOriginalFilename();
            blogX.setImage(newImageName);

            // Upload new image to S3
            s3Service.uploadImageToS3(folderName,newImageName, file);

            // Delete the old image file from S3
            if (oldImageName != null) {
                s3Service.deleteImageFromS3(oldImageName);
            }
        }
        return blogRepository.save(blogX);
    }

}
