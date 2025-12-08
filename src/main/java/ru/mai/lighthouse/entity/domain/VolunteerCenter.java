package ru.mai.lighthouse.entity.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VolunteerCenter {
    private Long centerId;
    private String title;
    private String city;
    private String postCode;
    private String address;
}

