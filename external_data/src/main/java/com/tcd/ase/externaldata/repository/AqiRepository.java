package com.tcd.ase.externaldata.repository;



import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tcd.ase.externaldata.entity.AqiDAO;

import java.util.Optional;

@Repository
public interface AqiRepository extends MongoRepository<AqiDAO, Integer> {

    Optional<AqiDAO> findFirstByOrderByLastUpdatedTimeDesc();

}
