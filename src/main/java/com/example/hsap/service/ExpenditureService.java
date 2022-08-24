package com.example.hsap.service;

import com.example.hsap.model.ExpenditureEntity;
import com.example.hsap.repository.ExpenditureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ExpenditureService {
    @Autowired
    private ExpenditureRepository expenditureRepository;

    // CRUD 구성
    // validate :

    // create
    public List<ExpenditureEntity> create(ExpenditureEntity expenditureEntity) {
        // validate
        validate(expenditureEntity);
        expenditureEntity.setCreatedAt(LocalDateTime.now());
        expenditureEntity.setUpdatedAt(LocalDateTime.now());
        expenditureRepository.save(expenditureEntity);
        return expenditureRepository.findByUserId(expenditureEntity.getUserId());
    }

    // read
    public List<ExpenditureEntity> retrieve(String user) {
        return expenditureRepository.findByUserId(user);
    }

    // update
    public List<ExpenditureEntity> update(ExpenditureEntity expenditureEntity) {
        validate(expenditureEntity);
        Optional<ExpenditureEntity> original = expenditureRepository.findById(expenditureEntity.getId());
        original.ifPresent(entity -> {
            entity.setUseDate(expenditureEntity.getUseDate());
            entity.setCategory(expenditureEntity.getCategory());
            entity.setInOrOut(expenditureEntity.getInOrOut());
            entity.setAmountOfMoney(expenditureEntity.getAmountOfMoney());
            entity.setMemo(expenditureEntity.getMemo());
            entity.setUpdatedAt(LocalDateTime.now());
            expenditureRepository.save(entity);
        });
        return expenditureRepository.findByUserId(expenditureEntity.getUserId());
    }

    // delete
    public List<ExpenditureEntity> delete(ExpenditureEntity expenditureEntity) {
        validate(expenditureEntity);
        expenditureRepository.delete(expenditureEntity);
        return expenditureRepository.findByUserId(expenditureEntity.getUserId());
    }

    public void validate(ExpenditureEntity expenditureEntity) {
        if (expenditureEntity == null || expenditureEntity.getUserId() == null) {
            throw new RuntimeException("ExpenditureEntity validate exception");
        }
    }

}
