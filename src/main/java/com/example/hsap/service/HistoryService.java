package com.example.hsap.service;

import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.repository.CategoryRepository;
import com.example.hsap.repository.HistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class HistoryService {
    @Autowired
    private HistoryRepository expenditureRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public List<HistoryEntity> create(HistoryEntity historyEntity) {
            validate(historyEntity);
            historyEntity.setCreatedAt(LocalDateTime.now());
            historyEntity.setUpdatedAt(LocalDateTime.now());
        Optional<CategoryEntity> category = categoryRepository.findById(historyEntity.getCategory().getId());
        if (category.isPresent()) {
            historyEntity.setCategory(category.get());
            expenditureRepository.save(historyEntity);
            return historyEntity.getMember().getExpenditures();
        }
        throw new RuntimeException("This category is not exists");
    }

    public List<HistoryEntity> retrieveAll() {
        return expenditureRepository.findAll();
    }

    public List<HistoryEntity> update(HistoryEntity historyEntity) {
        validate(historyEntity);
        Optional<HistoryEntity> original = expenditureRepository.findById(historyEntity.getId());
        original.ifPresent(history -> {
            history.setUseDate(historyEntity.getUseDate());
            history.setCategory(historyEntity.getCategory());
            history.setIncome(historyEntity.getIncome());
            history.setExpenditure(historyEntity.getExpenditure());
            history.setMemo(historyEntity.getMemo());
            history.setUpdatedAt(LocalDateTime.now());
            expenditureRepository.save(history);
        });
        return historyEntity.getMember().getExpenditures();
    }

    public List<HistoryEntity> delete(HistoryEntity expenditureEntity) {
        validate(expenditureEntity);
        expenditureRepository.delete(expenditureEntity);
        return expenditureEntity.getMember().getExpenditures();
    }

    public void validate(HistoryEntity entity) {
        if (entity == null) {
            log.warn("This history is null");
            throw new RuntimeException("This history is null");
        }
    }
}
