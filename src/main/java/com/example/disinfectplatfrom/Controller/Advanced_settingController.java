package com.example.disinfectplatfrom.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : Advanced_settingController
 * @description : 高级设置controller
 * @createTime : 2023/6/25 20:31
 * @updateUser : Lin
 * @updateTime : 2023/6/25 20:31
 * @updateRemark : 描述说明本次修改内容
 */
@RestController
@PreAuthorize("hasAuthority('advanced_setting')")
@RequestMapping("advanced_setting/")
public class Advanced_settingController {
}
