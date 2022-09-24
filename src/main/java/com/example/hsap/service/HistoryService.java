package com.example.hsap.service;

import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.repository.CategoryRepository;
import com.example.hsap.repository.HistoryRepository;
import com.example.hsap.repository.MemberRepository;
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
    private HistoryRepository historyRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MemberRepository memberRepository;

    public List<HistoryEntity> create(HistoryEntity historyEntity) {
            validate(historyEntity);
            historyEntity.setCreatedAt(LocalDateTime.now());
            historyEntity.setUpdatedAt(LocalDateTime.now());
        Optional<CategoryEntity> category = categoryRepository.findById(historyEntity.getCategory().getId());
        if (category.isPresent()) {
            historyEntity.setCategory(category.get());
            historyRepository.save(historyEntity);
            return historyEntity.getMember().getHistories();
        }
        throw new RuntimeException("This category is not exists");
    }

    public List<HistoryEntity> retrieveAll() {
        return historyRepository.findAll();
    }

    public List<HistoryEntity> update(HistoryEntity historyEntity) {
        validate(historyEntity);
        Optional<HistoryEntity> original = historyRepository.findById(historyEntity.getId());
        if (original.isPresent()) {
            HistoryEntity history = original.get();
            if (historyEntity.getUseDate() != null) history.setUseDate(historyEntity.getUseDate());
            Optional<CategoryEntity> category = categoryRepository.findById(historyEntity.getCategory().getId());
            category.ifPresent(history::setCategory);
            history.setIncome(historyEntity.getIncome());
            history.setExpenditure(historyEntity.getExpenditure());
            if (historyEntity.getMemo() != null) history.setMemo(historyEntity.getMemo());
            history.setUpdatedAt(LocalDateTime.now());
            historyRepository.save(history);
        }
        return historyEntity.getMember().getHistories();
    }

    public void delete(HistoryEntity historyEntity) {
        validate(historyEntity);
        Optional<HistoryEntity> history = historyRepository.findById(historyEntity.getId());
        history.ifPresent(entity -> historyRepository.deleteById(entity.getId()));
    }

    public void validate(HistoryEntity entity) {
        if (entity == null) {
            log.warn("This history is null");
            throw new RuntimeException("This history is null");
        }
    }
}
