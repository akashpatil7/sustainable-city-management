package com.tcd.ase.realtimedataprocessor.repository;

import com.tcd.ase.realtimedataprocessor.entity.PedestrianInfoDAO;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedestrianInfoRepository extends MongoRepository<PedestrianInfoDAO, Integer> {

    Optional<PedestrianInfoDAO> findFirstByOrderByTimeDesc();

}
