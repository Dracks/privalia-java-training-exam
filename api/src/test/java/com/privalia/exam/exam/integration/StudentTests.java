package com.privalia.exam.exam.integration;

import com.privalia.exam.controllers.StudentController;
import com.privalia.exam.domain.Address;
import com.privalia.exam.domain.Student;
import com.privalia.exam.repository.AddressRepository;
import com.privalia.exam.repository.StudentRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentTests {

    @Autowired
    public StudentRepository repository;

    @Autowired
    public AddressRepository addressRepository;

    @Autowired
    public StudentController subject;

    private Student student1;
    private Student student2;

    private Address address1;

    @Before
    public void setUp(){
        student1 = new Student("Jaume", "Singla");
        repository.save(student1);
        student2 = new Student("Dr", "Who");
        repository.save(student2);

        address1 = new Address("Skaro", "EXTERMINATE!");
        address1.setStudent(student2);
        addressRepository.save(address1);
    }

    @After
    public void tearDown(){
        addressRepository.deleteAll();
        repository.deleteAll();
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

    @Test
    public void createUser(){
        String street1="Testing... "+Math.random();
        String street2="c/Balmes "+Math.random();
        Student st1 = new Student("Test"+Math.random(), "Testing...");
        st1.getAddressList().add(new Address("Barcelona", street1));
        st1.getAddressList().add(new Address("Barcelona", street2));

        subject.create(st1);

        Iterable<Address> data = addressRepository.findAll();

        boolean containsFirst = false;
        boolean containsSecond = false;

        for (Address address: data){
            containsFirst = containsFirst || address.getStreet().equals(street1);
            containsSecond = containsSecond || address.getStreet().equals(street2);
        }

        assertTrue(containsFirst);
        assertTrue(containsSecond);
    }

    @Test
    public void createUserWithError(){
        Student st = new Student("Test", "Testing...");
        st.getAddressList().add(new Address("", "Sortint a la dreta"));

        boolean isOk = false;
        try {
            subject.create(st);
        } catch (Exception e){
            isOk = true;
        }

        assertTrue(isOk);
        assertNotEquals(0, st.getId());
        assertNull(addressRepository.findOne(st.getId()));
    }

    @Test
    public void updateUserOneField(){
        Student st = new Student();
        st.setName("Dalek!");
        Student newStudent = subject.update(student1.getId(),st);

        assertEquals("Dalek!", newStudent.getName());
        assertEquals(student1.getLastName(), newStudent.getLastName());
    }

    @Test
    public void updateUserAllFields(){
        Student st = new Student("Dalek", "Skaro");
        //Address addr1 = new Address("Pepito!", "nothing!");
        //st.setAddressList(Arrays.asList(addr1));

        Student newStudent = subject.update(student1.getId(), st);

        assertEquals("Dalek", newStudent.getName());
        assertEquals("Skaro", newStudent.getLastName());

        /*int count = 0;
        for (Address address: addressRepository.findAll()){
            if (address.getStudent().getId()==student1.getId()){
                count++;
            }
        }

        assertEquals(1, count);*/
    }

    public void deleteUser(){
        subject.destroy(student2.getId());
    }

}
