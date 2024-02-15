package com.example.Foundation.service;

import com.example.Foundation.modal.Clients;
import com.example.Foundation.repositories.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ClientsServiceImpl {

    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private S3Service s3Service; // Injecting the S3Service

    @Value("${aws.s3.ClientsFolder}")
    private String folderName;

    public List<Clients> getAllClients() {
        return clientsRepository.findAll();
    }

    public Clients getClientById(int id) {
        return clientsRepository.findById(id).orElse(null);
    }

    public Clients saveClient(Clients client, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            client.setImage(fileName);
            // Upload the image to S3
            s3Service.uploadImageToS3(folderName,fileName, file);
        }
        return clientsRepository.save(client);
    }

    public Clients partialUpdateClient(int id, Clients client, MultipartFile file) throws IOException {
        Optional<Clients> existingClientOptional = clientsRepository.findById(id);
        if (existingClientOptional.isPresent()) {
            Clients existingClient = existingClientOptional.get();

            // Merge changes from the provided client object to the existing client object
            if (client.getName() != null) {
                existingClient.setName(client.getName());
            }
            if (client.getEmailAddress() != null) {
                existingClient.setEmailAddress(client.getEmailAddress());
            }
            if (client.getContactNumber() != null) {
                existingClient.setContactNumber(client.getContactNumber());
            }
            if (client.getDescription() != null) {
                existingClient.setDescription(client.getDescription());
            }

            // Handle image update if provided
            if (file != null && !file.isEmpty()) {
                String oldImageName = existingClient.getImage();
                String newImageName = file.getOriginalFilename();
                existingClient.setImage(newImageName);

                // Upload new image to S3
                s3Service.uploadImageToS3(folderName,newImageName, file);

                // Delete the old image file from S3
                if (oldImageName != null) {
                    s3Service.deleteImageFromS3(oldImageName);
                }
            }

            return clientsRepository.save(existingClient);
        }
        return null; // or throw an exception indicating resource not found
    }

    public void deleteClient(int id) {
        clientsRepository.deleteById(id);
    }

    public Resource loadImageAsResource(String imageName) throws IOException {
        return s3Service.loadImageAsResource(imageName); // Delegating to S3Service
    }
}

