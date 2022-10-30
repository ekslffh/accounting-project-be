package com.example.hsap.service;

import com.example.hsap.model.CategoryEntity;
import com.example.hsap.model.HistoryEntity;
import com.example.hsap.model.MemberEntity;
import com.example.hsap.repository.CategoryRepository;
import com.example.hsap.repository.HistoryRepository;
import com.example.hsap.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final CategoryRepository categoryRepository;

    private final MemberRepository memberRepository;

    public List<HistoryEntity> create(HistoryEntity historyEntity) {
        validate(historyEntity);
        Optional<CategoryEntity> category = categoryRepository.findById(historyEntity.getCategory().getId());
        if (category.isPresent()) {
            historyEntity.setCategory(category.get());
            historyRepository.save(historyEntity);
            MemberEntity member = historyEntity.getMember();
            return memberRepository.save(member).getHistories();
        }
        throw new RuntimeException("This category is not exists");
    }

    public List<HistoryEntity> retrieveAll() {
        return historyRepository.findAll();
    }

    public List<HistoryEntity> deleteReceipt(String historyId) {
        HistoryEntity history = historyRepository.findById(historyId).orElseThrow(RuntimeException::new);
        history.setImagePath(new ArrayList<>());
        historyRepository.save(history);
        return history.getMember().getHistories();
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

            if (historyEntity.getImagePath() != null) history.setImagePath(historyEntity.getImagePath());

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
