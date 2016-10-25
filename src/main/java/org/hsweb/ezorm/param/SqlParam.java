package org.hsweb.ezorm.param;


import java.util.*;
import java.util.stream.Collectors;

/**
 * SQL参数对象
 *
 * @author zhouhao
 * @since 1.0
 */
public class SqlParam implements Cloneable {

    /**
     * 条件
     */
    protected List<Term> terms = new LinkedList<>();

    /**
     * 指定要处理的字段
     */
    protected Set<String> includes = new LinkedHashSet<>();

    /**
     * 指定不处理的字段
     */
    protected Set<String> excludes = new LinkedHashSet<>();

    public <T extends SqlParam> T or(String column, Object value) {
        return or(column, TermType.eq, value);
    }

    public <T extends SqlParam> T and(String column, Object value) {
        return and(column, TermType.eq, value);
    }


    public <T extends SqlParam> T or(String column, String termType, Object value) {
        Term term = new Term();
        term.setColumn(column);
        term.setValue(value);
        term.setTermType(termType);
        term.setType(Term.Type.or);
        terms.add(term);
        return (T) this;
    }

    public <T extends SqlParam> T and(String column, String termType, Object value) {
        Term term = new Term();
        term.setColumn(column);
        term.setValue(value);
        term.setTermType(termType);
        term.setType(Term.Type.and);
        terms.add(term);
        return (T) this;
    }


    public Term nest() {
        return nest(null, null);
    }

    public Term orNest() {
        return orNest(null, null);
    }

    public Term nest(String termString, Object value) {
        Term term = new Term();
        term.setColumn(termString);
        term.setValue(value);
        term.setType(Term.Type.and);
        terms.add(term);
        return term;
    }

    public Term orNest(String termString, Object value) {
        Term term = new Term();
        term.setColumn(termString);
        term.setValue(value);
        term.setType(Term.Type.or);
        terms.add(term);
        return term;
    }

    public <T extends SqlParam> T includes(String... fields) {
        includes.addAll(Arrays.asList(fields));
        return (T) this;
    }

    public <T extends SqlParam> T excludes(String... fields) {
        excludes.addAll(Arrays.asList(fields));
        includes.removeAll(Arrays.asList(fields));
        return (T) this;
    }

    public <T extends SqlParam> T where(String key, Object value) {
        and(key, value);
        return (T) this;
    }

    public <T extends SqlParam> T where(String key, String termType, Object value) {
        and(key, value);
        return (T) this;
    }

    public Set<String> getIncludes() {
        if (includes == null) includes = new LinkedHashSet<>();
        return includes;
    }

    public Set<String> getExcludes() {
        if (excludes == null) excludes = new LinkedHashSet<>();
        return excludes;
    }

    public void setIncludes(Set<String> includes) {
        this.includes = includes;
    }

    public void setExcludes(Set<String> excludes) {
        this.excludes = excludes;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    @Override
    public SqlParam clone() {
        SqlParam sqlParam = new SqlParam();
        sqlParam.setExcludes(new LinkedHashSet<>(excludes));
        sqlParam.setIncludes(new LinkedHashSet<>(includes));
        List<Term> terms = this.terms.stream().map(term -> term.clone()).collect(Collectors.toList());
        sqlParam.setTerms(terms);
        return sqlParam;
    }
}
