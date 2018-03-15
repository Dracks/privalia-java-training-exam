package com.privalia.exam.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ApiModelProperty(notes = "User name", required = true)
    @NotNull
    @Size(min = 2, max = 200)
    private String name;

    @ApiModelProperty(notes = "The product description")
    @Size(min = 2, max = 200)
    private String lastName;

    @OneToMany(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "student")
    private List<Address> addressList;

    public Student(String name, String lastName){
        this.name = name;
        this.lastName = lastName;
        this.addressList = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Student){
            Student st = (Student) o;
            return st.id == this.id;
        }
        return false;
    }
}
