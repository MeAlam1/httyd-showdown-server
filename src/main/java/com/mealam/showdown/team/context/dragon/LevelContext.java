package com.mealam.showdown.team.context.dragon;

public record LevelContext(
        int level
) {
    public LevelContext {
        if (level < 1) {
            level = 1;
        }
    }
}