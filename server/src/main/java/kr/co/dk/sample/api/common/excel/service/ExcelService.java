package kr.co.dk.sample.api.common.excel.service;

import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.common.model.exception.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class ExcelService {
    private static final Logger log = LogManager.getLogger(ExcelService.class);

    public String getStringOfCell(Cell cell) {
        String value = "";
        switch (cell.getCellType()) {
            case FORMULA:
                value = String.valueOf(cell.getCellFormula());
                break;
            case NUMERIC:
                value = new BigDecimal(String.valueOf(cell.getNumericCellValue())).toPlainString();
                break;
            case STRING:
                value = String.valueOf(cell.getStringCellValue());
                break;
            case BLANK:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case ERROR:
                value = String.valueOf(cell.getErrorCellValue());
        }
        return value;
    }

    public Workbook getWorkBook(MultipartFile excel) throws CustomException, IOException {
        Workbook workbook;
        String extension = StringUtils.getFilenameExtension(excel.getOriginalFilename());
        if (extension.equalsIgnoreCase("xlsx")) {
            workbook = new XSSFWorkbook(excel.getInputStream());
        } else if (extension.equalsIgnoreCase("xls")) {
            workbook = new HSSFWorkbook(excel.getInputStream());
        } else {
            throw new CustomException(ErrorCode.CODE_1504);
        }
        return workbook;
    }
}
