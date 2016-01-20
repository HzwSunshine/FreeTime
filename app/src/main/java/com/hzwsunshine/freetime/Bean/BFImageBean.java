package com.hzwsunshine.freetime.Bean;

import java.util.List;

/**
 * Created by 何志伟 on 2015/11/8.
 */
public class BFImageBean {

    private String comment_author;

    private String comment_author_IP;

    private String comment_date;

    private String vote_positive;

    private String vote_negative;

    private String text_content;

    private List<String> pics;

    public String getComment_author() {
        return comment_author;
    }

    public String getComment_author_IP() {
        return comment_author_IP;
    }

    public String getComment_date() {
        return comment_date;
    }

    public String getVote_positive() {
        return vote_positive;
    }

    public String getVote_negative() {
        return vote_negative;
    }

    public String getText_content() {
        return text_content;
    }

    public List<String> getPics() {
        return pics;
    }
}
