package com.websql.util;


import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * @ClassName ExcelLocalDateStringConverter
 * @Description excel 日期格式转换
 * @Author rabbit boy_0214@sina.com
 * @Date 2024/10/08  12:34
 **/
public class ExcelLocalDateStringConverter implements Converter<Timestamp> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return Timestamp.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }


    @Override
    public WriteCellData<LocalDate> convertToExcelData(Timestamp value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (value == null) {
            return null;
        }
        return new WriteCellData(DateUtil.format(value, "yyyy-MM-dd HH:mm:ss"));
    }


}
