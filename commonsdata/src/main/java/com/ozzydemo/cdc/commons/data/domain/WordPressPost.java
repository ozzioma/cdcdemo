package com.ozzydemo.cdc.commons.data.domain;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class WordPressPost
{
    private Long id;
    private Long author;

//    private LocalDateTime postDate;
//    private LocalDateTime postDateGmt;

    private String postDate;
    private String postDateGmt;

    private String postContent;
    private String postTitle;
    private String postExcerpt;
    private String postStatus;
    private String commentStatus;
    private String pingStatus;
    private String postPassword;
    private String postName;

    private String toPing;
    private String pinged;

//    private LocalDateTime postModified;
//    private LocalDateTime postModifiedGmt;

    private String postModified;
    private String postModifiedGmt;

    private String postContentFiltered;
    //private Long postParent;
    private String postParent;

    private String guid;

    //private Integer menuOrder;
    private String menuOrder;

    private String postType;
    private String postMimeType;
    //private Long commentCount;
    private String commentCount;

}
