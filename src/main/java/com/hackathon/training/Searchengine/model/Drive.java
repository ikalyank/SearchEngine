package com.hackathon.training.Searchengine.model;

import java.io.File;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// @Entity
public class Drive {
    private File drive;
    private String desc;
}
