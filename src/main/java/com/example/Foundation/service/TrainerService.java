package com.example.Foundation.service;
import com.example.Foundation.exception.TrainerNotFoundException;
import com.example.Foundation.modal.Trainer;

import java.util.List;

public interface TrainerService {


    Trainer createTrainer(Trainer trainer);

    Trainer updateTrainer(int trainerId, Trainer trainer) throws TrainerNotFoundException;

    List<Trainer> getAllTrainers();

    Trainer getTrainerById(int trainerId) throws TrainerNotFoundException;

    String deleteTrainerById(int trainerId) throws TrainerNotFoundException;

    Trainer login(String emailAddress, String password);

    Trainer getTrainerByEmail(String emailAddress);

}
