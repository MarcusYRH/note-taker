package learnk8s.io.demo.Application.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public interface NoteService {
  public void saveNote(String description, Model model);

  public void getAllNotes(Model model);
}
