package com.chainup.action;

import com.chainup.core.config.ExceptionCode;
import com.chainup.core.config.RequestResult;
import com.chainup.core.dto.MeetingRoomDto;
import com.chainup.core.dto.MeetingRoomReserveDto;
import com.chainup.core.dto.MyMeetingRoomDto;
import com.chainup.core.params.ReserveMeetingParams;
import com.chainup.service.MeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.chainup.core.config.WebConstants.API_STATUS_DEVELOPING;

/**
 * @author lili
 * @date 2020/10/17 14:18
 * @see
 * @since
 */
@Slf4j
@RestController
@Api(value = "MeetingContoller", description = "会议室API 6/6")
public class MeetingContoller extends BaseController {

    @Autowired
    private MeetingService meetingService;

    /**
     * 1.通过时间查找可用会议室
     *
     * @return
     */
    @ApiOperation(value = "通过时间查找可用会议室" + API_STATUS_DEVELOPING, httpMethod = "GET")
    @GetMapping("/availableRoomByTime")
    public RequestResult<List<MeetingRoomDto>> availableRoomByTime(@ApiParam(name = "date", value = "日期")
                                                                   @RequestParam(name = "date") String date,
                                                                   @ApiParam(name = "beginTime", value = "开始时间")
                                                                   @RequestParam(name = "beginTime") String beginTime,
                                                                   @ApiParam(name = "endTime", value = "结束时间")
                                                                   @RequestParam(name = "endTime") String endTime,
                                                                   @ApiParam(name = "workplace", value = "职场")
                                                                   @RequestParam(name = "workplace") int workplace) {

        List<MeetingRoomDto> data = meetingService.availableRoomByTime(date, beginTime + ":00", endTime + ":00" , workplace);
        return success(data);
    }


    /**
     * 2.通过时间+room_id查找会议室及预定详情
     *
     * @return 会议室详情 + 此会议室预定meeting记录
     * 部门下拉列表
     * 参数 时间+room_id传回去
     */
    @ApiOperation(value = "某个会议室预定前的详情" + API_STATUS_DEVELOPING, httpMethod = "GET")
    @GetMapping("/meetingRoomInfo")
    public RequestResult<MeetingRoomReserveDto> meetingRoomInfo(@ApiParam(name = "date", value = "预定日期")
                                                                @RequestParam(name = "date") String date,
                                                                @ApiParam(name = "beginTime", value = "开始时间")
                                                                @RequestParam(name = "beginTime") String beginTime,
                                                                @ApiParam(name = "endTime", value = "结束时间")
                                                                @RequestParam(name = "endTime") String endTime,
                                                                @ApiParam(name = "roomId", value = "房间id")
                                                                @RequestParam(name = "roomId") int roomId,
                                                                @ApiParam(name = "openId", value = "用户Id")
                                                                @RequestParam(name = "openId") String openId) {


        beginTime = beginTime + ":00";
        endTime = endTime + ":00";
        MeetingRoomReserveDto reserveDto = meetingService.getMeetingRoomInfo(date, beginTime, endTime, roomId, openId);
        return success(reserveDto);
    }


    /**
     * 3.预定会议室
     *
     * @return
     */
    @PostMapping("/reserveMeetingRoom")
    @ApiOperation(value = "预定会议室" + API_STATUS_DEVELOPING, httpMethod = "POST")
    public RequestResult<Void> reserveMeetingRoom(@RequestBody ReserveMeetingParams reserveMeetingParams) {
        int result = meetingService.reserveMeetingRoom(reserveMeetingParams);
        if (ExceptionCode.SUCCESS.getCode() == result) {
            return success();
        } else {
            return error(ExceptionCode.parseCode(result));
        }
    }


    /**
     * 4.我的预定会议室
     *
     * @return
     */
    @GetMapping("/myMeetingList")
    @ApiOperation(value = "我预定的会议室列表" + API_STATUS_DEVELOPING, httpMethod = "GET")
    public RequestResult<List<MyMeetingRoomDto>> myMeetingList(@ApiParam(name = "openId", value = "用户Id")
                                                               @RequestParam(name = "openId") String openId) {

        if (StringUtils.isBlank(openId)) {
            return error(ExceptionCode.PARAM_ERROR);
        }
        List<MyMeetingRoomDto> data = meetingService.getMyMeetingList(openId);
        return success(data);
    }


    /**
     * 5.通过meeting_id查找会议室及预定详情
     *
     * @return 会议室详情 + 此会议室预定meeting记录
     * 部门下拉列表
     */
    @GetMapping("/myMeetingDetail")
    @ApiOperation(value = "我预定的会议室详情" + API_STATUS_DEVELOPING, httpMethod = "GET")
    public RequestResult<MyMeetingRoomDto> myMeetingDetail(@ApiParam(name = "meetingId", value = "会议Id")
                                                           @RequestParam(name = "meetingId") int meetingId) {
        MyMeetingRoomDto myMeetingRoomDto = meetingService.myMeetingDetail(meetingId);
        return success(myMeetingRoomDto);
    }


    /**
     * 6.取消自己预定的会议室
     *
     * @return
     */
    @PostMapping("/cancelMeetingRoom")
    @ApiOperation(value = "取消我的预定" + API_STATUS_DEVELOPING, httpMethod = "POST")
    public RequestResult<Void> cancelMeetingRoom(@ApiParam(name = "openId", value = "用户Id")
                                                 @RequestParam(name = "openId") String openId,
                                                 @ApiParam(name = "meetingId", value = "会议Id")
                                                 @RequestParam(name = "meetingId") int meetingId) {
        meetingService.cancelMeetingRoom(meetingId);
        return success();
    }


}
