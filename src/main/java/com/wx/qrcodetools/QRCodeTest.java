package com.wx.qrcodetools;

public class QRCodeTest {
    public static void main1(String[] args) throws Exception {
        /**
         *    QRcode 二维码生成测试
         *    QRCodeUtil.QRCodeCreate("http://blog.csdn.net/u014266877", "E://qrcode.jpg", 15, "E://icon.png");
         */
        /**
         *     QRcode 二维码解析测试
         *    String qrcodeAnalyze = QRCodeUtil.QRCodeAnalyze("E://qrcode.jpg");
         */
        /**
         * ZXingCode 二维码生成测试
         * QRCodeUtil.zxingCodeCreate("http://blog.csdn.net/u014266877", 300, 300, "E://zxingcode.jpg", "jpg");
         */
        /**
         * ZxingCode 二维码解析
         *    String zxingAnalyze =  QRCodeUtil.zxingCodeAnalyze("E://zxingcode.jpg").toString();
         */

          //   QRcode 二维码生成测试
//        QRCodeUtil.QRCodeCreate("1234", "E://qrcode1.jpg", 15, "E://icon.png");
//        System.out.println("success");

        QRCodeUtil.zxingCodeCreate("http://blog.csdn.net/u014266877", 500, 500, "E://zxingcode.jpg", "jpg");
        System.out.println("success");
    }
}
