package com.banking.online_banking.assistance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Service
public class IdealResponse {

    /*
    When the IdealResponse object is returned to the user, they will see the String representation of the
    ResponseStatus enum. The reason is that Spring Boot's default JSON serialization mechanism will use the
    toString() method of the enum to convert it to a String.
     */

    private ResponseStatus status;
    private Object data;
    private Object error;
}
