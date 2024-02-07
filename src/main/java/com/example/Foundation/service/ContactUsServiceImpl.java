package com.example.Foundation.service;

import com.example.Foundation.modal.ContactUs;
import com.example.Foundation.repositories.ContactUsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactUsServiceImpl {

    @Autowired
    private ContactUsRepository contactUsRepository;

    public ContactUs saveContactUs(ContactUs contactUs) {
        return contactUsRepository.save(contactUs);
    }

    public List<ContactUs> getAllContactUsMessages() {
        return contactUsRepository.findAll();
    }

    public ContactUs getContactUsById(int id) {
        return contactUsRepository.findById(id).orElse(null);
    }

    public ContactUs updateContactUs(int id, ContactUs contactUsDetails) {
        ContactUs contactUs = contactUsRepository.findById(id).orElse(null);
        if (contactUs != null) {
            contactUs.setFirstName(contactUsDetails.getFirstName());
            contactUs.setLastName(contactUsDetails.getLastName());
            contactUs.setContactNumber(contactUsDetails.getContactNumber());
            contactUs.setEmailAddress(contactUsDetails.getEmailAddress());
            contactUs.setDescription(contactUsDetails.getDescription());
            contactUs.setPurpose(contactUsDetails.getPurpose());
            return contactUsRepository.save(contactUs);
        }
        return null;
    }

    public void deleteContactUs(int id) {
        contactUsRepository.deleteById(id);
    }
}
