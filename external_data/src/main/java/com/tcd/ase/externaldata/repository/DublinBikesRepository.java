package com.tcd.ase.externaldata.repository;

import com.tcd.ase.externaldata.entity.DublinBikes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DublinBikesRepository extends MongoRepository<DublinBikes, Integer> {
}
