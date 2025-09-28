package org.spiderflow.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.spi.DeferredProcessingAware;
import ch.qos.logback.core.status.ErrorStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SpiderFlowFileAppender extends FileAppender<ILoggingEvent> {
    
    private final Lock lock = new ReentrantLock();

    @Override
    protected void subAppend(ILoggingEvent event) {
        OutputStream os = getOutputStream();
        // 尝试获取SpiderContext，如果类不存在则跳过
        try {
            Class<?> contextHolderClass = Class.forName("org.spiderflow.context.SpiderContextHolder");
            Object context = contextHolderClass.getMethod("get").invoke(null);
            if (context != null) {
                Class<?> jobContextClass = Class.forName("org.spiderflow.core.job.SpiderJobContext");
                if (jobContextClass.isInstance(context)) {
                    os = (OutputStream) jobContextClass.getMethod("getOutputstream").invoke(context);
                }
            }
        } catch (Exception e) {
            // 类不存在或方法调用失败，使用默认输出流
        }
        
        try {
            if (event instanceof DeferredProcessingAware) {
                ((DeferredProcessingAware) event).prepareForDeferredProcessing();
            }
            byte[] byteArray = this.encoder.encode(event);
            writeBytes(os, byteArray);

        } catch (IOException ioe) {
            this.started = false;
            addStatus(new ErrorStatus("IO failure in appender", this, ioe));
        }
    }

    private void writeBytes(OutputStream os, byte[] byteArray) throws IOException {
        if (byteArray == null || byteArray.length == 0)
            return;

        lock.lock();
        try {
            os.write(byteArray);
            if (isImmediateFlush()) {
                os.flush();
            }
        } finally {
            lock.unlock();
        }
    }

}