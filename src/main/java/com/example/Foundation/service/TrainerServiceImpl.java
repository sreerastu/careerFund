package com.example.Foundation.service;


import com.example.Foundation.exception.TrainerNotFoundException;
import com.example.Foundation.modal.Trainer;
import com.example.Foundation.repositories.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainerServiceImpl implements TrainerService, UserDetailsService {

    @Autowired
    private S3Service s3Service; // Injecting the S3Service


    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Value("${aws.s3.TrainerFolder}")
    private String folderName;

    @Override
    public Trainer createTrainer(Trainer trainer, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String filename = file.getOriginalFilename();
            trainer.setImage(filename);
            // Upload the image to S3
            s3Service.uploadImageToS3(folderName, filename,file);
        }
        return trainerRepository.save(trainer);
    }

    @Override
    public Trainer updateTrainer(int trainerId, Trainer trainer, MultipartFile file) throws TrainerNotFoundException, IOException {

        Trainer existingTrainer = trainerRepository.findById(trainer.getTrainerId()).orElseThrow(() -> new TrainerNotFoundException("Invalid TrainerId"));
        if (trainer.getFirstName() != null) {
            existingTrainer.setFirstName(trainer.getFirstName());
        }
        if (trainer.getLastName() != null) {
            existingTrainer.setLastName(trainer.getLastName());
        }
        if (trainer.getGender() != null) {
            existingTrainer.setGender(trainer.getGender());
        }
        if (trainer.getCourse() != null) {
            existingTrainer.setCourse(trainer.getCourse());
        }
        if (trainer.getEmailAddress() != null) {
            existingTrainer.setEmailAddress(trainer.getEmailAddress());
        }
        if (trainer.getPassword() != null) {
            existingTrainer.setPassword(this.bCryptPasswordEncoder.encode(trainer.getPassword()));
        }
        if (trainer.getContactNumber() != null) {
            existingTrainer.setContactNumber(trainer.getContactNumber());
        }
        if(trainer.getUserType() !=null){
            existingTrainer.setUserType(trainer.getUserType());
        }

        if(trainer.getCertification() !=null){
            existingTrainer.setCertification(trainer.getCertification());
        }
        if (file != null && !file.isEmpty()) {
            String oldImageName = trainer.getImage();
            String newImageName = file.getOriginalFilename();
            existingTrainer.setImage(newImageName);

            // Upload new image to S3
            s3Service.uploadImageToS3(newImageName, newImageName,file);

            // Delete the old image file from S3
            if (oldImageName != null) {
                s3Service.deleteImageFromS3(oldImageName);
            }
        }
        return existingTrainer;
    }

    @Override
    public List<Trainer> getAllTrainers() {
        List<Trainer> trainers = trainerRepository.findAll();
        return trainers;
    }

    public List<Trainer> getTrainers() {
        List<Trainer> trainers = trainerRepository.findAll();
        return trainers;
    }

    @Override
    public Trainer getTrainerById(int trainerId) throws TrainerNotFoundException {
        return trainerRepository.findById(trainerId).orElseThrow(() -> new TrainerNotFoundException("invalid trainerId"));
    }

    @Override
    public String deleteTrainerById(int trainerId) throws TrainerNotFoundException {
        try {
            trainerRepository.deleteById(trainerId);

        } catch (Exception ex) {
            throw new TrainerNotFoundException("invalid trainerId passed");
        }
        return "Trainer Successfully deleted" + trainerId;
    }

    @Override
    public Trainer login(String emailAddress, String password) {
        return trainerRepository.findByEmailAddressAndPassword(emailAddress, password);
    }

    @Override
    public Trainer getTrainerByEmail(String emailAddress) {
        return trainerRepository.findByEmailAddress(emailAddress);
    }



    public List<Trainer> getAllTopTrainers() {
        List<Trainer> list = trainerRepository.findAll();
        List<Trainer> sortedList = list.stream()
                .sorted() // Sort by Payment amount in descending order
                .limit(10) // Limit to the top 10 payments
                .collect(Collectors.toList());
        return sortedList;
    }

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        Trainer trainer = trainerRepository.findByEmailAddress(emailAddress);
        if (trainer == null) {
            throw new UsernameNotFoundException("trainer not found with email address: " + emailAddress);
        }
        return new org.springframework.security.core.userdetails.User(trainer.getEmailAddress(), trainer.getPassword(), trainer.getAuthorities());
    }
}

