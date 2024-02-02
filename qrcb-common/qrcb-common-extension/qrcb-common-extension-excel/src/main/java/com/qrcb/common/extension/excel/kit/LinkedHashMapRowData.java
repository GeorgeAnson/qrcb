package com.qrcb.common.extension.excel.kit;

import com.alibaba.excel.write.metadata.MapRowData;
import com.alibaba.excel.write.metadata.RowData;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author Anson
 * @Create 2024-02-01
 * @Description 行数据，拓展原有 MapRowData <br/>
 */

public class LinkedHashMapRowData implements RowData {

    private final MapRowData keyRowData;
    private final List<?> valueList;

    public LinkedHashMapRowData(Map<String, ?> map) {
        int dataIndex=0;
        LinkedHashMap<Integer,Object> keyMap=new LinkedHashMap<>();
        for (String key : map.keySet()) {
            keyMap.put(dataIndex++,key);
        }
        this.keyRowData=new MapRowData(keyMap);
        this.valueList=new LinkedList<>(map.values());
    }


    public MapRowData getKeyRowData() {
        return this.keyRowData;
    }

    @Override
    public Object get(int index) {
        return this.valueList.get(index);
    }

    public List<?> getValueList(){
        return this.valueList;
    }

    public int size() {
        return this.valueList.size();
    }

    public boolean isEmpty() {
        return this.valueList.isEmpty();
    }

}
