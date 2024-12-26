package com.gxyan.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.gxyan.common.ServerResponse;
import com.gxyan.domain.AjaxResult;
import com.gxyan.pojo.Car;
import com.gxyan.pojo.TableDataInfo;
import com.gxyan.service.IStoreService;
import com.gxyan.vo.StoreQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gxyan.pojo.Promotion;
import com.gxyan.service.IPromotionService;

/**
 * 宣传Controller
 *
 * @author ruoyi
 * @date 2024-11-14
 */
@RestController
@RequestMapping("/promotion")
public class PromotionController extends BaseController
{
    @Autowired
    private IPromotionService promotionService;
    @Autowired
    private IStoreService storeService;
    /**
     * 查询宣传列表
     */
//    @GetMapping("/list")
//    public TableDataInfo list(Promotion promotion)
//    {
//        startPage();
//        List<Promotion> list = promotionService.selectPromotionList(promotion);
//        return getDataTable(list);
//    }
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ServerResponse getList(StoreQuery storeQuery) {
        ServerResponse list = promotionService.getList(storeQuery);
        return list;
    }

    /**
     * 导出宣传列表
     */
//
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, Promotion promotion)
//    {
//        List<Promotion> list = promotionService.selectPromotionList(promotion);
//        ExcelUtil<Promotion> util = new ExcelUtil<Promotion>(Promotion.class);
//        util.exportExcel(response, list, "宣传数据");
//    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ServerResponse update(@RequestBody Map<String, Object> params) {
        Long aLong = Long.valueOf(params.get("id").toString());
        String promotionText = (String) params.get("promotionText");
        return promotionService.updatepromotion(aLong, promotionText);
    }
    /**
     * 获取宣传详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(promotionService.selectPromotionById(id));
    }

    /**
     * 新增宣传
     */
    @PostMapping
    public AjaxResult add(@RequestBody Promotion promotion)
    {
        return toAjax(promotionService.insertPromotion(promotion));
    }

    /**
     * 修改宣传
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Promotion promotion)
    {
        return toAjax(promotionService.updatePromotion(promotion));
    }

    /**
     * 删除宣传
     */
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        int i = ;
//        if (i > 0) {
//            return success();
//        } else {
//            return error();
//        }
//        promotionService.deletePromotionByIds(ids)>0?ServerResponse.createBySuccess("修改成功"):ServerResponse.createByErrorMessage("修改失败");
//        return toAjax(i);
//    }
    @DeleteMapping("/{ids}")
    public ServerResponse remove(@PathVariable Long[] ids)
    {

        return promotionService.deletePromotionByIds(ids)>0?ServerResponse.createBySuccess("修改成功"):ServerResponse.createByErrorMessage("修改失败");
    }
}
