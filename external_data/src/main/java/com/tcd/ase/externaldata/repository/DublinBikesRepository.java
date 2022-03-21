package com.tcd.ase.externaldata.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tcd.ase.externaldata.entity.DublinBikeDAO;

import java.util.Optional;

@Repository
public interface DublinBikesRepository extends MongoRepository<DublinBikeDAO, Integer> {

    Optional<DublinBikeDAO> findFirstByOrderByHarvestTimeDesc();
}
