package com.medicin.demo.controller;

import com.medicin.demo.model.Patient;
import com.medicin.demo.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/liste")
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @PostMapping("/inscription")
    public Patient createPatient(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }
}
