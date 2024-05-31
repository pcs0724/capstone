package com.example.capstone;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarningLightManager {//경고등 찾아주는 역할
    private static WarningLightManager instance = null;
    private Map<String, WarningLight> warningLightsMap = new HashMap<>();

    private WarningLightManager(Context context) {
        loadWarningLights(context);
    }

    public static synchronized WarningLightManager getInstance(Context context) {
        if (instance == null) {
            instance = new WarningLightManager(context);
        }
        return instance;
    }

    private void loadWarningLights(Context context) {
        warningLightsMap.put("ABS", new WarningLight("warning_lights/abs.png", "ABS 경고등", AssetHelper.loadAssetTextAsString(context, "des/abs.txt"), AssetHelper.loadAssetTextAsString(context, "des/abs2.txt"), AssetHelper.loadAssetTextAsString(context, "des/abs3.txt")));
        warningLightsMap.put("Airbag", new WarningLight("warning_lights/airbag.png", "에어백 경고등", AssetHelper.loadAssetTextAsString(context, "des/airbags.txt"), AssetHelper.loadAssetTextAsString(context, "des/airbags2.txt"), AssetHelper.loadAssetTextAsString(context, "des/airbags3.txt")));
        warningLightsMap.put("Battery", new WarningLight("warning_lights/battery.png", "배터리 경고등", AssetHelper.loadAssetTextAsString(context, "des/battery.txt"), AssetHelper.loadAssetTextAsString(context, "des/battery2.txt"), AssetHelper.loadAssetTextAsString(context, "des/battery3.txt")));
        warningLightsMap.put("CheckBrake", new WarningLight("warning_lights/brake.png", "브레이크 경고등", AssetHelper.loadAssetTextAsString(context, "des/checkBrake.txt"), AssetHelper.loadAssetTextAsString(context, "des/checkBrake2.txt"), AssetHelper.loadAssetTextAsString(context, "des/checkBrake3.txt")));
        warningLightsMap.put("Door", new WarningLight("warning_lights/door.png", "문 열림 경고등", AssetHelper.loadAssetTextAsString(context, "des/door.txt"), AssetHelper.loadAssetTextAsString(context, "des/door2.txt"), AssetHelper.loadAssetTextAsString(context, "des/door3.txt")));
        warningLightsMap.put("ESC", new WarningLight("warning_lights/esc.png", "차체 자세 제어 장치 경고등", AssetHelper.loadAssetTextAsString(context, "des/esc.txt"), AssetHelper.loadAssetTextAsString(context, "des/esc2.txt"), AssetHelper.loadAssetTextAsString(context, "des/esc3.txt")));
        warningLightsMap.put("EmissionFilter", new WarningLight("warning_lights/emissionfilter.png", "디젤 매연 필터 경고등", AssetHelper.loadAssetTextAsString(context, "des/emissionfilter.txt"), AssetHelper.loadAssetTextAsString(context, "des/emissionfilter2.txt"), AssetHelper.loadAssetTextAsString(context, "des/emissionfilter3.txt")));
        warningLightsMap.put("Engine", new WarningLight("warning_lights/engine.png", "엔진 경고등", AssetHelper.loadAssetTextAsString(context, "des/engine.txt"), AssetHelper.loadAssetTextAsString(context, "des/engine2.txt"), AssetHelper.loadAssetTextAsString(context, "des/engine3.txt")));
        warningLightsMap.put("EngineOilPressure", new WarningLight("warning_lights/engineoilpressure.png", "엔진 오일 압력 경고등", AssetHelper.loadAssetTextAsString(context, "des/engineoilpressure.txt"), AssetHelper.loadAssetTextAsString(context, "des/engineoilpressure2.txt"), AssetHelper.loadAssetTextAsString(context, "des/engineoilpressure3.txt")));
        warningLightsMap.put("EngineOilLevel", new WarningLight("warning_lights/engineoillevel.png", "엔진 오일 부족 경고등", AssetHelper.loadAssetTextAsString(context, "des/engineoillevel.txt"), AssetHelper.loadAssetTextAsString(context, "des/engineoillevel2.txt"), AssetHelper.loadAssetTextAsString(context, "des/engineoillevel3.txt")));
        warningLightsMap.put("FrontFogLight", new WarningLight("warning_lights/frontfoglight.png", "전면 안개등 표시등", AssetHelper.loadAssetTextAsString(context, "des/frontfoglight.txt"), AssetHelper.loadAssetTextAsString(context, "des/frontfoglight2.txt"), AssetHelper.loadAssetTextAsString(context, "des/frontfoglight3.txt")));
        warningLightsMap.put("HighBeam", new WarningLight("warning_lights/highbeam.png", "상향등 표시등", AssetHelper.loadAssetTextAsString(context, "des/highbeam.txt"), AssetHelper.loadAssetTextAsString(context, "des/highbeam2.txt"), AssetHelper.loadAssetTextAsString(context, "des/highbeam3.txt")));
        warningLightsMap.put("LaneCenteringOff", new WarningLight("warning_lights/lanecenteringoff.png", "차선 이탈 방지 시스템 경고등", AssetHelper.loadAssetTextAsString(context, "des/lanecenteringoff.txt"), AssetHelper.loadAssetTextAsString(context, "des/lanecenteringoff2.txt"), AssetHelper.loadAssetTextAsString(context, "des/lanecenteringoff3.txt")));
        warningLightsMap.put("LightsAbnormal", new WarningLight("warning_lights/lightsabnormal.png", "라이트 결함 경고등", AssetHelper.loadAssetTextAsString(context, "des/lightsabnormal.txt"), AssetHelper.loadAssetTextAsString(context, "des/lightsabnormal2.txt"), AssetHelper.loadAssetTextAsString(context, "des/lightsabnormal3.txt")));
        warningLightsMap.put("LowBeam", new WarningLight("warning_lights/lowbeam.png", "하향등 표시등", AssetHelper.loadAssetTextAsString(context, "des/lowbeam.txt"), AssetHelper.loadAssetTextAsString(context, "des/lowbeam2.txt"), AssetHelper.loadAssetTextAsString(context, "des/lowbeam3.txt")));
        warningLightsMap.put("LowFuel", new WarningLight("warning_lights/lowfuel.png", "연료 부족 경고등", AssetHelper.loadAssetTextAsString(context, "des/lowfuel.txt"), AssetHelper.loadAssetTextAsString(context, "des/lowfuel2.txt"), AssetHelper.loadAssetTextAsString(context, "des/lowfuel3.txt")));
        warningLightsMap.put("LowTirePressure", new WarningLight("warning_lights/lowtirepressure.png", "타이어 압력 경고등", AssetHelper.loadAssetTextAsString(context, "des/lowtirepressure.txt"), AssetHelper.loadAssetTextAsString(context, "des/lowtirepressure2.txt"), AssetHelper.loadAssetTextAsString(context, "des/lowtirepressure3.txt")));
        warningLightsMap.put("LowWasher", new WarningLight("warning_lights/lowwasher.png", "워셔액 부족 경고등", AssetHelper.loadAssetTextAsString(context, "des/lowwasher.txt"), AssetHelper.loadAssetTextAsString(context, "des/lowwasher2.txt"), AssetHelper.loadAssetTextAsString(context, "des/lowwasher3.txt")));
        warningLightsMap.put("Master", new WarningLight("warning_lights/master.png", "통합 경고등", AssetHelper.loadAssetTextAsString(context, "des/master.txt"), AssetHelper.loadAssetTextAsString(context, "des/master2.txt"), AssetHelper.loadAssetTextAsString(context, "des/master3.txt")));
        warningLightsMap.put("Overheated", new WarningLight("warning_lights/overheated.png", "엔진 과열 경고등", AssetHelper.loadAssetTextAsString(context, "des/overheated.txt"), AssetHelper.loadAssetTextAsString(context, "des/overheated2.txt"), AssetHelper.loadAssetTextAsString(context, "des/overheated3.txt")));
        warningLightsMap.put("ParkingBrake", new WarningLight("warning_lights/parkingbrake.png", "주차 브레이크 경고등", AssetHelper.loadAssetTextAsString(context, "des/parkingbrake.txt"), AssetHelper.loadAssetTextAsString(context, "des/parkingbrake2.txt"), AssetHelper.loadAssetTextAsString(context, "des/parkingbrake3.txt")));
        warningLightsMap.put("Preheating", new WarningLight("warning_lights/preheating.png", "디젤 엔진 예열 경고등", AssetHelper.loadAssetTextAsString(context, "des/preheating.txt"), AssetHelper.loadAssetTextAsString(context, "des/preheating2.txt"), AssetHelper.loadAssetTextAsString(context, "des/preheating3.txt")));
        warningLightsMap.put("RearFogLight", new WarningLight("warning_lights/rearfoglight.png", "후면 안개등 표시등", AssetHelper.loadAssetTextAsString(context, "des/rearfoglight.txt"), AssetHelper.loadAssetTextAsString(context, "des/rearfoglight2.txt"), AssetHelper.loadAssetTextAsString(context, "des/rearfoglight3.txt")));
        warningLightsMap.put("Seatbelt", new WarningLight("warning_lights/seatbelt.png", "안전벨트 경고등", AssetHelper.loadAssetTextAsString(context, "des/seatbelt.txt"), AssetHelper.loadAssetTextAsString(context, "des/seatbelt2.txt"), AssetHelper.loadAssetTextAsString(context, "des/seatbelt3.txt")));
        warningLightsMap.put("SideLamp", new WarningLight("warning_lights/sidelamp.png", "조명 점등 표시등", AssetHelper.loadAssetTextAsString(context, "des/sidelamp.txt"), AssetHelper.loadAssetTextAsString(context, "des/sidelamp2.txt"), AssetHelper.loadAssetTextAsString(context, "des/sidelamp3.txt")));
        warningLightsMap.put("Steering", new WarningLight("warning_lights/steering.png", "파워 스티어링 경고등", AssetHelper.loadAssetTextAsString(context, "des/steering.txt"), AssetHelper.loadAssetTextAsString(context, "des/steering2.txt"), AssetHelper.loadAssetTextAsString(context, "des/steering3.txt")));
        warningLightsMap.put("WaterInFuel", new WarningLight("warning_lights/waterinfuel.png", "디젤 연료필터 수분 경고등", AssetHelper.loadAssetTextAsString(context, "des/waterinfuel.txt"), AssetHelper.loadAssetTextAsString(context, "des/waterinfuel2.txt"), AssetHelper.loadAssetTextAsString(context, "des/waterinfuel3.txt")));

    }
    public WarningLight getWarningLight(String key) {
        return warningLightsMap.get(key);
    }

    public List<WarningLight> getAllWarningLights() {
        return new ArrayList<>(warningLightsMap.values());
    }
}
