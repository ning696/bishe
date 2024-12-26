package com.gxyan.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.gxyan.dao.TestDriveMapper;
import com.gxyan.pojo.dto.TestDrive;
import com.gxyan.service.ITestDriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2024-11-09
 */
@Service
public class TestDriveServiceImpl implements ITestDriveService
{
    @Autowired
    private TestDriveMapper testDriveMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public TestDrive selectTestDriveById(Long id)
    {
        return testDriveMapper.selectTestDriveById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param testDrive 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TestDrive> selectTestDriveList(TestDrive testDrive)
    {
        return testDriveMapper.selectTestDriveList(testDrive);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param testDrive 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTestDrive(TestDrive testDrive)
    {
        testDrive.setId(createTestId());
        return testDriveMapper.insertTestDrive(testDrive);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param testDrive 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTestDrive(TestDrive testDrive)
    {
        return testDriveMapper.updateTestDrive(testDrive);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTestDriveByIds(Long[] ids)
    {
        return testDriveMapper.deleteTestDriveByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTestDriveById(Long id)
    {
        return testDriveMapper.deleteTestDriveById(id);
    }
    private Long createTestId() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String format = dateFormat.format(new Date()) + "000000";
        return Long.valueOf(format) + (num++);
    }

    @Override
    public List<TestDrive> getTestDrive(Long id) {
        return testDriveMapper.getTestDriveById(id);
    }

    private int num = 1;

    @Scheduled(cron="0 0 0 * * ?")
    private void clearNum() {
        num = 1;
    }
}
