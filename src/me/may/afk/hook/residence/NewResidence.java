package me.may.afk.hook.residence;

import com.bekvon.bukkit.residence.protection.ResidenceManager;
import me.may.afk.hook.Residence;

public class NewResidence implements Residence {

    @Override
    public ResidenceManager getResidenceManager() {
        return com.bekvon.bukkit.residence.Residence.getInstance().getResidenceManager();
    }
}
