package learnk8s.io.demo.Application.repo;

import learnk8s.io.demo.Application.MainJavaApplication;
import learnk8s.io.demo.Application.models.Note;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainJavaApplication.class)
public class NotesRepositoryTest {
    @Autowired NotesRepository notesRepository;
    Parser parser = Parser.builder().build();
    HtmlRenderer renderer = HtmlRenderer.builder().build();

    @Before
    public void setUp() {
        String description = "THIS IS A TEST BOYS";
        Node document = parser.parse(description.trim());
        String html = renderer.render(document);
        try {
            notesRepository.save(new Note(null, html));
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
            log.error(e.getMessage());
        }

    }

    @Test
    public void Given_NoteSaved_When_findAllByDescription_Then_ReturnsCorrectNote() {
        // Given
        List<Note> noteList = notesRepository.findAll();
        Note note = noteList.get(33);
        String descriptionNote = note.getDescription();
        log.info(descriptionNote);
        // When
        try {
            String appendingHtmlStart = "<p>";
            String appendingHtmlEnd = "</p>\n";
            String description = appendingHtmlStart.concat("THIS IS A TEST BOYS").concat(appendingHtmlEnd);
            log.info(description);
            List<Note> matchingNote = notesRepository.findAllByDescription(description);
            // Then
            assertThat(matchingNote).isNotNull();
        } catch (Exception e) {
            log.info(e.getStackTrace().toString());
            log.error(e.getMessage());
        }
    }
}