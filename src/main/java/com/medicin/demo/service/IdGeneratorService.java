package com.medicin.demo.service;

import com.medicin.demo.repository.MedecinRepository;
import com.medicin.demo.repository.PatientRepository;
import com.medicin.demo.repository.RdvRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdGeneratorService {

    private final PatientRepository patientRepository;
    private final MedecinRepository medecinRepository;
    private final RdvRepository rdvRepository;

    public IdGeneratorService(PatientRepository patientRepository,
                              MedecinRepository medecinRepository,
                              RdvRepository rdvRepository) {
        this.patientRepository = patientRepository;
        this.medecinRepository = medecinRepository;
        this.rdvRepository = rdvRepository;
    }

    public synchronized String nextPatientId() {
        return nextId("P", patientRepository.findIdsByPrefix("P"));
    }

    public synchronized String nextMedecinId() {
        return nextId("M", medecinRepository.findIdsByPrefix("M"));
    }

    public synchronized String nextRdvId() {
        return nextId("R", rdvRepository.findIdsByPrefix("R"));
    }

    private String nextId(String prefix, List<String> existingIds) {
        int max = 0;
        for (String id : existingIds) {
            if (id == null || !id.startsWith(prefix)) {
                continue;
            }
            String numericPart = id.substring(prefix.length());
            if (numericPart.isEmpty()) {
                continue;
            }
            try {
                int value = Integer.parseInt(numericPart);
                if (value > max) {
                    max = value;
                }
            } catch (NumberFormatException ignored) {
                // Ignore malformed IDs and keep generating from valid ones.
            }
        }
        return String.format("%s%03d", prefix, max + 1);
    }
}
