package com.example.backend.controller.user;

import com.example.backend.ApiResult.ApiResult;
import com.example.backend.model.ClickItem;
import com.example.backend.service.ClickItemService;
import com.example.backend.utils.MapResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/clickItem")
public class ClickItemController {
    @Autowired
    private ClickItemService clickItemService;
    @Autowired
    private MapResult mapResult;
    @PostMapping("/{identified}")
    public ResponseEntity<ApiResult<ClickItem>> addItemToList(@PathVariable String identified,
                                                              @RequestParam String productId){
        ApiResult<ClickItem> apiResult = mapResult.map(clickItemService.addItemToClickList(identified, productId), "Add item to click list successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @GetMapping("/{identified}")
    public ResponseEntity<ApiResult<ClickItem>> getListClickItem(@PathVariable String identified){
        ApiResult<ClickItem> apiResult = mapResult.map(clickItemService.getAllClickItem(identified), "Get item from click list successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
}
