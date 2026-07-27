package com.billing.controller;
import com.billing.dto.ApiResponse; import com.billing.service.ReportService; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.time.LocalDate; import java.util.Map;
@RestController @RequestMapping("/api/reports") public class ReportController {
 private final ReportService reports; public ReportController(ReportService reports){this.reports=reports;}
 @GetMapping("/{type}") public ResponseEntity<ApiResponse<Map<String,Object>>> summary(@PathVariable String type,@RequestParam(required=false) LocalDate from,@RequestParam(required=false) LocalDate to){return ResponseEntity.ok(ApiResponse.success(reports.summary(type,from,to)));}
 @GetMapping("/{type}/export") public ResponseEntity<byte[]> export(@PathVariable String type,@RequestParam String format,@RequestParam(required=false) LocalDate from,@RequestParam(required=false) LocalDate to){
  boolean pdf="pdf".equalsIgnoreCase(format); byte[] body;
  try { body = pdf ? reports.pdf(type,from,to) : reports.excel(type,from,to); }
  catch (RuntimeException e) { throw e; }
  String extension=pdf?"pdf":"xlsx";
  return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+type+"-report."+extension).contentType(MediaType.parseMediaType(pdf?"application/pdf":"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body(body);
 }
}
