package com.tcd.ase.realtimedataprocessor.repository;

import com.tcd.ase.realtimedataprocessor.entity.DublinBikeDAO;
import com.tcd.ase.realtimedataprocessor.models.DublinBike;
import com.tcd.ase.realtimedataprocessor.models.DublinBikes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DublinBikesRepository extends MongoRepository<DublinBikeDAO, Integer> {

    Optional<DublinBikeDAO> findFirstByOrderByHarvestTimeDesc();
}
