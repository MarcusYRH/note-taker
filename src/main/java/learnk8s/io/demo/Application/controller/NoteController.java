package learnk8s.io.demo.Application.controller;

import learnk8s.io.demo.Application.service.CreateNoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
public class NoteController {

  @Autowired CreateNoteService createNoteService;

  @GetMapping("/")
  public String index(Model model) {
    createNoteService.getAllNotes(model);
    log.info("Received inital request to start up page");
    return "index";
  }

  @GetMapping("/getSpecificNote")
  public String getSpecificNote(@RequestParam String description, Model model) {
    createNoteService.selectSpecificNote(description, model);
    log.info("Received request to pull specific note.");
    return "index";
  }

  @PostMapping("/note")
  public String saveNotes(@RequestParam("image") MultipartFile file,
                          @RequestParam String description,
                          @RequestParam(required = false) String publish,
                          @RequestParam(required = false) String upload,
                          Model model) throws Exception {

    if (publish != null && publish.equals("Publish")) {
      createNoteService.saveNote(description, model);
      createNoteService.getAllNotes(model);
      return "redirect:/";
    }
    if (upload != null && upload.equals("Upload")) {
      if (file != null && file.getOriginalFilename() != null &&
              !file.getOriginalFilename().isEmpty()) {
        createNoteService.uploadImage(file, description, model);
      }
      createNoteService.getAllNotes(model);
      return "index";
    }
    return "index";
  }
}
