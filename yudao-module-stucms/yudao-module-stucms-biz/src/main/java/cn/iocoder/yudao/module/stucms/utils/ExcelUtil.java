package cn.iocoder.yudao.module.stucms.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;

public class ExcelUtil {

    /**
     * 设置下载excel的响应头
     *
     * @param response     响应对象
     * @param realFileName 真实文件名
     */
    public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName) throws UnsupportedEncodingException {
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(realFileName, "UTF-8"));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
    }

    /**
     * 设置excel标题行样式
     * 黑体13,边框,水平居中,垂直居中,自动换行
     *
     * @return
     */
    public static WriteCellStyle getHeadStyle() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        headWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("黑体");//设置字体名字
        headWriteFont.setFontHeightInPoints((short) 13);//设置字体大小
        // headWriteFont.setBold(true);//字体加粗
        headWriteCellStyle.setWriteFont(headWriteFont); //在样式用应用设置的字体;
        // 样式
        headWriteCellStyle.setBorderBottom(BorderStyle.THIN);//设置底边框;
        headWriteCellStyle.setBottomBorderColor((short) 0);//设置底边框颜色;
        headWriteCellStyle.setBorderLeft(BorderStyle.THIN);  //设置左边框;
        headWriteCellStyle.setLeftBorderColor((short) 0);//设置左边框颜色;
        headWriteCellStyle.setBorderRight(BorderStyle.THIN);//设置右边框;
        headWriteCellStyle.setRightBorderColor((short) 0);//设置右边框颜色;
        headWriteCellStyle.setBorderTop(BorderStyle.THIN);//设置顶边框;
        headWriteCellStyle.setTopBorderColor((short) 0); //设置顶边框颜色;
        headWriteCellStyle.setWrapped(true);  //设置自动换行;
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);//设置水平对齐的样式为居中对齐;
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);  //设置垂直对齐的样式为居中对齐;
        //        headWriteCellStyle.setShrinkToFit(true);//设置文本收缩至合适

        return headWriteCellStyle;
    }

    /**
     * 设置excel单元格格式
     * 宋体11,水平居中,垂直居中,边框,自动换行,
     *
     * @return
     */
    public static WriteCellStyle getCellStyle() {
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        headWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("宋体");//设置字体名字
        headWriteFont.setFontHeightInPoints((short) 11);//设置字体大小
        headWriteCellStyle.setWriteFont(headWriteFont); //在样式用应用设置的字体;
        // 样式
        headWriteCellStyle.setBorderBottom(BorderStyle.THIN);//设置底边框;
        headWriteCellStyle.setBottomBorderColor((short) 0);//设置底边框颜色;
        headWriteCellStyle.setBorderLeft(BorderStyle.THIN);  //设置左边框;
        headWriteCellStyle.setLeftBorderColor((short) 0);//设置左边框颜色;
        headWriteCellStyle.setBorderRight(BorderStyle.THIN);//设置右边框;
        headWriteCellStyle.setRightBorderColor((short) 0);//设置右边框颜色;
        headWriteCellStyle.setBorderTop(BorderStyle.THIN);//设置顶边框;
        headWriteCellStyle.setTopBorderColor((short) 0); //设置顶边框颜色;
        headWriteCellStyle.setWrapped(true);  //设置自动换行;
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);//设置水平对齐的样式为居中对齐;
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);  //设置垂直对齐的样式为居中对齐;
        return headWriteCellStyle;
    }

    /**
     * 设置excel首行大标题样式
     */
    public static class ExcelBigTitleStyle implements SheetWriteHandler {

        private final String title;
        private final Integer headerSize;

        public ExcelBigTitleStyle(String title, Integer headerSize) {
            this.title = title;
            this.headerSize = headerSize;
        }

        @Override
        public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

        }

        @Override
        public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
            Workbook workbook = writeWorkbookHolder.getWorkbook();
            Sheet sheet = workbook.getSheetAt(0);
            //设置标题
            Row row1 = sheet.createRow(0);
            row1.setHeight((short) 800);
            Cell cell = row1.createCell(0);
            //设置单元格内容
            cell.setCellValue(this.title);
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            Font font = workbook.createFont();
            font.setFontName("方正小标宋简体");
            // font.setBold(true);
            font.setFontHeight((short) 400);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            // 第一行大标题占位设置
            sheet.addMergedRegionUnsafe(new CellRangeAddress(0, 0, 0, this.headerSize - 1));
        }
    }

    /**
     * 大数值转换
     * Excel 数值长度位15位 大于15位的数值转换位字符串
     */
    public static class ExcelBigNumberConvert implements Converter<Long> {

        @Override
        public Class<Long> supportJavaTypeKey() {
            return Long.class;
        }

        @Override
        public CellDataTypeEnum supportExcelTypeKey() {
            return CellDataTypeEnum.STRING;
        }

        @Override
        public Long convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
            return Convert.toLong(cellData.getData());
        }

        @Override
        public WriteCellData<Object> convertToExcelData(Long object, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
            if (ObjectUtil.isNotNull(object)) {
                String str = Convert.toStr(object);
                if (str.length() > 15) {
                    return new WriteCellData<>(str);
                }
            }
            WriteCellData<Object> cellData = new WriteCellData<>(new BigDecimal(object));
            cellData.setType(CellDataTypeEnum.NUMBER);
            return cellData;
        }

    }

}
