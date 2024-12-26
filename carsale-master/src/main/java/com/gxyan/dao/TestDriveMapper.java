package com.gxyan.dao;

import com.gxyan.pojo.dto.TestDrive;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2024-11-09
 */
public interface TestDriveMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public TestDrive selectTestDriveById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param testDrive 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TestDrive> selectTestDriveList(TestDrive testDrive);

    /**
     * 新增【请填写功能名称】
     *
     * @param testDrive 【请填写功能名称】
     * @return 结果
     */
    public int insertTestDrive(TestDrive testDrive);

    /**
     * 修改【请填写功能名称】
     *
     * @param testDrive 【请填写功能名称】
     * @return 结果
     */
    public int updateTestDrive(TestDrive testDrive);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteTestDriveById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTestDriveByIds(Long[] ids);

    List<TestDrive> getTestDriveById(Long id);
}
