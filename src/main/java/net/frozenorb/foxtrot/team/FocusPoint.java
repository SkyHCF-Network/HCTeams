/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.team;

import com.cheatbreaker.api.object.CBWaypoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;

@AllArgsConstructor
@Getter
public class FocusPoint {

    private Location location;
    private CBWaypoint cbWaypoint;
}

