package com.hackathon.training.Searchengine.model;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPermission {
    private String drives;
    private boolean canDeleteSearchRecord;
}
