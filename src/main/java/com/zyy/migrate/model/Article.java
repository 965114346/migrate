package com.zyy.migrate.model;

import lombok.Data;

import java.util.Date;

@Data
public class Article {

    private Integer id;

    private Integer sortId;

    private String userId;

    private String title;

    private String description;

    private String thumbnail;

    private String content;

    private Integer passed;

    private Integer readType;

    private Date createTime;

    private Date updateTime;

    private String articleExtend;

    private Integer readNum;

    private String languageType;
}
