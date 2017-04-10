package com.crazymike.respositories;

import android.util.Log;

import com.crazymike.api.NetworkService;
import com.crazymike.models.SWCalendar;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;
import com.crazymike.util.RxUtil;

import java.util.List;

import lombok.Getter;
import rx.Observable;

/**
 * Created by ChaoJen on 2017/3/21.
 */

@Getter
public class SWCalendarRepository {

    private static final String TAG = SWCalendarRepository.class.getSimpleName();
    private static SWCalendarRepository instance = new SWCalendarRepository();

    private List<SWCalendar> mSWCalendarList;
    @Getter
    private String csServiceStartTime;
    @Getter
    private String csServiceEndTime;

    public static SWCalendarRepository getInstance() {
        return instance;
    }

    public Observable<SWCalendarRepository> callSWCalendar() {
        return NetworkService.getInstance().getProductApi().callSWCalendar("swcalendar", "1", "")
                .compose(RxUtil.mainAsync())
                .map(swCalendarResponse -> {
                    mSWCalendarList = swCalendarResponse.getSwCalendarList();
                    csServiceStartTime = mSWCalendarList.get(0).getCsServiceStartTime();
                    csServiceEndTime = mSWCalendarList.get(0).getCsServiceEdnTime();
                    return this;
                });
    }
}
