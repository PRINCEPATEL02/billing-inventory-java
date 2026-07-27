package com.billing.service;

import com.billing.entity.*;
import com.billing.repository.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.LinkedHashMap;

@Service
public class ReportService {
 private final BillRepository bills; private final PurchaseRepository purchases; private final ProductRepository products;
 public ReportService(BillRepository bills, PurchaseRepository purchases, ProductRepository products){this.bills=bills;this.purchases=purchases;this.products=products;}
 public Map<String,Object> summary(String type, LocalDate from, LocalDate to){
  LocalDate start=from==null?defaultStart(type):from, end=to==null?LocalDate.now():to; LocalDateTime s=start.atStartOfDay(), e=end.plusDays(1).atStartOfDay();
  java.util.List<Bill> sale=bills.findBillsBetween(s,e); java.util.List<Purchase> buy=purchases.filter(start,end,null,null);
  BigDecimal sales=sale.stream().map(Bill::getGrandTotal).reduce(BigDecimal.ZERO,BigDecimal::add), costs=buy.stream().map(Purchase::getTotalCost).reduce(BigDecimal.ZERO,BigDecimal::add), gst=buy.stream().map(p->p.getTotalCost().subtract(p.getPurchasePrice().multiply(BigDecimal.valueOf(p.getQuantity())))).reduce(BigDecimal.ZERO,BigDecimal::add);
  Map<String,Object> r=new LinkedHashMap<>(); r.put("reportType",type.toUpperCase());r.put("from",start);r.put("to",end);r.put("sales",sales);r.put("purchaseCost",costs);r.put("gstOnPurchases",gst);r.put("profit",sales.subtract(costs));r.put("salesCount",sale.size());r.put("purchaseCount",buy.size());r.put("inventoryValue",products.findByActiveTrue().stream().map(p->p.getPurchasePrice().multiply(BigDecimal.valueOf(p.getQuantity()))).reduce(BigDecimal.ZERO,BigDecimal::add)); return r;
 }
 public byte[] excel(String type, LocalDate from, LocalDate to) { Map<String,Object> data=summary(type,from,to); try(XSSFWorkbook wb=new XSSFWorkbook(); ByteArrayOutputStream out=new ByteArrayOutputStream()){var sheet=wb.createSheet("Report");int row=0; for(var entry:data.entrySet()){var r=sheet.createRow(row++);r.createCell(0).setCellValue(entry.getKey());r.createCell(1).setCellValue(String.valueOf(entry.getValue()));}sheet.autoSizeColumn(0);sheet.autoSizeColumn(1);wb.write(out);return out.toByteArray();} catch(IOException e) { throw new IllegalStateException("Unable to create Excel report", e); }}
 public byte[] pdf(String type, LocalDate from, LocalDate to){Map<String,Object> data=summary(type,from,to);try(ByteArrayOutputStream out=new ByteArrayOutputStream()){PdfDocument pdf=new PdfDocument(new PdfWriter(out));Document doc=new Document(pdf);doc.add(new Paragraph(type.toUpperCase()+" REPORT").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));Table table=new Table(new float[]{2,3}).useAllAvailableWidth();for(var e:data.entrySet()){table.addCell(e.getKey());table.addCell(String.valueOf(e.getValue()));}doc.add(table);doc.close();return out.toByteArray();}catch(IOException e){throw new IllegalStateException("Unable to create PDF",e);}}
 private LocalDate defaultStart(String type){return switch(type.toLowerCase()){case "daily"->LocalDate.now();case "weekly"->LocalDate.now().minusDays(6);case "monthly"->LocalDate.now().withDayOfMonth(1);case "yearly"->LocalDate.now().withDayOfYear(1);default->LocalDate.now().minusDays(30);};}
}
