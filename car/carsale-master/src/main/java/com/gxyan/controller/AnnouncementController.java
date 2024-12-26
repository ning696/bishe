package com.gxyan.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.gxyan.domain.AjaxResult;
import com.gxyan.domain.Announcement;
import com.gxyan.pojo.TableDataInfo;
import com.gxyan.service.IAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公告Controller
 *
 * @author ruoyi
 * @date 2024-11-13
 */
@RestController
@RequestMapping("/announcement")
public class AnnouncementController extends BaseController
{
    @Autowired
    private IAnnouncementService announcementService;

    /**
     * 查询公告列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Announcement announcement)
    {
        startPage();
        List<Announcement> list = announcementService.selectAnnouncementList(announcement);
        return getDataTable(list);
    }

    /**
     * 导出公告列表
     */
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, Announcement announcement)
//    {
//        List<Announcement> list = announcementService.selectAnnouncementList(announcement);
//        ExcelUtil<Announcement> util = new ExcelUtil<Announcement>(Announcement.class);
//        util.exportExcel(response, list, "公告数据");
//    }

    /**
     * 获取公告详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(announcementService.selectAnnouncementById(id));
    }

    /**
     * 新增公告
     */
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody Announcement announcement)
    {
        return toAjax(announcementService.insertAnnouncement(announcement));
    }

    /**
     * 修改公告
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Announcement announcement)
    {
        return toAjax(announcementService.updateAnnouncement(announcement));
    }

    /**
     * 删除公告
     */
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(announcementService.deleteAnnouncementByIds(ids));
    }
}
