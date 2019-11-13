package com.zyy.migrate.sql;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class Sql {

    private List<String> insert;

    private List<String> select;

    private String table;

    private String where;

    private List<String> and;

    private Class<?> clazz;

    public Sql(Builder builder) {
        this.select = builder.select;
        this.table = builder.table;
        this.where = builder.where;
        this.and = builder.and;
        this.clazz = builder.clazz;
        this.insert = builder.insert;
    }

    public static class Builder {
        private List<String> insert;

        private List<String> select;

        private String table;

        private String where;

        private List<String> and;

        private Class<?> clazz;

        public Builder() {

        }

        public Builder(String table) {
            this.table = table;
        }

        public Builder(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Builder(Class<?> clazz, String table) {
            this.clazz = clazz;
            this.table = table;
        }

        public Builder insert(String insert) {
            String[] split = insert.split(",");
            return insert(split);
        }

        public Builder insert(String... insert) {
            return insert(Arrays.asList(insert));
        }

        public Builder insert(List<String> insert) {
            this.insert = insert;
            return this;
        }

        public Builder select(String select) {
            String[] split = select.split(",");
            return select(split);
        }

        public Builder select(String... select) {
            return select(Arrays.asList(select));
        }

        public Builder select(List<String> select) {
            if (select.size() < 1) {
                select.add("*");
            }
            this.select = select;
            return this;
        }

        public Builder table(String table) {
            this.table = table;
            return this;
        }

        public Builder where(String where) {
            this.where = where;
            return this;
        }

        public Builder and(String and) {
            String[] split = and.split(",");
            return and(split);
        }

        public Builder and(String... and) {
            return and(Arrays.asList(and));
        }

        public Builder and(List<String> and) {
            this.and = and;
            return this;
        }

        public Sql build() {
            return new Sql(this);
        }
    }
}
