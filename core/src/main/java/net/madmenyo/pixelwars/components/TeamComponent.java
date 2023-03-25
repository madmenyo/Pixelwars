package net.madmenyo.pixelwars.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;

import java.util.Objects;

public class TeamComponent implements Component, Pool.Poolable {
    public String teamName;
    public Color teamColor = new Color();

    @Override
    public void reset() {
        teamName = "Default Warriors";
        teamColor.set(Color.GREEN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamComponent that = (TeamComponent) o;

        if (!Objects.equals(teamName, that.teamName)) return false;
        return Objects.equals(teamColor, that.teamColor);
    }

    @Override
    public int hashCode() {
        int result = teamName != null ? teamName.hashCode() : 0;
        result = 31 * result + (teamColor != null ? teamColor.hashCode() : 0);
        return result;
    }
}
