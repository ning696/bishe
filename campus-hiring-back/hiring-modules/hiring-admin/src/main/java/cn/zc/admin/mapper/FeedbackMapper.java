package cn.zc.admin.mapper;

import cn.zc.admin.domain.entity.Feedback;
import cn.zc.admin.domain.vo.FeedbackListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 反馈Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {

    /**
     * 查询反馈列表
     */
    List<FeedbackListVO> selectFeedbackList(@Param("handleStatus") Integer handleStatus,
                                              @Param("feedbackType") String feedbackType);
}

