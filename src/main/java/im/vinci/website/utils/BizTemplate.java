package im.vinci.website.utils;

/**
 * Created by tim@vinci on 15/11/26.
 */

import im.vinci.website.exception.ErrorCode;
import im.vinci.website.exception.VinciException;
import im.vinci.monitor.QMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BizTemplate
 */
public abstract class BizTemplate<T> {
    protected String monitorKey;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected BizTemplate(String monitorKey) {
        this.monitorKey = monitorKey;
    }

    protected abstract void checkParams() throws VinciException;

    protected abstract T process() throws Exception;

    protected void afterProcess() {
    }

    protected void onSuccess(T result) {
    }

    protected void onError(Throwable e) {
    }

    protected boolean isCriticalErrorCode(int errorCode) {
        return false;
    }

    public T execute() throws VinciException {
        try {
            checkParams();
        } catch (VinciException e) {
            recordInvalidParam(e);
            throw e;
        } catch (Throwable e) {
            recordInvalidParam(e);
            throw new VinciException();
        }

        long start = System.currentTimeMillis();
        try {
            T result = process();
            onSuccess(result);
            QMonitor.recordOne(monitorKey + "_Success", System.currentTimeMillis() - start);
            return result;
        } catch (VinciException e) {
            onError(e);
            QMonitor.recordOne(monitorKey + "_VinciException");
            QMonitor.recordOne(monitorKey + "_Failed");
            QMonitor.recordOne("BizTemplate_VinciException");

            if (isCriticalErrorCode(e.getErrorCode())) {
                QMonitor.recordOne(monitorKey + "_CriticalError");
                logger.warn("执行业务逻辑出现异常错误: monitorKey={}, code={}, msg={}", monitorKey, e.getErrorCode(), e.getMessage(), e);
            } else {
                QMonitor.recordOne(monitorKey + "_CommonError");
                logger.warn("执行业务逻辑出现普通错误: monitorKey={}, code={}, msg={}", monitorKey, e.getErrorCode(), e.getMessage());
            }
            throw e;
        } catch (Throwable e) {
            onError(e);
            logger.error("执行业务逻辑出现未知异常 monitoryKey={}", monitorKey, e);
            QMonitor.recordOne(monitorKey + "_UnknownError");
            QMonitor.recordOne(monitorKey + "_Failed");
            QMonitor.recordOne(monitorKey + "_CriticalError");
            QMonitor.recordOne("BizTemplate_UnknownError");
            throw new VinciException(e, ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage(), "未知错误");

        } finally {
            afterProcess();
            QMonitor.recordOne(monitorKey + "_Invoke", System.currentTimeMillis() - start);
        }
    }

    private void recordInvalidParam(Throwable e) {
        if (logger.isDebugEnabled()) {
            logger.debug(monitorKey + "_校验参数失败", e);
        } else {
            logger.info(monitorKey + "_校验参数失败: " + e.getMessage());
        }
        QMonitor.recordOne(this.monitorKey + "_Invalid_Parameter");
        QMonitor.recordOne("BizTemplate_VinciException_Invalid_Parameter");
    }
}
