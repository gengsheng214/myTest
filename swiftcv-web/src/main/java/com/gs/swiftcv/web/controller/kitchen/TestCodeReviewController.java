package com.gs.swiftcv.web.controller.kitchen;

import com.gs.swiftcv.service.model.KitchenCategoryVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : gengsheng
 * @Description:
 * @Date: 2026/3/22 19:54
 */
public class TestCodeReviewController {


    public void codeReviewTest() {
        KitchenCategoryVO kitchenCategoryVO = null;
        String description = kitchenCategoryVO.getDescription();
        List<String> list = new ArrayList<>();
        list.add(description);
    }
}
