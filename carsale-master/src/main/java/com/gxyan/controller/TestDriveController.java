package com.gxyan.controller;

import java.util.List;
import java.util.Map;

import com.gxyan.common.Const;
import com.gxyan.common.ServerResponse;
import com.gxyan.domain.AjaxResult;
import com.gxyan.pojo.Customer;
import com.gxyan.pojo.Employee;
import com.gxyan.pojo.TableDataInfo;
import com.gxyan.pojo.dto.TestDrive;
import com.gxyan.service.ITestDriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static com.github.pagehelper.page.PageMethod.startPage;

/**
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2024-11-09
 */
@RestController
@RequestMapping("testdrive")
public class TestDriveController extends BaseController
{
    @Autowired
    private ITestDriveService testDriveService;

    /**
     * 查询【请填写功能名称】列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TestDrive testDrive)
    {
        startPage();
        List<TestDrive> list = testDriveService.selectTestDriveList(testDrive);
        return getDataTable(list);
    }

//    /**
//     * 导出【请填写功能名称】列表
//     */
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, TestDrive testDrive)
//    {
//        List<TestDrive> list = testDriveService.selectTestDriveList(testDrive);
//        ExcelUtil<TestDrive> util = new ExcelUtil<TestDrive>(TestDrive.class);
//        util.exportExcel(response, list, "【请填写功能名称】数据");
//    }
//
//    /**
//     * 获取【请填写功能名称】详细信息
//     */
    @RequestMapping(value = "userdriver", method = RequestMethod.POST)
    public ServerResponse info(HttpSession session) {
        Object user = session.getAttribute(Const.CURRENT_USER);
        if (user instanceof Employee) {
            return ServerResponse.createBySuccess((Employee) user);
        } else if (user instanceof Customer) {
            Customer customer = (Customer) user;
            List<TestDrive> testDrive = testDriveService.getTestDrive(customer.getId());
            return ServerResponse.createBySuccess(testDrive);
        }
        return ServerResponse.createByErrorMessage("无法获取用户信息");
    }
    /**
     * 新增【请填写功能名称】
     */
    @RequestMapping(value = "bookTestDrive", method = RequestMethod.POST)
    public ServerResponse add(@RequestBody TestDrive testDrive)
    {
        testDriveService.insertTestDrive(testDrive);
        return  ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "refuse", method = RequestMethod.POST)
    public ServerResponse refuse(@RequestBody Map<String, Object> data) {
        String ids = data.get("ids").toString();
        TestDrive testDrive = new TestDrive();
        testDrive.setStatus("3");
        testDrive.setId(Long.parseLong(ids));
        testDriveService.updateTestDrive(testDrive);
        return  ServerResponse.createBySuccess();
    }
    /**
     * 修改【请填写功能名称】
     */
    @PutMapping
    public AjaxResult edit(@RequestBody TestDrive testDrive)
    {
        return toAjax(testDriveService.updateTestDrive(testDrive));
    }
//
//    /**
//     * 删除【请填写功能名称】
//     */
//	@DeleteMapping("/{ids}")
//    public ServerResponse remove(@PathVariable Long[] ids)
//    {
//        return toAjax(testDriveService.deleteTestDriveByIds(ids));
//    }
}
