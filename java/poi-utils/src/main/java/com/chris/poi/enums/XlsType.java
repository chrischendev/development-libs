package com.chris.poi.enums;

/**
 * Created by Chris Chen
 * 2018/12/10
 * Explain: xls文件版本格式
 */

public enum XlsType {
    XLS("xls", ".xls"), XLSX("xlsx", ".xlsx");

    private String extName;
    private String ext;

    XlsType(String extName, String ext) {
        this.extName = extName;
        this.ext = ext;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
