package com.jiangnx.community.entity;

import java.util.Date;
import java.util.Objects;

public class DiscussPost {

    private Integer id;
    private Integer UserId;
    private String title;
    private String content;
    private Integer type;
    private Integer status;
    private Date cerateTime;
    private Integer commentCount;
    private double score;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCerateTime() {
        return cerateTime;
    }

    public void setCerateTime(Date cerateTime) {
        this.cerateTime = cerateTime;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "DiscussPost{" +
                "id=" + id +
                ", UserId='" + UserId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", cerateTime=" + cerateTime +
                ", commentCount=" + commentCount +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscussPost that = (DiscussPost) o;
        return Double.compare(that.score, score) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(UserId, that.UserId) &&
                Objects.equals(title, that.title) &&
                Objects.equals(content, that.content) &&
                Objects.equals(type, that.type) &&
                Objects.equals(status, that.status) &&
                Objects.equals(cerateTime, that.cerateTime) &&
                Objects.equals(commentCount, that.commentCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, UserId, title, content, type, status, cerateTime, commentCount, score);
    }
}
