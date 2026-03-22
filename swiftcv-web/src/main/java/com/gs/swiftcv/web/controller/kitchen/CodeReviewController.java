package com.gs.swiftcv.web.controller.kitchen;

import com.gs.swiftcv.service.model.KitchenCategoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : gengsheng
 * @Description:
 * @Date: 2026/3/22 19:54
 */
@RestController
@RequestMapping("codeReview")
@Slf4j
public class CodeReviewController {


    @GetMapping("test")
    public void test() {
        KitchenCategoryVO kitchenCategoryVO = null;
        String description = kitchenCategoryVO.getDescription();
        List<String> list = new ArrayList<>();
        list.add(description);
        while (true) {
            for (String s : list) {
                log.error(s);
            }
        }
    }
}
