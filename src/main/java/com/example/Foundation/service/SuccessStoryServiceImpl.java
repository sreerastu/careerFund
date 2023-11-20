package com.example.Foundation.service;

import com.example.Foundation.exception.InvalidStudentIdException;
import com.example.Foundation.exception.StudentNotFoundException;
import com.example.Foundation.modal.Student;
import com.example.Foundation.modal.SuccessStories;
import com.example.Foundation.repositories.SuccessStoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuccessStoryServiceImpl {

    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private SuccessStoryRepository successStoryRepository;

    public SuccessStories createSuccessStory(SuccessStories successStories, int studentId) throws InvalidStudentIdException {

        Student student = studentService.getStudentById(studentId);
        if (student != null && student.getPlaced() != null && student.getPlaced() && student.getSuccessStories() == null) {
            successStories.setStudent(student);
            return successStoryRepository.save(successStories);
        } else {
            throw new IllegalStateException("Cannot create success story for non-placed student or student already has a success story.");
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
}
