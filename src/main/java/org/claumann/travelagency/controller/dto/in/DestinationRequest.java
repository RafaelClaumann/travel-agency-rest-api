package org.claumann.travelagency.controller.dto.in;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DestinationRequest {

    private String name;
    private String location;
    private String description;

}
