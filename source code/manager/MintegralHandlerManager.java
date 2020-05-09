package com.mintegral.adapter.manager;

import com.mintegral.msdk.interstitialvideo.out.MTGBidInterstitialVideoHandler;
import com.mintegral.msdk.interstitialvideo.out.MTGInterstitialVideoHandler;
import com.mintegral.msdk.out.MTGBidRewardVideoHandler;
import com.mintegral.msdk.out.MTGRewardVideoHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fiissh.zhao
 * @version 1.0.0
 * @email yongchun.zhao@mintegral.com
 * @create_time 2020-四月-26 星期日
 * @description
 */
final public class MintegralHandlerManager {
    private Map<String, MTGRewardVideoHandler> mtgRewardVideoHandlerHashMap = new HashMap<String, MTGRewardVideoHandler>();
    private Map<String, MTGInterstitialVideoHandler> mtgInterstitialVideoHandlerHashMap = new HashMap<String, MTGInterstitialVideoHandler>();

    private MintegralHandlerManager() {
    }

    public static MintegralHandlerManager getInstance() {
        return ClassHolder.MINTEGRAL_HANDLER_MANAGER;
    }

    public MTGRewardVideoHandler getMTGRewardVideoHandler(String unitID) {
        if (mtgRewardVideoHandlerHashMap != null && mtgRewardVideoHandlerHashMap.containsKey(unitID)) {
            return mtgRewardVideoHandlerHashMap.get(unitID);
        }
        return null;
    }

    public void addMTGRewardVideoHandler(String unitID, MTGRewardVideoHandler mtgRewardVideoHandler) {
        if (mtgRewardVideoHandlerHashMap != null) {
            mtgRewardVideoHandlerHashMap.put(unitID, mtgRewardVideoHandler);
        }
    }


    public MTGInterstitialVideoHandler getMTGInterstitialVideoHandler(String unitID) {
        if (mtgInterstitialVideoHandlerHashMap != null && mtgInterstitialVideoHandlerHashMap.containsKey(unitID)) {
            return mtgInterstitialVideoHandlerHashMap.get(unitID);
        }
        return null;
    }

    public void addMTGInterstitialVideoHandler(String unitID, MTGInterstitialVideoHandler mtgInterstitialVideoHandler) {
        if (mtgInterstitialVideoHandlerHashMap != null) {
            mtgInterstitialVideoHandlerHashMap.put(unitID, mtgInterstitialVideoHandler);
        }
    }



    private static final class ClassHolder {
        private static final MintegralHandlerManager MINTEGRAL_HANDLER_MANAGER = new MintegralHandlerManager();
    }
}
