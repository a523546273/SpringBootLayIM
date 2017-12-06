package com.fyp.layim.web.biz;

import com.fyp.layim.common.event.ApplyEvent;
import com.fyp.layim.domain.result.JsonResult;
import com.fyp.layim.im.LayimWebsocketStarter;
import com.fyp.layim.im.common.util.SpringUtil;
import com.fyp.layim.im.packet.ContextUser;
import com.fyp.layim.service.ApplyService;
import com.fyp.layim.service.GroupService;
import com.fyp.layim.service.UserService;
import com.fyp.layim.service.auth.ShiroUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fyp
 * @crate 2017/11/2 22:50
 * @project SpringBootLayIM
 */
@RestController
@RequestMapping("/layim")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ApplyService applyService;

    @GetMapping(value = "/base")
    public JsonResult getBaseData(){
        return userService.getBaseList();
    }

    @GetMapping(value="/members")
    public JsonResult getMembers( long id){
        return groupService.getGroupMembers(id);
    }

    @GetMapping(value = "/token")
    public JsonResult getToken() throws Exception{
        return userService.getUserToken();
    }

    /**
     * 好友申请
     * */
    @PostMapping(value = "/apply-friend")
    public JsonResult apply(@RequestParam("toid") Long toId,@RequestParam("remark") String remark){

        JsonResult result = applyService.saveFriendApply(toId, remark);
        //申请成功，发布申请事件，通知 toId处理消息，如果不在线，不会进行处理
        if(result.isSuccess()){
            SpringUtil.publish(new ApplyEvent("apply",toId));
        }
        return result;
    }
}
