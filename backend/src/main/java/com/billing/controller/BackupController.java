package com.billing.controller;
import com.billing.dto.*; import com.billing.service.BackupService; import org.springframework.http.*; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/backups") @PreAuthorize("hasRole('ADMIN')") public class BackupController {
 private final BackupService backups; public BackupController(BackupService backups){this.backups=backups;}
 @GetMapping public ResponseEntity<BackupPayload> download(){return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=inventory-backup.json").contentType(MediaType.APPLICATION_JSON).body(backups.create());}
 @PostMapping("/restore") public ResponseEntity<ApiResponse<Integer>> restore(@RequestBody BackupPayload payload){return ResponseEntity.ok(ApiResponse.success("Backup restored",backups.restore(payload)));}
}
