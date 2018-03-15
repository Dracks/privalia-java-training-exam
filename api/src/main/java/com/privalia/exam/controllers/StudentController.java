package com.privalia.exam.controllers;

import com.privalia.exam.domain.Student;
import com.privalia.exam.repository.StudentRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/student")
@Api(value="Student endpoints")
public class StudentController {

    @Autowired
    private StudentRepository repository;

    @RequestMapping(value="/", produces="application/json")
    public List<Student> list(){
        Iterable<Student> data = repository.findAll();
        List<Student> list = new ArrayList<>();
        data.forEach(list::add);
        return list;
    }

    @RequestMapping(value="/{id}", produces="application/json")
    public Student get(@PathVariable Integer id) {
        return repository.findOne(id);
    }

    @RequestMapping(value="/", method = RequestMethod.POST, produces="application/json")
    @Transactional
    public Student create(@RequestBody Student student){
        student.getAddressList().forEach(address -> {
            address.setStudent(student);
        });
        repository.save(student);
        return student;
    }





}
