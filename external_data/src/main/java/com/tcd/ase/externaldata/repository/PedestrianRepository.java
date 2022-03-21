package com.tcd.ase.externaldata.repository;



import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tcd.ase.externaldata.entity.PedestrianDAO;

import java.util.Optional;

@Repository
public interface PedestrianRepository extends MongoRepository<PedestrianDAO, Integer> {

    Optional<PedestrianDAO> findFirstByOrderByTimeDesc();

}
