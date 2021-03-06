package org.modelcatalogue.spreadsheet.query.simple;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.FromString;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.modelcatalogue.spreadsheet.api.Row;
import org.modelcatalogue.spreadsheet.query.api.Predicate;
import org.modelcatalogue.spreadsheet.query.api.RowCriterion;
import org.modelcatalogue.spreadsheet.query.api.SheetCriterion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

final class SimpleSheetCriterion extends AbstractCriterion<Row> implements SheetCriterion {

    private final Collection<SimpleRowCriterion> criteria = new ArrayList<SimpleRowCriterion>();

    SimpleSheetCriterion() {}

    private SimpleSheetCriterion(boolean disjoint) {
        super(disjoint);
    }

    @Override
    public Predicate<Row> number(final int row) {
        return new Predicate<Row>() {
            @Override
            public boolean test(Row o) {
                return o.getNumber() == row;
            }
        };
    }

    @Override
    public Predicate<Row> range(final int from, final int to) {
        return new Predicate<Row>() {
            @Override
            public boolean test(Row o) {
                return o.getNumber() >= from && o.getNumber() <= to;
            }
        };
    }

    @Override
    public void row(@DelegatesTo(RowCriterion.class) @ClosureParams(value = FromString.class, options = "org.modelcatalogue.spreadsheet.query.api.RowCriterion") Closure rowCriterion) {
        SimpleRowCriterion criterion = new SimpleRowCriterion();
        DefaultGroovyMethods.with(criterion, rowCriterion);
        criteria.add(criterion);
    }

    @Override
    public void row(int row, @DelegatesTo(RowCriterion.class) @ClosureParams(value = FromString.class, options = "org.modelcatalogue.spreadsheet.query.api.RowCriterion") Closure rowCriterion) {
        row(row);
        row(rowCriterion);
    }

    @Override
    public void row(Predicate<Row> predicate, @DelegatesTo(RowCriterion.class) @ClosureParams(value = FromString.class, options = "org.modelcatalogue.spreadsheet.query.api.RowCriterion") Closure rowCriterion) {
        row(predicate);
        row(rowCriterion);
    }

    @Override
    public void row(Predicate<Row> predicate) {
        addCondition(predicate);
    }

    @Override
    public void row(int row) {
        row(number(row));
    }

    Collection<SimpleRowCriterion> getCriteria() {
        return Collections.unmodifiableCollection(criteria);
    }

    @Override
    Predicate<Row> newDisjointCriterionInstance() {
        return new SimpleSheetCriterion(true);
    }
}
