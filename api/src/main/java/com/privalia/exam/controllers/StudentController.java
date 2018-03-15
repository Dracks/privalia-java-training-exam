package com.privalia.exam.controllers;

import com.privalia.exam.domain.Address;
import com.privalia.exam.domain.Student;
import com.privalia.exam.repository.AddressRepository;
import com.privalia.exam.repository.StudentRepository;
import io.swagger.annotations.Api;
import org.hibernate.Hibernate;
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

    @Autowired
    private AddressRepository addressRepository;

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

    @RequestMapping(value="/{id}", method = RequestMethod.PUT, produces="application/json")
    @Transactional
    public Student update(@PathVariable int id,@RequestBody Student student) {
        Student st = repository.findOne(id);
        if (student.getName()!=null){
            st.setName(student.getName());
        }

        if (student.getLastName()!=null){
            st.setLastName(student.getLastName());
        }

        if (student.getAddressList()!=null){
            Hibernate.initialize(st.getAddressList());
            List<Address> oldAddressList = st.getAddressList();
            oldAddressList.forEach(address -> addressRepository.delete(address));
            List<Address> newList = student.getAddressList();
            newList.forEach(address -> {
                address.setStudent(st);
                addressRepository.save(address);
            } );
            st.setAddressList(newList);

        }
        repository.save(st);
        return st;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE, produces="application/json")
    @Transactional
    public void destroy(int id) {
        Student st = repository.findOne(id);
        Hibernate.initialize(st.getAddressList());
        List<Address> addressList = st.getAddressList();
        addressList.forEach(address -> addressRepository.delete(address));
        repository.delete(st);
    }
}
