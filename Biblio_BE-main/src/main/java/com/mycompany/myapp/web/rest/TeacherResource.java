package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Loaner;
import com.mycompany.myapp.service.TeacherService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;


@RestController
@RequestMapping("/api")
public class TeacherResource {

    private final Logger log = LoggerFactory.getLogger(TeacherResource.class);

    private static final String ENTITY_NAME = "teacher";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeacherService teacherService;


    public TeacherResource(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    /**
     * {@code POST  /teachers} : Create a new teacher.
     *
     * @param teacher the teacher to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new teacher, or with status {@code 400 (Bad Request)} if the teacher has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/teachers")
    public ResponseEntity<Loaner> createTeacher(@RequestBody Loaner teacher) throws URISyntaxException {
        log.debug("REST request to save Teacher : {}", teacher);
        if (teacher.getId() != null) {
            throw new BadRequestAlertException("A new teacher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Loaner result = teacherService.save(teacher);
        return ResponseEntity
            .created(new URI("/api/teachers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /teachers/:id} : Updates an existing teacher.
     *
     * @param id the id of the teacher to save.
     * @param teacher the teacher to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teacher,
     * or with status {@code 400 (Bad Request)} if the teacher is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teacher couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/teachers/{id}")
    public ResponseEntity<Loaner> updateTeacher(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Loaner teacher
    ) throws URISyntaxException {
        log.debug("REST request to update Teacher : {}, {}", id, teacher);
        if (teacher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teacher.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }


        Loaner result = teacherService.save(teacher);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, teacher.getId()))
            .body(result);
    }

    /**
     * {@code GET  /teachers} : get all the teachers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teachers in body.
     */
    @GetMapping("/teachers")
    public ResponseEntity<List<Loaner>> getAllTeachers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Teachers");
        Page<Loaner> page = teacherService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /teachers/:id} : get the "id" teacher.
     *
     * @param id the id of the teacher to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teacher, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/teachers/{id}")
    public ResponseEntity<Loaner> getTeacher(@PathVariable String id) {
        log.debug("REST request to get Teacher : {}", id);
        Optional<Loaner> teacher = teacherService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teacher);
    }

    /**
     * {@code DELETE  /teachers/:id} : delete the "id" teacher.
     *
     * @param id the id of the teacher to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable String id) {
        log.debug("REST request to delete Teacher : {}", id);
        teacherService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
