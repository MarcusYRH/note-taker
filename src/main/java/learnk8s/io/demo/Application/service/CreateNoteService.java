package learnk8s.io.demo.Application.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import learnk8s.io.demo.Application.KnoteProperties;
import learnk8s.io.demo.Application.models.Note;
import learnk8s.io.demo.Application.repo.NotesRepository;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Transactional
@Service
public class CreateNoteService implements NoteService {

  @Autowired NotesRepository notesRepository;
  @Autowired
  private KnoteProperties properties;
  Parser parser = Parser.builder().build();
  HtmlRenderer renderer = HtmlRenderer.builder().build();

  @Override
  public void saveNote(String description, Model model) {
    if (description != null && !description.trim().isEmpty()) {
      // You need to translate markup to HTML
      Node document = parser.parse(description.trim());
      String html = renderer.render(document);
      notesRepository.save(new Note(null, html));
      // After publish you need to clean up the textarea
      model.addAttribute("description", "");
    }
  }

  @Override
  public void getAllNotes(Model model) {
    List<Note> notes = notesRepository.findAll();
    Collections.reverse(notes);
    model.addAttribute("notes", notes);
  }

  public void selectSpecificNote(String description, Model model) {
    List<Note> notes = new ArrayList<>();
    // Format given description before passing to repo layer to query
    String appendingHtmlStart = "<p>";
    String appendingHtmlEnd = "</p>\n";
    String descriptionForQuery = appendingHtmlStart + description + appendingHtmlEnd;
    log.info(descriptionForQuery);
    List<Note> noteFound = notesRepository.findAllByDescription(descriptionForQuery);
    if(noteFound == null) {
      log.error("NO NOTE FOUND!");
    }
    notes.add(noteFound.get(0));
    model.addAttribute("notes", notes);
  }

  public void uploadImage(MultipartFile file, String description, Model model) throws Exception {
    File uploadsDir = new File(properties.getUploadDir());
    if (!uploadsDir.exists()) {
      uploadsDir.mkdir();
    }
    String fileId = UUID.randomUUID().toString() + "." +
            file.getOriginalFilename().split("\\.")[1];
    file.transferTo(new File(properties.getUploadDir() + fileId));
    model.addAttribute("description",
            description + " ![](/uploads/" + fileId + ")");
  }
}
