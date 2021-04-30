/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.persist.maps;

import net.frozenorb.foxtrot.persist.PersistMap;

import java.util.UUID;

public class ClaimOptionMap extends PersistMap<Boolean> {

    public ClaimOptionMap(){
        super("ClaimOptionToggle", "ClaimOptionToggle");
    }

    @Override
    public String getRedisValue(Boolean toggled) {
        return String.valueOf(toggled);
    }

    @Override
    public Object getMongoValue(Boolean toggled) {
        return String.valueOf(toggled);
    }

    @Override
    public Boolean getJavaObject(String str) {
        return Boolean.valueOf(str);
    }

    public void setScoreboardClaimEnabled(UUID update, boolean toggled){
        updateValueAsync(update, toggled);
    }

    public boolean isScoreboardClaimEnabled(UUID check){
        return contains(check);
    }

}
