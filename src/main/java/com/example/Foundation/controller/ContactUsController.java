package com.example.Foundation.controller;

import com.example.Foundation.modal.ContactUs;
import com.example.Foundation.service.ContactUsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/contactus")
public class ContactUsController {

    @Autowired
    private ContactUsServiceImpl contactUsService;

    @PostMapping("/add")
    public ContactUs addContactUs(@RequestBody ContactUs contactUs) {
        return contactUsService.saveContactUs(contactUs);
    }

    @GetMapping("/all")
    public List<ContactUs> getAllContactUsMessages() {
        return contactUsService.getAllContactUsMessages();
    }

    @GetMapping("/{id}")
    public ContactUs getContactUsById(@PathVariable int id) {
        return contactUsService.getContactUsById(id);
    }

    @PutMapping("/update/{id}")
    public ContactUs updateContactUs(@PathVariable int id, @RequestBody ContactUs contactUsDetails) {
        return contactUsService.updateContactUs(id, contactUsDetails);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteContactUs(@PathVariable int id) {
        contactUsService.deleteContactUs(id);
    }
}
