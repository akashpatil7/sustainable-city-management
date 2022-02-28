package com.tcd.ase.realtimedataprocessor.repository;

import com.tcd.ase.realtimedataprocessor.entity.AqiDAO;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AqiRepository extends MongoRepository<AqiDAO, Integer> {

    Optional<AqiDAO> findFirstByOrderByLastUpdatedTimeDesc();

}
