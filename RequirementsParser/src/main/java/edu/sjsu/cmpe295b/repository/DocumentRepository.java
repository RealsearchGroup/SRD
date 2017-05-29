package edu.sjsu.cmpe295b.repository;

import edu.sjsu.cmpe295b.model.Document;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface DocumentRepository extends CrudRepository<Document,Integer> {
    List<Document> findAll();

    List<Document> findAllByCreateUser(String createUser);

    List<Document> findAllByCreateUserNot(String createUser);
}
