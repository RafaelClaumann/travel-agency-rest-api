package org.claumann.travelagency.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Destination {

    private Long id;
    private String name;
    private String location;
    private String description;
    private Double averageRating;
    private Integer totalRatings;

}
