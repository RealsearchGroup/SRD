package edu.sjsu.cmpe295b.repository;

import edu.sjsu.cmpe295b.model.Classification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClassificationRepository extends CrudRepository<Classification,Integer> {
    List<Classification> findAll();
}
