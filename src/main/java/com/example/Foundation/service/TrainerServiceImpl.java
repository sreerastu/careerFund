package com.example.Foundation.service;


import com.example.Foundation.exception.TrainerNotFoundException;
import com.example.Foundation.modal.Trainer;
import com.example.Foundation.repositories.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService, UserDetailsService {


    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static String UPLOADS_DIR = "./src/main/resources/static/uploads/";


    @Override
    public Trainer createTrainer(Trainer trainer, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            // Save image file to uploads directory
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            Path path = Paths.get(UPLOADS_DIR + fileName);
            Files.write(path, file.getBytes());
            trainer.setImage(fileName);
        }
        return trainerRepository.save(trainer);
    }

    @Override
    public Trainer updateTrainer(int trainerId, Trainer trainer) throws TrainerNotFoundException {

        Trainer existingTrainer = trainerRepository.findById(trainer.getTrainerId()).orElseThrow(() -> new TrainerNotFoundException("Invalid TrainerId"));
        existingTrainer.setFirstName(trainer.getFirstName());
        existingTrainer.setLastName(trainer.getLastName());
        existingTrainer.setContactNumber(trainer.getContactNumber());
        existingTrainer.setEmailAddress(trainer.getEmailAddress());
        existingTrainer.setPassword(this.bCryptPasswordEncoder.encode(trainer.getPassword()));
        existingTrainer.setCourse(trainer.getCourse());
        existingTrainer.setGender(trainer.getGender());
        if (trainer.getImage() != null) {
            existingTrainer.setImage(trainer.getImage());
        }

        return existingTrainer;
    }

    @Override
    public List<Trainer> getAllTrainers() {

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

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        Trainer trainer = trainerRepository.findByEmailAddress(emailAddress);
        if (trainer == null) {
            throw new UsernameNotFoundException("trainer not found with email address: " + emailAddress);
        }
        return new org.springframework.security.core.userdetails.User(trainer.getEmailAddress(), trainer.getPassword(), trainer.getAuthorities());
    }
}

