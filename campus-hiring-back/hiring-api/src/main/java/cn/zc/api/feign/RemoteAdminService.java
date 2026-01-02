package cn.zc.api.feign;

import cn.zc.api.domain.vo.UserInfoVO;
import cn.zc.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 管理员服务调用接口
 * 
 * @author campus-hiring-system
 */
@FeignClient(contextId = "RemoteAdminService", value = "hiring-admin", url = "http://localhost:8081")
public interface RemoteAdminService {

    /**
     * 获取用户信息
     * 
     * @param userId 用户ID
     * @param userType 用户类型：1-学生，2-企业
     * @return 用户信息
     */
    @GetMapping("/admin/user/detail/{userId}")
    R<UserInfoVO> getUserInfo(@PathVariable("userId") Long userId, @RequestParam("userType") Integer userType);
}

