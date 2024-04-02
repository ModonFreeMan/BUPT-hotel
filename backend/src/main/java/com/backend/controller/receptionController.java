package com.backend.controller;


import com.backend.pojo.Customer;
import com.backend.pojo.Result;
import com.backend.service.ReceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reception")
public class receptionController {
    @Autowired
    private ReceptionService receptionService;

    @PostMapping("/checkin")
    public Result customerCheckIn(@RequestBody @Validated Customer customer){
        receptionService.checkin(customer);
        return Result.success();
    }








}
