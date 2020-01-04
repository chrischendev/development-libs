package com.chris.framework.builder.utils;

import com.chris.framework.builder.net.exception.RequestException;
import com.chris.framework.builder.net.protocol.NetResult;

/**
 * ChrisSpringDemo
 * com.meiyue.library.utils
 * Created by Chris Chen
 * 2017/9/13
 * Explain:构建返回结果的工具
 */
public class ResultUtils {
    /**
     * 构建一个字符串类型的返回结果
     *
     * @param obj
     * @return
     */
    public static String buildJson(Object obj) {
        return JsonUtils.toJson(obj);
    }

    /**
     * 构建一个NetResult<DataType>类型的返回结果
     *
     * @param obj
     * @param <DataType>
     * @return
     */
    public static <DataType> NetResult<DataType> buildResult(DataType obj) {
        NetResult<DataType> result = new NetResult<>();
        if (obj != null) {
            result.setCode(NetResult.SUCCESS);
            result.setMsg(NetResult.MSG_SUCCESS);
            result.setData(obj);
        } else {
            result.setCode(NetResult.ERROR);
            result.setMsg("data is null");
            result.setData(null);
        }
        return result;
    }

    /**
     * 构造一个请求错误的结果
     *
     * @param errInfo    错误信息
     * @param <DataType>
     * @return
     */
    public static <DataType> NetResult<DataType> buildError(String errInfo) {
        NetResult<DataType> result = new NetResult<>();
        result.setCode(NetResult.ERROR);
        result.setMsg(errInfo);
        result.setData(null);
        return result;
    }

    /**
     * 构造一个自定义错误码的结果
     *
     * @param code
     * @param errInfo
     * @param <DataType>
     * @return
     */
    public static <DataType> NetResult<DataType> buildError(int code, String errInfo) {
        NetResult<DataType> result = new NetResult<>();
        result.setCode(code);
        result.setMsg(errInfo);
        result.setData(null);
        return result;
    }

    /**
     * 构造请求异常的返回结果
     *
     * @param re         请求异常
     * @param <DataType>
     * @return
     */
    public static <DataType> NetResult<DataType> buildError(RequestException re) {
        return buildError(re.getCode(), re.getMessage());
    }

}
