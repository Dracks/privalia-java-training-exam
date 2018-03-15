package com.privalia.exam.exam.integration;

import com.privalia.exam.controllers.StudentController;
import com.privalia.exam.domain.Student;
import com.privalia.exam.repository.StudentRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentTests {

    @Autowired
    public StudentRepository repository;

    @Autowired
    public StudentController subject;

    private Student student1;
    private Student student2;

    @Before
    public void setUp(){
        student1 = new Student("Jaume", "Singla");
        repository.save(student1);
        student2 = new Student("Dr", "Who");
        repository.save(student2);
    }

    @After
    public void tearDown(){
        repository.delete(student1);
        repository.delete(student2);
    }


    @Test
    public void getList(){
        List<Student> data = subject.list();
        assertNotNull(data);
        assertEquals(2, data.size());
        assertTrue(data.indexOf(student1)>=0);
    }

    @Test
    public void getSecond(){
        Student student = subject.get(student2.getId());
        assertNotNull(student);
        assertEquals(student2, student);
    }

}
