package com.bzaja.ljudskiresursiiposlovniprocesilibrary.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class EntitySearchSpecification<T> implements Specification<T> {

    private List<SearchCriteriaDto> searchCriterias;
    private Operator operator;

    public EntitySearchSpecification(List<SearchCriteriaDto> searchCriterias, Operator operator) {
        this.searchCriterias = searchCriterias;
        this.operator = operator;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        searchCriterias = searchCriterias.stream().filter(x -> x.getSearchCriteriaType() != null && x.getColumn() != null && x.getSearchOperation() != null && x.getValue() != null).collect(Collectors.toList());

        List<Predicate> predicates = new ArrayList<>();
        Expression property = null;
        Predicate predicate = null;

        for (SearchCriteriaDto searchCriteria : searchCriterias) {
            property = getProperty(root, searchCriteria.getColumn(), searchCriteria.getSearchOperation());
            addPredicateBySearchOperation(searchCriteria, predicates, cb, property, predicate);
        }

        return buildPredicates(cb, predicates);
    }

    private void addPredicateBySearchOperation(SearchCriteriaDto searchCriteria, List<Predicate> predicates, CriteriaBuilder cb, Expression property, Predicate predicate) throws IllegalArgumentException {
        switch (searchCriteria.getSearchOperation()) {
            case LESS_THAN:
                predicate = cb.lessThan(property, searchCriteria.getValue().toString());
                addPredicate(predicates, predicate, searchCriteria.getSearchCriteriaType());
                break;
            case LESS_THAN_OR_EQUAL_TO:
                predicate = cb.lessThanOrEqualTo(property, searchCriteria.getValue().toString());
                addPredicate(predicates, predicate, searchCriteria.getSearchCriteriaType());
                break;
            case EQUAL:
                predicate = cb.equal(property, searchCriteria.getValue());
                addPredicate(predicates, predicate, searchCriteria.getSearchCriteriaType());
                break;
            case STARTS_WITH:
                predicate = cb.like(property, searchCriteria.getValue().toString() + "%");
                addPredicate(predicates, predicate, searchCriteria.getSearchCriteriaType());
                break;
            case CONTAINS:
                predicate = cb.like(property, "%" + searchCriteria.getValue().toString() + "%");
                addPredicate(predicates, predicate, searchCriteria.getSearchCriteriaType());
                break;
            case ENDS_WITH:
                predicate = cb.like(property, "%" + searchCriteria.getValue().toString());
                addPredicate(predicates, predicate, searchCriteria.getSearchCriteriaType());
                break;
            case GREATER_THEN:
                predicate = cb.greaterThan(property, searchCriteria.getValue().toString());
                addPredicate(predicates, predicate, searchCriteria.getSearchCriteriaType());
                break;
            case GREATER_THEN_OR_EQUAL_TO:
                predicate = cb.greaterThanOrEqualTo(property, searchCriteria.getValue().toString());
                addPredicate(predicates, predicate, searchCriteria.getSearchCriteriaType());
                break;
            default:
                throw new IllegalArgumentException("This search operation does not exists.");
        }
    }

    public void addPredicate(List<Predicate> predicates, Predicate predicate, SearchCriteriaType searchCriteriaType) {
        if (searchCriteriaType.equals(SearchCriteriaType.EXCLUDE)) {
            predicate = predicate.not();
        }
        predicates.add(predicate);
    }

    private Predicate buildPredicates(CriteriaBuilder cb, List<Predicate> predicates) throws IllegalArgumentException {
        switch (operator) {
            case AND:
                return cb.and(predicates.toArray(new Predicate[0]));
            case OR:
                return cb.or(predicates.toArray(new Predicate[0]));
            default:
                throw new IllegalArgumentException("This operator does not exists.");
        }
    }

    private <T> Expression getProperty(Root<T> root, String column, SearchOperation searchOperation) {
        int count = StringUtils.countMatches(column, ".");
        String[] part = column.split("\\.");

        Expression property = tryGetPropertyWithPrimitveType(count, root, part);

        boolean isColumnNumericType = isColumnNumericType(property);
        boolean isSearchOperationTextBase = isSearchOperationTextBase(searchOperation);

        if (isColumnNumericType && isSearchOperationTextBase || !isColumnNumericType) {
            property = property.as((Class<T>) String.class);
        }

        return property;
    }

    private <T> Expression tryGetPropertyWithPrimitveType(int count, Root<T> root, String[] part) {
        switch (count) {
            case 0:
                return root.get(part[0]);
            case 1:
                return root.get(part[0]).get(part[1]);
            case 2:
                return root.get(part[0]).get(part[1]).get(part[2]);
            default:
                throw new IllegalArgumentException("Cannot search more than 2 nested objects.");
        }
    }

    private boolean isColumnNumericType(Expression property) {
        return property.getJavaType().equals(Integer.class) || property.getJavaType().equals(Double.class);
    }

    private boolean isSearchOperationTextBase(SearchOperation searchOperation) {
        return searchOperation.equals(SearchOperation.STARTS_WITH)
                || searchOperation.equals(SearchOperation.CONTAINS)
                || searchOperation.equals(SearchOperation.ENDS_WITH);
    }
}
