package com.example.Foundation.controller;

import com.example.Foundation.exception.InvalidStudentIdException;
import com.example.Foundation.modal.SuccessStories;
import com.example.Foundation.service.SuccessStoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class SuccessStoryController {

    @Autowired
    private SuccessStoryServiceImpl successStoryService;

    @PostMapping("/register/successStory")
    public ResponseEntity<?> createSuccessStory(@RequestBody SuccessStories successStories,
                                                @RequestParam int studentId) throws InvalidStudentIdException {
        SuccessStories stories = successStoryService.createSuccessStory(successStories,studentId);
        return ResponseEntity.status(HttpStatus.OK).body(stories);
    }

    @GetMapping("/stories")
    public ResponseEntity<?> getAllStories() {
        List<SuccessStories> stories = successStoryService.getAllSuccessStories();
        return ResponseEntity.status(HttpStatus.OK).body(stories);
    }

    @GetMapping("/stories/{storiesId}")
    public ResponseEntity<?> getStoriesById(@PathVariable int ssId) throws InvalidStudentIdException {
        SuccessStories stories = successStoryService.getSuccessStoryById(ssId);
        return ResponseEntity.status(HttpStatus.OK).body(stories);
    }
}
