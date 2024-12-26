//package com.gxyan.controller;
//
//import java.util.List;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import com.ruoyi.common.annotation.Log;
//import com.ruoyi.common.core.controller.BaseController;
//import com.ruoyi.common.core.domain.AjaxResult;
//import com.ruoyi.common.enums.BusinessType;
//import com.gxyan.domain.TestDrive;
//import com.gxyan.service.ITestDriveService;
//import com.ruoyi.common.utils.poi.ExcelUtil;
//import com.ruoyi.common.core.page.TableDataInfo;
//
///**
// * driveController
// *
// * @author coll
// * @date 2024-11-11
// */
//@RestController
//@RequestMapping("/system/drive")
//public class TestDriveController extends BaseController
//{
//    @Autowired
//    private ITestDriveService testDriveService;
//
//    /**
//     * 查询drive列表
//     */
//    @PreAuthorize("@ss.hasPermi('system:drive:list')")
//    @GetMapping("/list")
//    public TableDataInfo list(TestDrive testDrive)
//    {
//        startPage();
//        List<TestDrive> list = testDriveService.selectTestDriveList(testDrive);
//        return getDataTable(list);
//    }
//
//    /**
//     * 导出drive列表
//     */
//    @PreAuthorize("@ss.hasPermi('system:drive:export')")
//    @Log(title = "drive", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, TestDrive testDrive)
//    {
//        List<TestDrive> list = testDriveService.selectTestDriveList(testDrive);
//        ExcelUtil<TestDrive> util = new ExcelUtil<TestDrive>(TestDrive.class);
//        util.exportExcel(response, list, "drive数据");
//    }
//
//    /**
//     * 获取drive详细信息
//     */
//    @PreAuthorize("@ss.hasPermi('system:drive:query')")
//    @GetMapping(value = "/{id}")
//    public AjaxResult getInfo(@PathVariable("id") Long id)
//    {
//        return success(testDriveService.selectTestDriveById(id));
//    }
//
//    /**
//     * 新增drive
//     */
//    @PreAuthorize("@ss.hasPermi('system:drive:add')")
//    @Log(title = "drive", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody TestDrive testDrive)
//    {
//        return toAjax(testDriveService.insertTestDrive(testDrive));
//    }
//
//    /**
//     * 修改drive
//     */
//    @PreAuthorize("@ss.hasPermi('system:drive:edit')")
//    @Log(title = "drive", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody TestDrive testDrive)
//    {
//        return toAjax(testDriveService.updateTestDrive(testDrive));
//    }
//
//    /**
//     * 删除drive
//     */
//    @PreAuthorize("@ss.hasPermi('system:drive:remove')")
//    @Log(title = "drive", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(testDriveService.deleteTestDriveByIds(ids));
//    }
//}
