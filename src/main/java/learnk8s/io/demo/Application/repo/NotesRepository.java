package learnk8s.io.demo.Application.repo;

import learnk8s.io.demo.Application.models.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepository extends MongoRepository<Note, String> {
    List<Note> findAllByDescription(String description);
}
