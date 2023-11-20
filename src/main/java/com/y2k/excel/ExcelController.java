package com.y2k.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.ImmutableList;
import com.y2k.device.DeviceModel;
import com.y2k.device.DeviceService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ExcelController {

    private Logger logger = LoggerFactory.getLogger(ExcelController.class);

    private final DeviceService deviceService;

    /**
     * 엑셀 파일 읽기
     * @return
     */
    @ResponseBody
    @PostMapping("readExcel")
    public String readExcel() {
        String filePath = "c:/testFile/test_excel.xls";

        try ( FileInputStream file = new FileInputStream( filePath );
              Workbook workbook = WorkbookFactory.create( file ); ) {
            
            IOUtils.setByteArrayMaxOverride( Integer.MAX_VALUE );

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            Iterator<Cell> header = rowIterator.next().cellIterator();

            List<Cell> Colheader = ImmutableList.copyOf(header);

            System.out.println(header.toString());
            System.out.println("###");

            while ( rowIterator.hasNext() ) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                while ( cellIterator.hasNext() ) {
                    Cell cell = cellIterator.next();

                    switch ( cell.getCellType() ) {
                        case NUMERIC:
                            System.out.print( cell.getNumericCellValue() );
                            break;
                    
                        case STRING:
                            System.out.print( cell.getStringCellValue() );
                            break;

                        default:
                            throw new IllegalStateException( "예기치 않은 값 : " + cell.getCellType() );
                    }
                    System.out.print(" ");
                }
                System.out.println("");
            }
            
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return "엑셀 업로드";
    }
    
    /**
     * 엑셀 파일 다운로드
     * @return
     * @throws IOException
     */
    @ResponseBody
    @GetMapping("downloadExcel")
    public ResponseEntity<InputStreamResource> downloadExcel(HttpServletResponse response) throws IOException {
        String[] header = { "기기ID", "기기유형", "기기이름", "기기사용지점", "기기상태", "기기사용유무", "기기마지막접근시간" };
        
        try ( Workbook workbook = new XSSFWorkbook(); ) {
            Sheet sheet = workbook.createSheet("기기현황 A");
            int rowNo = 0;

            CellStyle headStyle = workbook.createCellStyle();
            headStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.BLUE_GREY.getIndex());
            headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            Font font = workbook.createFont();
            font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
            // font.setFontHeightInPoints((short) 1db3);
            headStyle.setFont(font);

            Row headerRow = sheet.createRow(rowNo++);
            for ( int i = 0; i < header.length; i++ ) {
                headerRow.createCell(i).setCellValue(header[i]);
                headerRow.getCell(i).setCellStyle(headStyle);
            }

            List<DeviceModel> dvcList = deviceService.getDvcList();
            for ( DeviceModel dvc : dvcList) {
                Row row = sheet.createRow(rowNo++);
                row.createCell(0).setCellValue(dvc.getDvcId());
                row.createCell(1).setCellValue(dvc.getDvcType());
                row.createCell(2).setCellValue(dvc.getDvcName());
                row.createCell(3).setCellValue(dvc.getDvcBrc());
                row.createCell(4).setCellValue(dvc.getDvcStts());
                row.createCell(5).setCellValue(dvc.getDvcUseYn());
                row.createCell(6).setCellValue(dvc.getDvcLastAcsTime());
            }

            response.setContentType("ms-vnd/excel");
            response.setHeader("Content-Disposition", "attachment;filename=deviceList.xlsx");
    
            workbook.write(response.getOutputStream());

        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return null;
    }
}
