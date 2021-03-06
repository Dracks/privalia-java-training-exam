package com.privalia.exam.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ApiModelProperty(notes="City", required=true)
    @NotNull
    @Size(min = 2, max = 200)
    private String city;

    @ApiModelProperty(notes = "Street", required = true)
    private String street;


    @ManyToOne()
    @JoinColumn(name="STUDENT_ID", nullable=false)
    private Student student;

    public Address(String city, String street){
        this.city = city;
        this.street = street;
    }
}
