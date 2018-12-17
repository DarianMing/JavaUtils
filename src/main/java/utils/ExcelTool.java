package utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: hfHuang
 * @create: 2018-12-07
 * https://www.cnblogs.com/lyy9902/p/8392202.html
 * https://blog.csdn.net/s_ongfei/article/details/2794570
 */
@Slf4j
public class ExcelTool<T> {

    static int sheetsize = 5000;

    /**
     * @param data   导入到excel中的数据
     * @param out    数据写入的文件
     * @param fields 需要注意的是这个方法中的map中：每一列对应的实体类的英文名为键，excel表格中每一列名为值
     * @author Lyy
     */
    public static <T> void ListtoExecl(List<T> data, OutputStream out,
                                       Map<String, String> fields) throws Exception {
//        HSSFWorkbook workbook = new HSSFWorkbook();
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 如果导入数据为空，则抛出异常。
        if (data == null || data.size() == 0) {
            workbook.close();
            throw new Exception("导入的数据为空");
        }
        // 根据data计算有多少页sheet
        int pages = data.size() / sheetsize;
        if (data.size() % sheetsize > 0) {
            pages += 1;
        }
        // 提取表格的字段名（英文字段名是为了对照中文字段名的）
        String[] egtitles = new String[fields.size()];
        String[] cntitles = new String[fields.size()];
        Iterator<String> it = fields.keySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            String egtitle = (String) it.next();
            String cntitle = fields.get(egtitle);
            egtitles[count] = egtitle;
            cntitles[count] = cntitle;
            count++;
        }
        // 添加数据
        for (int i = 0; i < pages; i++) {
            int rownum = 0;
            // 计算每页的起始数据和结束数据
            int startIndex = i * sheetsize;
            int endIndex = (i + 1) * sheetsize - 1 > data.size() ? data.size()
                    : (i + 1) * sheetsize - 1;
            // 创建每页，并创建第一行
//            HSSFSheet sheet = workbook.createSheet();
//            HSSFRow row = sheet.createRow(rownum);
            XSSFSheet sheet = workbook.createSheet();
            XSSFRow row = sheet.createRow(rownum);

            // 在每页sheet的第一行中，添加字段名
            for (int f = 0; f < cntitles.length; f++) {
//                HSSFCell cell = row.createCell(f);
                XSSFCell cell = row.createCell(f);
                cell.setCellValue(cntitles[f]);
            }
            rownum++;
            // 将数据添加进表格
            for (int j = startIndex; j < endIndex; j++) {
                row = sheet.createRow(rownum);
                T item = data.get(j);
                for (int h = 0; h < cntitles.length; h++) {
                    Field fd = item.getClass().getDeclaredField(egtitles[h]);
                    fd.setAccessible(true);
                    Object o = fd.get(item);
                    String value = o == null ? "" : o.toString();
//                    HSSFCell cell = row.createCell(h);
                    XSSFCell cell = row.createCell(h);
                    cell.setCellValue(value);
                }
                rownum++;
            }
        }
        // 将创建好的数据写入输出流
        workbook.write(out);
        // 关闭workbook
        workbook.close();
    }


    public static <T> List<T> ExecltoList(InputStream in, Class<T> entityClass,
                                          Map<String, String> fields) throws Exception {

        List<T> resultList = new ArrayList<T>();

        XSSFWorkbook workbook = new XSSFWorkbook(in);
        // excel中字段的中英文名字数组
        String[] egtitles = new String[fields.size()];
        String[] cntitles = new String[fields.size()];
        Iterator<String> it = fields.keySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            String cntitle = (String) it.next();
            String egtitle = fields.get(cntitle);
            egtitles[count] = egtitle;
            cntitles[count] = cntitle;
            count++;
        }

        // 得到excel中sheet总数
        int sheetcount = workbook.getNumberOfSheets();

        if (sheetcount == 0) {
            workbook.close();
            throw new Exception("Excel文件中没有任何数据");
        }

        // 数据的导出
        for (int i = 0; i < sheetcount; i++) {
            XSSFSheet sheet = workbook.getSheetAt(i);
            if (sheet != null) {
                // 每页中的第一行为标题行，对标题行的特殊处理
                XSSFRow firstRow = sheet.getRow(0);
                int celllength = firstRow.getPhysicalNumberOfCells();
                String[] excelFieldNames = new String[celllength];
                LinkedHashMap<String, Integer> colMap = new LinkedHashMap<String, Integer>();
                // 获取Excel中的列名
                for (int f = 0; f < celllength; f++) {
                    XSSFCell cell = firstRow.getCell(f);
                    excelFieldNames[f] = cell.getStringCellValue().trim();
                    // 将列名和列号放入Map中,这样通过列名就可以拿到列号
                    for (int g = 0; g < excelFieldNames.length; g++) {
                        colMap.put(excelFieldNames[g], g);
                    }
                }
                // 由于数组是根据长度创建的，所以值是空值，这里对列名map做了去空键的处理
                colMap.remove(null);
                // 判断需要的字段在Excel中是否都存在
                // 需要注意的是这个方法中的map中：中文名为键，英文名为值
                boolean isExist = true;
                List<String> excelFieldList = Arrays.asList(excelFieldNames);
                for (String cnName : fields.keySet()) {
                    if (!excelFieldList.contains(cnName)) {
                        isExist = false;
                        break;
                    }
                }
                // 如果有列名不存在，则抛出异常，提示错误
                if (!isExist) {
                    workbook.close();
                    throw new Exception("Excel中缺少必要的字段，或字段名称有误");
                }
                // 将sheet转换为list
                for (int j = 1; j <= sheet.getLastRowNum(); j++) {
//                XHSSFRow row = sheet.getRow(j);
                    XSSFRow row = sheet.getRow(j);
                    // 根据泛型创建实体类
                    T entity = entityClass.newInstance();
                    // 给对象中的字段赋值
                    for (Map.Entry<String, String> entry : fields.entrySet()) {
                        // 获取中文字段名
                        String cnNormalName = entry.getKey();
                        // 获取英文字段名
                        String enNormalName = entry.getValue();
                        // 根据中文字段名获取列号
                        int col = colMap.get(cnNormalName);
                        // 获取当前单元格中的内容
                        String content = row.getCell(col).toString().trim();
                        // 给对象赋值
                        setFieldValueByName(enNormalName, content, entity);
                    }
                    System.out.println(entity.toString());
                    resultList.add(entity);
                }
            }

        }
        workbook.close();
        return resultList;
    }

    /**
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @param o          对象
     * @MethodName : setFieldValueByName
     * @Description : 根据字段名给对象的字段赋值
     */
    private static void setFieldValueByName(String fieldName,
                                            Object fieldValue, Object o) throws Exception {

        //excel 字段不变顺序可以直接来进行判断及校验
        Field field = getFieldByName(fieldName, o.getClass());
        if (field != null) {
            field.setAccessible(true);
            // 获取字段类型
            Class<?> fieldType = field.getType();

            // 根据字段类型给字段赋值
            if (String.class == fieldType) {
                field.set(o, String.valueOf(fieldValue));
            } else if ((Integer.TYPE == fieldType)
                    || (Integer.class == fieldType)) {
                field.set(o, Integer.parseInt(fieldValue.toString()));
            } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                field.set(o, Long.valueOf(fieldValue.toString()));
            } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                field.set(o, Float.valueOf(fieldValue.toString()));
            } else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
                field.set(o, Short.valueOf(fieldValue.toString()));
            } else if ((Double.TYPE == fieldType)
                    || (Double.class == fieldType)) {
                field.set(o, Double.valueOf(fieldValue.toString()));
            } else if (Character.TYPE == fieldType) {
                if ((fieldValue != null)
                        && (fieldValue.toString().length() > 0)) {
                    field.set(o,
                            Character.valueOf(fieldValue.toString().charAt(0)));
                }
            } else if (Date.class == fieldType) {
                field.set(o, new SimpleDateFormat("yyyy-MM-dd")
                        .parse(fieldValue.toString()));
            } else {
                field.set(o, fieldValue);
            }
        } else {
            throw new Exception(o.getClass().getSimpleName() + "类不存在字段名 "
                    + fieldName);
        }
    }

    /**
     * @param fieldName 字段名
     * @param clazz     包含该字段的类
     * @return 字段
     * @MethodName : getFieldByName
     * @Description : 根据字段名获取字段
     */
    private static Field getFieldByName(String fieldName, Class<?> clazz) {
        // 拿到本类的所有字段
        Field[] selfFields = clazz.getDeclaredFields();

        // 如果本类中存在该字段，则返回
        for (Field field : selfFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        // 否则，查看父类中是否存在此字段，如果有则返回
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {
            return getFieldByName(fieldName, superClazz);
        }

        // 如果本类和父类都没有，则返回空
        return null;
    }

    public void exportExcel(String title, String[] headers, Integer[] widths, Collection<T> dataSet, String pattern, HttpServletRequest request, HttpServletResponse response, String fileName) {
        String agent = request.getHeader("User-Agent").toLowerCase();
        // 声明一个工作簿
        HSSFWorkbook workBook = new HSSFWorkbook();

        // 生成一个表格
        buildSheet(title, headers, widths, dataSet, pattern, workBook);
        try {
            String finalFileName = null;
            if (agent.indexOf("msie") > 0) {//IE浏览器
                finalFileName = URLEncoder.encode(fileName, "UTF8").concat(".xls");
            } else if (agent.indexOf("firefox") > 0) {//火狐浏览器
                finalFileName = new String(fileName.getBytes(), "ISO8859-1").concat(".xls");
            } else {
                finalFileName = URLEncoder.encode(fileName, "UTF8").concat(".xls");//其他浏览器
            }
            response.setHeader("content-disposition", "attachment;filename=" + finalFileName);
            workBook.write(response.getOutputStream());
        } catch (IOException e) {
            log.error("导出文件流时异常！ e:", e);
            e.printStackTrace();
        }
    }

    /**
     * 添加返回值，用于合并单元格
     */
    public HSSFSheet buildMutilSheetExcel(String sheetName, String[] headers, Integer[] widths, Collection<T> dataSet, String pattern, HSSFWorkbook workBook) {
        // 生成一个表格
        return buildSheet(sheetName, headers, widths, dataSet, pattern, workBook);
    }

    /**
     * 合并某个sheet里的单元格
     */
    public void createMergeRow(HSSFSheet sheet, List<CellRangeAddress> mergerAddresses) {
        for (CellRangeAddress address : mergerAddresses) {
            sheet.addMergedRegion(address);
        }
    }


    /**
     * 创建一个sheet
     *
     * @author
     */
    private HSSFSheet buildSheet(String title, String[] headers, Integer[] widths, Collection<T> dataSet, String pattern,
                                 HSSFWorkbook workBook) {
        HSSFSheet sheet = workBook.createSheet(title);
        // 设置表格默认列宽度为15字节
        sheet.autoSizeColumn(1);
        // 生成并设置一个样式
        HSSFCellStyle style = this.getColumnTopStyle(workBook);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = this.getStyle(workBook);
        //声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 定义注释的大小和位置,详见文档
        HSSFComment comment = patriarch.createCellComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
        // 设置注释内容
        comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
//        comment.setAuthor("");
        // 产生表格标题行r
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            sheet.setColumnWidth(i, widths[i] * 256);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        // 遍历集合数据，产生数据行
        Iterator<T> it = dataSet.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = it.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();
            for (short i = 0; i < fields.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style2);
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                try {
                    Class<? extends Object> tCls = t.getClass();
                    Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                    Object value = getMethod.invoke(t, new Object[]{});
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;
                    if (value instanceof Boolean) {
                        boolean bValue = (Boolean) value;
                        textValue = "是";
                        if (!bValue) {
                            textValue = "否";
                        }
                    } else if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);
                    } else if (value instanceof byte[]) {
                        // 有图片时，设置行高为60px;
                        row.setHeightInPoints(60);
                        // 设置图片所在列宽度为80px,注意这里单位的一个换算
                        sheet.setColumnWidth(i, (short) (35.7 * 80));
                        // sheet.autoSizeColumn(i);
                        byte[] bsValue = (byte[]) value;
                        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6, index);
                        anchor.setAnchorType(2);
                        patriarch.createPicture(anchor, workBook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                    } else if (value == null) {
                        System.out.println("null");
                    } /*else if(value instanceof Long){


                    	Date date = new Date((Long)value);
                    	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);
                    }*/ else {
                        // 其它数据类型都当作字符串简单处理
                        textValue = value.toString();
                    }
                    // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                    if (textValue != null) {
                        /*Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
                        Matcher matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {*/
                        HSSFRichTextString richString = new HSSFRichTextString(textValue);
                        HSSFFont font3 = workBook.createFont();
                        HSSFColor thiscolro = new HSSFColor();
                        richString.applyFont(font3);
                        cell.setCellValue(richString);
                        /*}*/
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } finally {
                    // 清理资源
                }
            }
        }
        return sheet;
    }


    /*
     * 列头单元格样式
     */
    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short) 11);
        //字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        //设置左边框;
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        //设置右边框;
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        //设置顶边框;
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);


        return style;

    }

    /*
     * 列数据信息单元格样式
     */
    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        //font.setFontHeightInPoints((short)10);
        //字体加粗
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        //设置左边框;
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        //设置右边框;
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        //设置顶边框;
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return style;

    }
}
