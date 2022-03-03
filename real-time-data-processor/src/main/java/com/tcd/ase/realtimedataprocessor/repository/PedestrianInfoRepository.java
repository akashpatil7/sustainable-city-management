package com.tcd.ase.realtimedataprocessor.repository;

import com.tcd.ase.realtimedataprocessor.entity.PedestrianInfoDAO;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedestrianInfoRepository extends MongoRepository<PedestrianInfoDAO, Integer> {

    List<PedestrianInfoDAO> findAll();

}
