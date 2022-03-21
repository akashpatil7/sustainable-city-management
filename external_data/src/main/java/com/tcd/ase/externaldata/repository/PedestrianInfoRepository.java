package com.tcd.ase.externaldata.repository;



import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tcd.ase.externaldata.entity.PedestrianInfoDAO;

import java.util.List;

@Repository
public interface PedestrianInfoRepository extends MongoRepository<PedestrianInfoDAO, Integer> {

    List<PedestrianInfoDAO> findAll();

}
