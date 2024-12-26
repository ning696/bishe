package com.gxyan.service.impl;

import java.util.List;

import com.gxyan.domain.Announcement;
import com.gxyan.dao.AnnouncementMapper;
import com.gxyan.service.IAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 公告Service业务层处理
 *
 * @author ruoyi
 * @date 2024-11-13
 */
@Service
public class AnnouncementServiceImpl implements IAnnouncementService
{
    @Autowired
    private AnnouncementMapper announcementMapper;

    /**
     * 查询公告
     *
     * @param id 公告主键
     * @return 公告
     */
    @Override
    public Announcement selectAnnouncementById(Long id)
    {
        return announcementMapper.selectAnnouncementById(id);
    }

    /**
     * 查询公告列表
     *
     * @param announcement 公告
     * @return 公告
     */
    @Override
    public List<Announcement> selectAnnouncementList(Announcement announcement)
    {
        return announcementMapper.selectAnnouncementList(announcement);
    }

    /**
     * 新增公告
     *
     * @param announcement 公告
     * @return 结果
     */
    @Override
    public int insertAnnouncement(Announcement announcement)
    {
        return announcementMapper.insertAnnouncement(announcement);
    }

    /**
     * 修改公告
     *
     * @param announcement 公告
     * @return 结果
     */
    @Override
    public int updateAnnouncement(Announcement announcement)
    {
        return announcementMapper.updateAnnouncement(announcement);
    }

    /**
     * 批量删除公告
     *
     * @param ids 需要删除的公告主键
     * @return 结果
     */
    @Override
    public int deleteAnnouncementByIds(Long[] ids)
    {
        return announcementMapper.deleteAnnouncementByIds(ids);
    }

    /**
     * 删除公告信息
     *
     * @param id 公告主键
     * @return 结果
     */
    @Override
    public int deleteAnnouncementById(Long id)
    {
        return announcementMapper.deleteAnnouncementById(id);
    }
}
