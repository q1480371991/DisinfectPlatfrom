package com.example.disinfectplatfrom.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : Main_consoleController
 * @description : 主控制台controller
 * @createTime : 2023/6/25 20:30
 * @updateUser : Lin
 * @updateTime : 2023/6/25 20:30
 * @updateRemark : 描述说明本次修改内容
 */
@RestController
@PreAuthorize("hasAuthority('main_console')")
@RequestMapping("/main_console")
public class Main_consoleController {

}
