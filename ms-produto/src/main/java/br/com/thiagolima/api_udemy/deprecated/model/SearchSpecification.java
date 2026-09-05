package br.com.thiagolima.api_udemy.deprecated.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Deprecated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchSpecification {
    private String columnName;
    private String value;
    private String operation;
}
