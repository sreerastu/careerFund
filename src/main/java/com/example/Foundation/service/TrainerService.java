package com.example.Foundation.service;
import com.example.Foundation.exception.TrainerNotFoundException;
import com.example.Foundation.modal.Trainer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TrainerService {


    Trainer createTrainer(Trainer trainer, MultipartFile file) throws IOException;

    Trainer updateTrainer(int trainerId, Trainer trainer,MultipartFile file) throws TrainerNotFoundException, IOException;

    List<Trainer> getAllTrainers();

    Trainer getTrainerById(int trainerId) throws TrainerNotFoundException;

    String deleteTrainerById(int trainerId) throws TrainerNotFoundException;

    Trainer login(String emailAddress, String password);

    Trainer getTrainerByEmail(String emailAddress);

}
