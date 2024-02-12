package com.example.Foundation.service;

import com.example.Foundation.exception.InvalidStudentIdException;
import com.example.Foundation.exception.StudentNotFoundException;
import com.example.Foundation.modal.Student;
import com.example.Foundation.modal.SuccessStories;
import com.example.Foundation.repositories.SuccessStoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class SuccessStoryServiceImpl {

    @Autowired
    private S3Service s3Service; // Injecting the S3Service

    //   private static String UPLOADS_DIR = "./src/main/resources/static/uploads/";

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
            s3Service.uploadImageToS3(fileName, file);
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

    public SuccessStories updateSuccessStories(int studentId, Student student) throws InvalidStudentIdException {
        SuccessStories existingX = successStoryRepository.findById(student.getStudentId())
                .orElseThrow(() -> new InvalidStudentIdException("Please enter a valid studentId"));

        existingX.setStudent(student.getSuccessStories().getStudent());
        existingX.setDescription(student.getSuccessStories().getDescription());
        if (student.getImage() != null) {
            existingX.setImage(student.getImage());
        }
        return successStoryRepository.save(existingX);
    }
}
