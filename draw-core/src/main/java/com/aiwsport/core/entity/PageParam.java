package com.aiwsport.core.entity;

/**
 * @author pangxin1
 * @date 2019-07-28 16:48
 */
public class PageParam {
    private int start;
    private int length;
    private String nickName;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
