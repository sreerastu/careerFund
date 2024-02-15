package com.example.Foundation.service;

import com.example.Foundation.exception.InvalidStudentIdException;
import com.example.Foundation.exception.StudentNotFoundException;
import com.example.Foundation.modal.Student;
import com.example.Foundation.modal.SuccessStories;
import com.example.Foundation.repositories.SuccessStoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class SuccessStoryServiceImpl {

    @Value("${aws.s3.SuccessStoriesFolder}")
    private String folderName;

    @Autowired
    private S3Service s3Service; // Injecting the S3Service
    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private SuccessStoryRepository successStoryRepository;

    public SuccessStories createSuccessStory(SuccessStories successStories, int studentId, MultipartFile file) throws InvalidStudentIdException, IOException {

        Student student = studentService.getStudentById(studentId);
        if (student != null && student.getPlaced() != null && file != null && !file.isEmpty()) {
            successStories.setStudent(student);
            String fileName = file.getOriginalFilename();
            successStories.setImage(fileName);
            // Upload the image to S3
            s3Service.uploadImageToS3(folderName,fileName, file);
            return successStoryRepository.save(successStories);
        } else {
            throw new IllegalStateException("Cannot create success story for non-placed student.");
        }
    }

    public List<SuccessStories> getAllSuccessStories() {
        return successStoryRepository.findAll();
    }


    public SuccessStories getSuccessStoryById(int ssId) throws InvalidStudentIdException {
        return successStoryRepository.findById(ssId).orElseThrow(() -> new InvalidStudentIdException("Invalid StudentId"));
    }

    public String deleteSuccessStoryById(int ssId) throws StudentNotFoundException {
        try {
            successStoryRepository.deleteById(ssId);

        } catch (Exception ex) {
            throw new StudentNotFoundException("invalid vendorId passed");
        }
        return "Student Successfully deleted" + ssId;
    }

    public SuccessStories updateSuccessStories(int studentId, SuccessStories stories,MultipartFile file) throws InvalidStudentIdException, IOException {
        SuccessStories existingX = successStoryRepository.findById(studentId)
                .orElseThrow(() -> new InvalidStudentIdException("Please enter a valid studentId"));


        if (stories.getTitle() !=null) {
            existingX.setTitle(stories.getTitle());
        }
        if (stories.getDescription() != null) {
            existingX.setDescription(stories.getDescription());
        }
        if(stories.getStudent() !=null) {
            existingX.setStudent(stories.getStudent());
        }
        if (file != null && !file.isEmpty()) {
            String oldImageName = stories.getImage();
            String newImageName = file.getOriginalFilename();
            existingX.setImage(newImageName);

            // Upload new image to S3
            s3Service.uploadImageToS3(folderName,newImageName,file);

            // Delete the old image file from S3
            if (oldImageName != null) {
                s3Service.deleteImageFromS3(oldImageName);
            }
    }
        return successStoryRepository.save(existingX);
    }
}
