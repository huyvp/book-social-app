package com.identity.controller;

import com.identity.dto.request.PasswordCreateReq;
import com.identity.dto.request.UserReq;
import com.identity.dto.response.UserResponse;
import com.identity.handler.ResponseHandler;
import com.identity.service.IUserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin
public class UserController {
    IUserService userService;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserReq userReq) {
        return ResponseHandler.execute(
                userService.createUser(userReq)
        );
    }

    @PutMapping("{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable String userId,
                                             @RequestBody UserReq userReq) {
        return ResponseHandler.execute(
                userService.updateUser(userId, userReq)
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseHandler.execute();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") String id) {
        return ResponseHandler.execute(
                userService.getUserById(id)
        );
    }

    @GetMapping
    public ResponseEntity<Object> getAllUser(@RequestParam("page") int page,
                                             @RequestParam("limit") int limit) {
        List<UserResponse> users = userService.getAllUsers(
                PageRequest.of(page, limit, Sort.by("id").ascending())
        );
        return ResponseHandler.execute(users, users.size());
    }

    @GetMapping("/myInfo")
    public ResponseEntity<Object> getMyInfo() {
        return ResponseHandler.execute(
                userService.getMyInfo()
        );
    }

    @PostMapping("/create-password")
    public ResponseEntity<Object> createCreatePassword(@Valid @RequestBody PasswordCreateReq req) {
        userService.createPassword(req);
        return ResponseHandler.execute("Create password successful!");
    }
}
