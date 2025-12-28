package com.mealam.showdown.team.context.dragon;

public record StatsContext(
        int attack,
        int speed,
        int defense,
        int armor,
        int firepower,
        int stealth,
        int stamina,
        int shotLimit,
        int venom,
        int jawStrength
) {}