package com.example.Foundation.controller;

import com.example.Foundation.exception.TrainerNotFoundException;
import com.example.Foundation.modal.Trainer;
import com.example.Foundation.service.TrainerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api")
public class TrainerController {


    @Autowired
    private TrainerServiceImpl trainerService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @PostMapping("/register/trainer")
    public ResponseEntity<?> createTrainer(@Valid @RequestBody Trainer trainer) {
        trainer.setPassword(this.bCryptPasswordEncoder.encode(trainer.getPassword()));
        Trainer createdTrainer = trainerService.createTrainer(trainer);
        return ResponseEntity.status(HttpStatus.OK).body(createdTrainer);
    }


    @PutMapping("/trainer/{trainerId}")
    public ResponseEntity<?> updateVendor(@PathVariable int trainerId,
                                          @RequestBody Trainer trainerX) throws TrainerNotFoundException {
        Trainer trainer = trainerService.updateTrainer(trainerId, trainerX);
        return ResponseEntity.status(HttpStatus.OK).body(trainer);
    }

    @GetMapping("/trainers")
    public ResponseEntity<?> getAllTrainers() {
        List<Trainer> trainers = trainerService.getAllTrainers();
        return ResponseEntity.status(HttpStatus.OK).body(trainers);
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<?> getTrainerById(@PathVariable int trainerId) throws TrainerNotFoundException {
        Trainer trainer = trainerService.getTrainerById(trainerId);
        return ResponseEntity.status(HttpStatus.OK).body(trainer);
    }

    @DeleteMapping("/trainer/{trainerId}")
    public ResponseEntity<?> deleteTrainerById(@PathVariable int trainerId) throws TrainerNotFoundException {
        trainerService.deleteTrainerById(trainerId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
