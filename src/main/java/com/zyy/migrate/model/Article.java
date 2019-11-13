package com.zyy.migrate.model;

import com.zyy.migrate.annotation.ColumnMapping;
import com.zyy.migrate.annotation.TableMapping;
import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

@Data
@TableMapping(sourceTable = "wp_posts", targetTable = "article")
public class Article {

    @ColumnMapping(sourceColumn = "id", targetColumn = "id")
    private BigInteger id;

    @ColumnMapping(targetColumn = "sort_id", defaultInsertValue = "30")
    private Integer sortId;

    @ColumnMapping(sourceColumn = "post_author", targetColumn = "user_id")
    private BigInteger userId;

    @ColumnMapping(sourceColumn = "post_title", targetColumn = "title")
    private String title;

    @ColumnMapping(sourceColumn = "post_excerpt", targetColumn = "description")
    private String description;

    @ColumnMapping(targetColumn = "thumbnail")
    private String thumbnail;

    @ColumnMapping(sourceColumn = "post_content", targetColumn = "content")
    private String content;

    @ColumnMapping(targetColumn = "passed", defaultInsertValue = "1")
    private Integer passed;

    @ColumnMapping(targetColumn = "read_type", defaultInsertValue = "4")
    private Integer readType;

    @ColumnMapping(sourceColumn = "post_password", targetColumn = "article_extend")
    private String articleExtend;

    @ColumnMapping(sourceColumn = "comment_count", targetColumn = "read_num")
    private Long readNum;

    @ColumnMapping(targetColumn = "language_type", defaultInsertValue = "zh")
    private String languageType;

    @ColumnMapping(sourceColumn = "post_date", targetColumn = "create_time")
    private Timestamp create;

    @ColumnMapping(sourceColumn = "post_modified", targetColumn = "update_time")
    private Timestamp update;
}
