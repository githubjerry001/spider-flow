package org.spiderflow.core.model;

import java.util.List;

public class Page<T> {
    private List<T> records;
    private long total;
    private long size;
    private long current;
    
    public Page() {}
    
    public Page(List<T> records, long total, long size, long current) {
        this.records = records;
        this.total = total;
        this.size = size;
        this.current = current;
    }
    
    public List<T> getRecords() {
        return records;
    }
    
    public void setRecords(List<T> records) {
        this.records = records;
    }
    
    public long getTotal() {
        return total;
    }
    
    public void setTotal(long total) {
        this.total = total;
    }
    
    public long getSize() {
        return size;
    }
    
    public void setSize(long size) {
        this.size = size;
    }
    
    public long getCurrent() {
        return current;
    }
    
    public void setCurrent(long current) {
        this.current = current;
    }
    
    public long getPages() {
        if (size == 0) return 0;
        return (total + size - 1) / size;
    }
}