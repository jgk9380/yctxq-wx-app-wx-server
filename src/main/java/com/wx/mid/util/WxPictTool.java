package com.wx.mid.util;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class WxPictTool extends PictUtils {

    /**获取推广海报
     */
    //    String coverPic="D:\\image\\VRV10.jpg";
    //    int nickNameX=115,nickNameY=675;
    //    int qrCodeX=430,qrCodeY=535,qrCodeWidth=820;
    String coverPic;
    int nickNameX, nickNameY, qrCodeX, qrCodeY, qrCodeWidth;

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setNickNameX(int nickNameX) {
        this.nickNameX = nickNameX;
    }

    public int getNickNameX() {
        return nickNameX;
    }

    public void setNickNameY(int nickNameY) {
        this.nickNameY = nickNameY;
    }

    public int getNickNameY() {
        return nickNameY;
    }

    public void setQrCodeX(int qrCodeX) {
        this.qrCodeX = qrCodeX;
    }

    public int getQrCodeX() {
        return qrCodeX;
    }

    public void setQrCodeY(int qrCodeY) {
        this.qrCodeY = qrCodeY;
    }

    public int getQrCodeY() {
        return qrCodeY;
    }

    public void setQrCodeWidth(int qrCodeWidth) {
        this.qrCodeWidth = qrCodeWidth;
    }

    public int getQrCodeWidth() {
        return qrCodeWidth;
    }
    //(coverImage, nn, 505, 675, qrImage, 250, 115, 625);
    public BufferedImage getSpreadPosters(String coverPic, String nickName, int nickNameX, int nickNameY,
                                          BufferedImage qrImage, int qrCodeWidth, int qrCodeX, int qrCodeY) {
        PictUtils pictUtil = new PictUtils();
        BufferedImage sourceImage = pictUtil.loadImageLocal(coverPic); //"D:\\image\\sbv2.jpg"
        //BufferedImage qrImage = pictUtil.convertByteArrayToImage(qrcode);
        pictUtil.modifyImage(sourceImage, "" + nickName + "", nickNameX, nickNameY); //505, 675
        Image sizeQrImage = qrImage.getScaledInstance(qrCodeWidth, qrCodeWidth, Image.SCALE_DEFAULT);
        BufferedImage bufferedQrImage = pictUtil.convertImageToBuffer(sizeQrImage);
        BufferedImage shareImage =
            pictUtil.modifyImagetogeter(bufferedQrImage, sourceImage, qrCodeX, qrCodeY); //115, 625
        return shareImage;
    }

    public BufferedImage getSpreadPosters(String nickName, BufferedImage qrImage) {
        PictUtils pictUtil = new PictUtils();
        BufferedImage sourceImage = pictUtil.loadImageLocal(coverPic); //"D:\\image\\sbv2.jpg"
        //BufferedImage qrImage = pictUtil.convertByteArrayToImage(qrcode);
        pictUtil.modifyImage(sourceImage, "" + nickName + "", nickNameX, nickNameY); //505, 675
        Image sizeQrImage = qrImage.getScaledInstance(qrCodeWidth, qrCodeWidth, Image.SCALE_DEFAULT);
        BufferedImage bufferedQrImage = pictUtil.convertImageToBuffer(sizeQrImage);
        BufferedImage shareImage =
            pictUtil.modifyImagetogeter(bufferedQrImage, sourceImage, qrCodeX, qrCodeY); //115, 625
        return shareImage;
    }

}
