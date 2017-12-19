package com.wx.controller;

import com.wx.dao.WxResourceDao;
import com.wx.entity.WxResource;
import com.wx.mid.util.WxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Controller
@RequestMapping("/public")
public class PictureContrller {
    @Autowired
    WxResourceDao wxPictureDao;
    @Autowired
    WxUtils wxUtils;
    @RequestMapping(value = "/resource_upload/batch", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public @ResponseBody
    String batchUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;
        long id = -1;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    id = uploadFile(file,null);
                } catch (Exception e) {
                    return "You failed to upload " + i + " => " + e.getMessage();
                }
            }
            return "You failed to upload " + i + " because the file was empty.";
        }
        return "上传成功,id=" + id;
    }


    @RequestMapping(value = "/resource_upload", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file,@RequestParam(value = "remark",required = false)String remark) {
        long id = -1;
        if (!file.isEmpty()) {
            try {
                id = uploadFile(file,remark);
            } catch (Exception e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            }
            return "上传成功,id=" + id;
        }
        return "文件为空，上传失败。";

    }

    public long uploadFile(MultipartFile file,String remark) throws IOException {
        //todo 文件名，ext为空什么情况？
        byte[] bytes = file.getBytes();
        String fileName = file.getOriginalFilename();
        String fileExt = fileName.substring(fileName.indexOf(".") + 1);
        //  stream = new BufferedOutputStream(new FileOutputStream(new File(file.getOriginalFilename())));
        //  stream.write(bytes);
        //  stream.close();
        WxResource wxPicture = new WxResource();
        wxPicture.setId(wxUtils.getSeqencesValue().longValue());
        wxPicture.setResourceContent(bytes);
        wxPicture.setFileName(fileName);
        if(remark!=null )wxPicture.setRemark(remark);
        wxPicture.setFileType(fileExt);
        wxPictureDao.save(wxPicture);
        return wxPicture.getId();
    }


    //显示资源库中的图片
    @RequestMapping(path = "/show_pict/{id}", method = RequestMethod.GET)
    public void showPicture(@PathVariable("id") long id,HttpServletResponse response) throws IOException {
        WxResource wxPicture= wxPictureDao.findById(id);
        if(!wxPicture.getFileType().equalsIgnoreCase("jpg"))
            return;
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        OutputStream os = response.getOutputStream();
        os.write(wxPicture.getResourceContent());
        os.flush();
    }
}


//    常见媒体类型：
//    text/html ： HTML格式          text/plain ：纯文本格式             text/xml ：XML格式
//    image/gif ：gif图片格式          image/jpeg ：jpg图片格式          image/png：png图片格式
//    application/x-www-form-urlencoded ： <form encType=””>中默认的encType，form表单数据被编码为key/value格式发送到服务器（表单默认的提交数据的格式）。
//    multipart/form-data ： 当你需要在表单中进行文件上传时，就需要使用该格式；
//
//    application/xhtml+xml ：XHTML格式               application/xml     ： XML数据格式
//    application/atom+xml  ：Atom XML聚合格式    application/json    ： JSON数据格式
//    application/pdf       ：pdf格式                        application/msword  ： Word文档格式
//    application/octet-stream ： 二进制流数据（如常见的文件下载）。