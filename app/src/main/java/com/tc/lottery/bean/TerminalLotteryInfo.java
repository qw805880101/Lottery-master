package com.tc.lottery.bean;

import java.io.Serializable;

public class TerminalLotteryInfo implements Serializable {

    /**
     * 票箱ID（列表）
     */
    private String boxId;
    /**
     * 彩票ID（列表）
     */
    private String lotteryId;
    /**
     * 彩票名称（列表）
     */
    private String lotteryName;
    /**
     * 图片地址（列表）
     */
    private String lotteryImg;
    /**
     * 彩票单价（列表）
     */
    private String lotteryAmt;
    /**
     * 剩余（列表）
     */
    private String surplus;
    /**
     * 票箱状态(列表)
     * 1 正常
     * 2 无票
     * 3 故障
     */
    private String boxStatus;

    /**
     * 出票状态
     * 1 出票成功
     * 2 出票异常
     */
    private String ticketStatus;

    /**
     * 张数
     */
    private String num;
    /**
     * 票尺寸
     */
    private String ticketLen;

    /**
     * 必传，以实际出票情况送，出成功就传1张数，出票异常则传0张数
     */
    private String ticketNum;

    public String getTicketNum() {
        return ticketNum;
    }

    public void setTicketNum(String ticketNum) {
        this.ticketNum = ticketNum;
    }

    public String getTicketLen() {
        return ticketLen;
    }

    public void setTicketLen(String ticketLen) {
        this.ticketLen = ticketLen;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getLotteryName() {
        return lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public String getLotteryImg() {
        return lotteryImg;
    }

    public void setLotteryImg(String lotteryImg) {
        this.lotteryImg = lotteryImg;
    }

    public String getLotteryAmt() {
        return lotteryAmt;
    }

    public void setLotteryAmt(String lotteryAmt) {
        this.lotteryAmt = lotteryAmt;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public String getBoxStatus() {
        return boxStatus;
    }

    public void setBoxStatus(String boxStatus) {
        this.boxStatus = boxStatus;
    }
}
