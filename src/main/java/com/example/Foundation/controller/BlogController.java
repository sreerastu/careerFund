package com.example.Foundation.controller;

import com.example.Foundation.modal.Blog;
import com.example.Foundation.service.BlogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    @Autowired
    private BlogServiceImpl blogService;

    @PostMapping("/upload")
    public ResponseEntity<Blog> handleBlogFileUpload(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("description") String description) {
        try {
            byte[] compressedImageBytes = blogService.compressImage(file.getBytes());

            Blog blog = new Blog();
            blog.setImage(compressedImageBytes);
            blog.setDescription(description);
            Blog blogs = blogService.saveBlog(blog);

            return ResponseEntity.status(HttpStatus.OK).body(blogs);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<byte[]> getBlog(@PathVariable int blogId) {
        Optional<Blog> tech = blogService.getBlogById(blogId);
        if (tech.isPresent()) {
            byte[] imageBytes = blogService.getBlogById(blogId).get().getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Adjust based on your image type

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Blog>> getAllBlogs() {
        List<Blog> allTechnologies = blogService.getAllBlogs();
        return ResponseEntity.ok(allTechnologies);
    }
}
