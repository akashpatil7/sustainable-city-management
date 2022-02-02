package com.tcd.ase.trendsengine.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tcd.ase.trendsengine.entity.Trends;

@Repository
public interface TrendsRepository extends MongoRepository<Trends, String> {

}
