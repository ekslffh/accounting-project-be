package com.example.hsap.service;

import com.example.hsap.model.DepartmentEntity;
import com.example.hsap.model.PeopleEntity;
import com.example.hsap.repository.DepartmentRepository;
import com.example.hsap.repository.PeopleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PeopleService {

    private final PeopleRepository peopleRepository;

    private final DepartmentRepository departmentRepository;

    public static List<PeopleEntity> sortingPeople(List<PeopleEntity> peopleEntities) {
        peopleEntities.sort(Comparator
                .comparing(PeopleEntity::getStatusNumber)
                .thenComparing(PeopleEntity::getName)
        );
        return peopleEntities;
    }
    // create
    public List<PeopleEntity> create(PeopleEntity peopleEntity) {
        valid(peopleEntity);
        PeopleEntity savedPeople = peopleRepository.save(peopleEntity);
        return sortingPeople(savedPeople.getDepartment().getPeoples());
    }
    // read
    public List<PeopleEntity> readAll() {
        return peopleRepository.findAll();
    }

    public List<PeopleEntity> readByDepartment(String name) {
        DepartmentEntity foundDepartment = departmentRepository.findByName(name);
        if (foundDepartment == null) {
            throw new RuntimeException("해당 부서가 존재하지 않습니다.");
        }
        return sortingPeople(foundDepartment.getPeoples());
    }

    // update
    public List<PeopleEntity> update(PeopleEntity peopleEntity) {
        valid(peopleEntity);
        if (peopleEntity.getId() == null) {
            throw new RuntimeException("People update 오류 : id가 존재하지 않습니다.");
        }
        Optional<PeopleEntity> optionalPeopleEntity = peopleRepository.findById(peopleEntity.getId());
        if (optionalPeopleEntity.isPresent()) {
            PeopleEntity original = optionalPeopleEntity.get();
            original.setName(peopleEntity.getName());
            original.setBirth(peopleEntity.getBirth());
            original.setPhoneNumber(peopleEntity.getPhoneNumber());
            original.setGender(peopleEntity.getGender());
            original.setStatus(peopleEntity.getStatus());
            original.setMemo(peopleEntity.getMemo());
            return sortingPeople(peopleRepository.save(original).getDepartment().getPeoples());
        }
        else throw new RuntimeException("해당 객체가 존재하지 않습니다.");
    }

    // delete
    public List<PeopleEntity> delete(String id, String department) {
            peopleRepository.deleteById(id);
            DepartmentEntity departmentEntity = departmentRepository.findByName(department);
            return sortingPeople(departmentEntity.getPeoples());
    }

    // valid
    public void valid(PeopleEntity peopleEntity) {
        if (peopleEntity == null) {
            log.error("해당 peopleEntity 유효성 검사 실패");
            throw new RuntimeException("해당 peopleEntity 유효성 검사 실패");
        }
    }

}
