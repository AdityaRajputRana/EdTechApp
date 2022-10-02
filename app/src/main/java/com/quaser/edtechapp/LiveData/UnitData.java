package com.quaser.edtechapp.LiveData;

import androidx.lifecycle.MutableLiveData;

import com.quaser.edtechapp.models.ShortLesson;
import com.quaser.edtechapp.rest.response.UnitRP;

public class UnitData {
    private static MutableLiveData<UnitRP> unitRPMutableLiveData;
    public static MutableLiveData<UnitRP> getUnitRPMutableLiveData(){
        if (unitRPMutableLiveData == null)
            unitRPMutableLiveData = new MutableLiveData<UnitRP>();
        return unitRPMutableLiveData;
    }

    public static void setUnitData(UnitRP unitData){
        getUnitRPMutableLiveData().setValue(unitData);
    }

    public static UnitRP getUnitRP(){
        return getUnitRPMutableLiveData().getValue();
    }

    public static void completeLesson(int position) {
        UnitRP rp = getUnitRP();
        rp.getLesson().get(position).setIs_complete(true);
        rp.setCompleted_lessons(rp.getCompleted_lessons()+1);
        rp.setHas_user_started(true);

        if (rp.getLesson().size() > position +1){
            ShortLesson nextLesson = rp.getLesson().get(position+1);
            rp.setStart_at(UnitRP.LastLesson.getNewLastLesson(
                    nextLesson.getName(), position+1, nextLesson.getType()
            ));
        }

        unitRPMutableLiveData.postValue(rp);

        //todo update the home screen live data from here
        }
    }
