package com.tcd.ase.realtimedataprocessor.repository;

import com.tcd.ase.realtimedataprocessor.entity.PedestrianDAO;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedestrianRepository extends MongoRepository<PedestrianDAO, Integer> {

    Optional<PedestrianDAO> findFirstByOrderByTimeDesc();

}
