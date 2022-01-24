package com.tcd.ase.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tcd.ase.userservice.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

}
