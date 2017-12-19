package com.wx;

import com.google.zxing.WriterException;
import com.wx.dao.WxPermQrCodeDao;
import com.wx.dao.WxQrCodeDao;
import com.wx.dao.WxResourceDao;
import com.wx.entity.WxPermQrCode;
import com.wx.entity.WxQrCode;
import com.wx.entity.WxResource;
import com.wx.mid.base.util.AdvancedUtil;
import com.wx.mid.operator.WxManager;
import com.wx.mid.util.WxUtils;
import com.wx.qrcodetools.QRCodeUtil;
import net.sf.json.JSONObject;
import org.apache.tomcat.jni.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


@Component
public class QrCodeCreater {

    @Autowired
    WxPermQrCodeDao wxPermQrCodeDao;
    @Autowired
    WxManager wxManager;
    @Autowired
    WxUtils wxUtils;
    @Autowired
    WxResourceDao wxResourceDao;
    @Autowired
    WxQrCodeDao wxQrCodeDao;

    String qrCodeUrlTemplate = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7dcc6b2e03a47c0b&redirect_uri=http://www.cu0515.com/myqrcode.html&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect ";
    private static Logger log = LoggerFactory.getLogger(QrCodeCreater.class);

    private WxPermQrCode createPermQrCode(int wxUserId) {
        log.info("\n createQrCode \n");
        Integer currentSceneId = wxPermQrCodeDao.queryMaxSceneId();
        log.info("\n -------currentSceneId=" + currentSceneId);
        if (null == currentSceneId) {
            currentSceneId = 0;
        }
        int nextCurrentSceneId = currentSceneId + 1;
        if (nextCurrentSceneId >= 100000) {
            log.error("\n-------------- 永久二维码已满，请整理二维码  ------------- \n ");
            return null;
        }

        JSONObject json = wxManager.getWxOperator().createPermanentQRCode(nextCurrentSceneId);
        log.info("\n---qrCodejson=" + json.toString());
        WxPermQrCode wxPermQrCode = new WxPermQrCode();
        wxPermQrCode.setId(wxUtils.getSeqencesValue().intValue());
        wxPermQrCode.setSceneId(currentSceneId + 1);
        wxPermQrCode.setTicket((String) json.get("ticket"));
        wxPermQrCode.setUrl((String) json.get("url"));
        wxPermQrCode.setWxUserId(wxUserId);
        return wxPermQrCodeDao.save(wxPermQrCode);

    }

    public WxPermQrCode getWxUserPermQrCde(int wxUserId) {
        log.info("--------------wxUserId="+wxUserId);
        WxPermQrCode wxPermQrCode = wxPermQrCodeDao.findByWxUserId(wxUserId);
   //     log.info("-------------- begin wxPermQrCode="+wxPermQrCode.getId());
        if (wxPermQrCode == null)
            wxPermQrCode = this.createPermQrCode(wxUserId);
        log.info("-------------- end wxPermQrCode="+wxPermQrCode.getId());
        return wxPermQrCode;
    }

    @Transactional
    public void createWxQrCode() throws WriterException, IOException {
        long id = wxUtils.getSeqencesValue().intValue();
        String url = qrCodeUrlTemplate.replace("STATE", "" + id);
        BufferedImage image = QRCodeUtil.zxingCodeCreateImage(url, 500, 500, "jpg");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean flag = ImageIO.write(image, "jpg", out);
        byte[] b = out.toByteArray();

        WxResource wxResource = new WxResource();
        long resourceId = wxUtils.getSeqencesValue().intValue();
        wxResource.setId(resourceId);
        wxResource.setResourceContent(b);
        wxResource.setRemark("二维码:" + id + "的图形");
        wxResource.setFileType("jpg");
        WxQrCode wxQrCode = new WxQrCode();
        wxQrCode.setId(id);
        wxQrCode.setUrl(url);
        wxQrCode.setPictId(resourceId);
        wxResourceDao.save(wxResource);
        wxQrCodeDao.save(wxQrCode);
        System.out.println("\n  创建二维码成功！");
    }

//    public void create500PermQrCode(){
//        for(int i=0;i<500;i++)
//            this.createPermQrCode();
//    }

    public void create500QrCode() {
        for (int i = 0; i < 5000; i++)
            try {
                this.createWxQrCode();
            } catch (WriterException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void downLoad(){//下载二维码文件
        List<WxQrCode> wxQrCodeList =wxQrCodeDao.findByOwnerIdIsNull();
        int i=0;
        for(WxQrCode wxQrCode:wxQrCodeList){
            i++;
            // String fileName="e:\\qrcode\\"+wxQrCode.getId()+"_"+wxQrCode.getPictId()+".jpg";
            String fileName="e:\\qrcode\\No"+wxQrCode.getId()+".jpg";
            System.out.println("fileName="+fileName+"  at: "+i);
            WxResource wxResource= wxResourceDao.findById(wxQrCode.getPictId());
            try {
                createFile(fileName, wxResource.getResourceContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createFile(String path, byte[] content) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(content);
        fos.close();
    }

}
