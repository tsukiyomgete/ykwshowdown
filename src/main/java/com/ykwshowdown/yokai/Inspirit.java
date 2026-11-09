package com.ykwshowdown.yokai;

public class Inspirit {

    String effect;
    int effectPower;
    String target;

    Inspirit(String effect, int effectPower, String target){
        this.effect = effect;
        this.effectPower = effectPower;
        this.target = target;
    }

    public String getEffect(){
        return this.effect;
    }
    public int getEffectPower(){
        return this.effectPower;
    }
    public String getTarget(){
        return this.target;
    }
}