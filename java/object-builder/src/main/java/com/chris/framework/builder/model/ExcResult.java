package com.chris.framework.builder.model;

import java.util.List;

/**
 * YuedaoXingApiReBuilder
 * com.ydx.api.model.base
 * Created by Chris Chen
 * 2018/2/13
 * Explain:方法执行结果
 */
public class ExcResult<T> {
    private int code = 0;//特殊约定的信息代码，使用代码返回执行报告比较简单高效
    private String msg = "success";//错误信息，一般在返回的末端可以直接使用
    private String report;//执行报告，适用于单条
    private List<String> reportList;//执行报告，适用于多条
    private Exception exception;//没有被抛出的主要异常
    private List<Exception> exceptionList;//当一个方法可能会抛出多个异常都需要被调用者处理时，放在集合中
    private boolean isSuccess = true;//执行是否成功
    private T resultData;//需要返回传递的数据体
    private ExcResult excResult;//从上一个调用获得的返回结果

    private ExcResult() {
    }

    public static ExcResult build() {
        return new ExcResult();
    }

    public int getCode() {
        return code;
    }

    public ExcResult setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ExcResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getReport() {
        return report;
    }

    public ExcResult setReport(String report) {
        this.report = report;
        return this;
    }

    public List<String> getReportList() {
        return reportList;
    }

    public ExcResult setReportList(List<String> reportList) {
        this.reportList = reportList;
        return this;
    }

    public Exception getException() {
        return exception;
    }

    public ExcResult setException(Exception exception) {
        this.exception = exception;
        return this;
    }

    public List<Exception> getExceptionList() {
        return exceptionList;
    }

    public ExcResult setExceptionList(List<Exception> exceptionList) {
        this.exceptionList = exceptionList;
        return this;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isFail() {
        return !isSuccess;
    }

    public ExcResult setSuccess() {
        isSuccess = true;
        return this;
    }

    public ExcResult setFail() {
        isSuccess = false;
        return this;
    }

    public T getResultData() {
        return resultData;
    }

    public ExcResult setResultData(T resultData) {
        this.resultData = resultData;
        return this;
    }

    public ExcResult getExcResult() {
        return excResult;
    }

    public ExcResult setExcResult(ExcResult excResult) {
        this.excResult = excResult;
        return this;
    }

    public void throwException() {
        if (this.exception == null) {
            return;
        }
        throwException(this.exception);
    }

    public void throwException(Exception exception) {
        if (exception == null) {
            return;
        }
        try {
            throw exception;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void throwExceptions(int index) {
        throwException(exceptionList.get(index));
    }

    public void throwExceptions(List<Exception> exceptionList) {
        if (exceptionList == null) {
            return;
        }
        for (Exception exception : exceptionList) {
            throwException(exception);
        }
    }
}
