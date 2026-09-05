package br.com.thiagolima.api_udemy.deprecated.shared;

import br.com.thiagolima.api_udemy.deprecated.model.SearchSpecification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Deprecated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    List<SearchSpecification> specificationContextList;
    String overallOperation;
}
