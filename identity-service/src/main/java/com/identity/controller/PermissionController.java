package com.identity.controller;

import com.identity.dto.request.PermissionReq;
import com.identity.handler.ResponseHandler;
import com.identity.service.IPermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/permissions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    IPermissionService permissionService;

    @PostMapping
    public ResponseEntity<Object> createPermission(@RequestBody PermissionReq permissionReq) {
        return ResponseHandler.execute(
                permissionService.createPermission(permissionReq)
        );
    }

    @DeleteMapping("/{permission}")
    public ResponseEntity<Object> deletePermission(@PathVariable String permission) {
        permissionService.deletePermission(permission);
        return ResponseHandler.execute();
    }

    @GetMapping("/{permission}")
    public ResponseEntity<Object> getPermission(@PathVariable String permission) {
        return ResponseHandler.execute(
                permissionService.getPermission(permission)
        );
    }

    @GetMapping
    public ResponseEntity<Object> getAllPermission() {
        return ResponseHandler.execute(
                permissionService.getAllPermission()
        );
    }
}
