package com.example.Foundation.controller;

import com.example.Foundation.modal.Blog;
import com.example.Foundation.service.BlogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    @Autowired
    private BlogServiceImpl blogService;

    @PostMapping("/upload")
    public ResponseEntity<Blog> handleBlogFileUpload(@ModelAttribute Blog blog, @RequestParam(required = false) MultipartFile file) throws IOException {

        Blog blogs = blogService.saveBlog(blog, file);
        return ResponseEntity.badRequest().body(blogs);
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<?> getBlog(@PathVariable int blogId) {
        Blog blogs = blogService.getBlogById(blogId).orElseThrow(() -> new RuntimeException("blog not found with Id" + ":" + blogId));

        return ResponseEntity.status(HttpStatus.OK).body(blogs);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Blog>> getAllBlogs() {
        List<Blog> allTechnologies = blogService.getAllBlogs();
        return ResponseEntity.ok(allTechnologies);
    }
}
