/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.persist.maps;

import net.frozenorb.foxtrot.persist.PersistMap;

import java.util.UUID;

public class FDisplayMap extends PersistMap<Boolean> {

    public FDisplayMap() {
        super("FDisplay", "FDisplay");
    }

    @Override
    public String getRedisValue(Boolean toggled){
        return (String.valueOf(toggled));
    }

    @Override
    public Boolean getJavaObject(String str){
        return (Boolean.valueOf(str));
    }

    @Override
    public Object getMongoValue(Boolean toggled) {
        return (toggled);
    }

    public void setToggled(UUID update, boolean toggled) {
        updateValueAsync(update, toggled);
    }

    public boolean isToggled(UUID check) {
        return (contains(check) ? getValue(check) : true);
    }

}