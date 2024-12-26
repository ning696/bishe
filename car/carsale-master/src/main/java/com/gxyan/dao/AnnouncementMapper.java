package com.gxyan.dao;

import com.gxyan.domain.Announcement;

import java.util.List;

/**
 * 公告Mapper接口
 *
 * @author ruoyi
 * @date 2024-11-13
 */
public interface AnnouncementMapper
{
    /**
     * 查询公告
     *
     * @param id 公告主键
     * @return 公告
     */
    public Announcement selectAnnouncementById(Long id);

    /**
     * 查询公告列表
     *
     * @param announcement 公告
     * @return 公告集合
     */
    public List<Announcement> selectAnnouncementList(Announcement announcement);

    /**
     * 新增公告
     *
     * @param announcement 公告
     * @return 结果
     */
    public int insertAnnouncement(Announcement announcement);

    /**
     * 修改公告
     *
     * @param announcement 公告
     * @return 结果
     */
    public int updateAnnouncement(Announcement announcement);

    /**
     * 删除公告
     *
     * @param id 公告主键
     * @return 结果
     */
    public int deleteAnnouncementById(Long id);

    /**
     * 批量删除公告
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAnnouncementByIds(Long[] ids);
}
