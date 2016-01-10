package net.vinote.smartboot.service.facade.result;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 调用接口的响应结果存放类
 *
 * @author 三刀
 * @version v 0.1 2015年11月5日 上午10:59:08 Seer Exp.
 */
public class ServiceResult<T> implements Serializable {
    /** */
    private static final long serialVersionUID = -7815723967101753647L;
    private boolean success;
    private String message;
    private T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

	public boolean isSuccess() {

        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                ToStringStyle.SIMPLE_STYLE);
    }
}
