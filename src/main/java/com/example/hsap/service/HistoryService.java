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

    public List<HistoryEntity> create(HistoryEntity historyEntity, String year) {
        validate(historyEntity);
        Optional<CategoryEntity> category = categoryRepository.findById(historyEntity.getCategory().getId());
        if (category.isPresent()) {
            historyEntity.setCategory(category.get());
            historyRepository.save(historyEntity);
            MemberEntity member = historyEntity.getMember();
            List<HistoryEntity> historyEntities = memberRepository.save(member).getHistories();
            return historyEntities.stream().filter(history -> history.getUseDate().getYear() == Integer.parseInt(year)).toList();
        }
        throw new RuntimeException("This category is not exists");
    }

    public List<HistoryEntity> retrieveAll() {
        return historyRepository.findAll();
    }

    public List<HistoryEntity> deleteReceipt(String historyId, String year) {
        HistoryEntity history = historyRepository.findById(historyId).orElseThrow(RuntimeException::new);
        history.setImagePath(new ArrayList<>());
        historyRepository.save(history);
        List<HistoryEntity> historyEntities = history.getMember().getHistories();
        return historyEntities.stream().filter(historyEntity -> historyEntity.getUseDate().getYear() == Integer.parseInt(year)).toList();
    }

    // 수정 내역: 사용일, 마테고리, 수입 or 지출, 비고, 경로, 수정일 업데이트
    public List<HistoryEntity> update(HistoryEntity historyEntity, String year) {
        validate(historyEntity);
        Optional<HistoryEntity> optionalHistory = historyRepository.findById(historyEntity.getId());
        if (optionalHistory.isPresent()) {
            HistoryEntity originalHistory = optionalHistory.get();
            if (historyEntity.getUseDate() != null) originalHistory.setUseDate(historyEntity.getUseDate());
            Optional<CategoryEntity> category = categoryRepository.findById(historyEntity.getCategory().getId());
            category.ifPresent(originalHistory::setCategory);
            originalHistory.setIncome(historyEntity.getIncome());
            originalHistory.setExpenditure(historyEntity.getExpenditure());
            if (historyEntity.getMemo() != null) originalHistory.setMemo(historyEntity.getMemo());

            if (historyEntity.getImagePath().size() != 0) originalHistory.setImagePath(historyEntity.getImagePath());

            originalHistory.setUpdatedAt(LocalDateTime.now());
            historyRepository.save(originalHistory);
        }

        List<HistoryEntity> historyEntities = historyEntity.getMember().getHistories();
        return historyEntities.stream().filter(history -> history.getUseDate().getYear() == Integer.parseInt(year)).toList();

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
