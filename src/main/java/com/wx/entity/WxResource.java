package com.wx.entity;

import javax.persistence.*;

@Entity
@Table(name="wx_resource")
public class WxResource {
    @Id
    @Column
    Long id;
    @Column
    String remark;
    @Lob
    @Column
    byte[] resourceContent;
    @Column
    String fileName;

    @Column
    String fileType;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public byte[] getResourceContent() {
        return resourceContent;
    }

    public void setResourceContent(byte[] resourceContent) {
        this.resourceContent = resourceContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WxResource wxPicture = (WxResource) o;

        return id != null ? id.equals(wxPicture.id) : wxPicture.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
