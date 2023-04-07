// Copyright (c) 2022 NetEase, Inc. All rights reserved.
// Use of this source code is governed by a MIT license that can be
// found in the LICENSE file.
package com.netease.yunxin.app.listentogether.utils;

import android.text.TextUtils;
import com.netease.yunxin.app.listentogether.Constants;
import com.netease.yunxin.app.listentogether.model.VoiceRoomSeat;
import com.netease.yunxin.kit.entertainment.common.model.RoomModel;
import com.netease.yunxin.kit.listentogetherkit.api.NEListenTogetherKit;
import com.netease.yunxin.kit.listentogetherkit.api.model.NEListenTogetherRoomInfo;
import com.netease.yunxin.kit.listentogetherkit.api.model.NEListenTogetherRoomMember;
import java.util.ArrayList;
import java.util.List;

public class ListenTogetherUtils {

  public static boolean isCurrentHost() {
    return NEListenTogetherKit.getInstance().getLocalMember() != null
        && TextUtils.equals(
            NEListenTogetherKit.getInstance().getLocalMember().getRole(), Constants.ROLE_HOST);
  }

  public static boolean isMySelf(String uuid) {
    return NEListenTogetherKit.getInstance().getLocalMember() != null
        && TextUtils.equals(NEListenTogetherKit.getInstance().getLocalMember().getAccount(), uuid);
  }

  public static boolean isHost(String uuid) {
    NEListenTogetherRoomMember member = getMember(uuid);
    if (member == null) {
      return false;
    }
    return TextUtils.equals(member.getRole(), Constants.ROLE_HOST);
  }

  public static NEListenTogetherRoomMember getMember(String uuid) {
    List<NEListenTogetherRoomMember> allMemberList =
        NEListenTogetherKit.getInstance().getAllMemberList();
    for (int i = 0; i < allMemberList.size(); i++) {
      NEListenTogetherRoomMember member = allMemberList.get(i);
      if (TextUtils.equals(member.getAccount(), uuid)) {
        return member;
      }
    }
    return null;
  }

  public static NEListenTogetherRoomMember getHost() {
    List<NEListenTogetherRoomMember> allMemberList =
        NEListenTogetherKit.getInstance().getAllMemberList();
    for (int i = 0; i < allMemberList.size(); i++) {
      NEListenTogetherRoomMember member = allMemberList.get(i);
      if (TextUtils.equals(member.getRole(), Constants.ROLE_HOST)) {
        return member;
      }
    }
    return null;
  }

  public static boolean isMute(String uuid) {
    NEListenTogetherRoomMember member = getMember(uuid);
    if (member != null) {
      return !member.isAudioOn();
    }
    return true;
  }

  public static List<VoiceRoomSeat> createSeats() {
    int size = VoiceRoomSeat.SEAT_COUNT;
    List<VoiceRoomSeat> seats = new ArrayList<>(size);
    for (int i = 1; i < size; i++) {
      seats.add(new VoiceRoomSeat(i + 1));
    }
    return seats;
  }

  public static String getCurrentName() {
    if (NEListenTogetherKit.getInstance().getLocalMember() == null) {
      return "";
    }
    return NEListenTogetherKit.getInstance().getLocalMember().getName();
  }

  public static String getCurrentAccount() {
    if (NEListenTogetherKit.getInstance().getLocalMember() == null) {
      return "";
    }
    return NEListenTogetherKit.getInstance().getLocalMember().getAccount();
  }

  public static List<RoomModel> neListenTogetherRoomInfos2RoomInfos(
      List<NEListenTogetherRoomInfo> listenTogetherRoomInfos) {
    List<RoomModel> result = new ArrayList<>();
    for (NEListenTogetherRoomInfo listenTogetherRoomInfo : listenTogetherRoomInfos) {
      result.add(neListenTogetherRoomInfo2RoomInfo(listenTogetherRoomInfo));
    }
    return result;
  }

  public static RoomModel neListenTogetherRoomInfo2RoomInfo(
      NEListenTogetherRoomInfo listenTogetherRoomInfo) {
    if (listenTogetherRoomInfo == null) {
      return null;
    }
    RoomModel roomModel = new RoomModel();
    roomModel.setRoomUuid(listenTogetherRoomInfo.getLiveModel().getRoomUuid());
    int onlineCount =
        Math.max(
            listenTogetherRoomInfo.getLiveModel().getAudienceCount() + 1,
            listenTogetherRoomInfo.getLiveModel().getOnSeatCount());
    roomModel.setOnlineCount(onlineCount);
    roomModel.setCover(listenTogetherRoomInfo.getLiveModel().getCover());
    roomModel.setLiveRecordId(listenTogetherRoomInfo.getLiveModel().getLiveRecordId());
    roomModel.setLiveTopic(listenTogetherRoomInfo.getLiveModel().getLiveTopic());
    roomModel.setAnchorAvatar(listenTogetherRoomInfo.getAnchor().getAvatar());
    roomModel.setAnchorNick(listenTogetherRoomInfo.getAnchor().getNick());
    roomModel.setAnchorUserUuid(listenTogetherRoomInfo.getAnchor().getAccount());
    return roomModel;
  }
}
