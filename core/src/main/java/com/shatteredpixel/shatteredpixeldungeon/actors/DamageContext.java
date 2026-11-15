package com.shatteredpixel.shatteredpixeldungeon.actors;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;

public final class DamageContext {

    private static final ThreadLocal<Hero> SOURCE_HERO = new ThreadLocal<>();

    private DamageContext() {}

    public static Hero sourceHero() {
        return SOURCE_HERO.get();
    }

    public static Hero heroById(int heroId) {
        if (heroId <= 0) {
            return null;
        }

        Actor actor = Actor.findById(heroId);
        return actor instanceof Hero ? (Hero) actor : null;
    }

    public static Hero push(Hero hero) {
        Hero previous = SOURCE_HERO.get();
        SOURCE_HERO.set(hero);
        return previous;
    }

    public static void restore(Hero previous) {
        if (previous == null) {
            SOURCE_HERO.remove();
        } else {
            SOURCE_HERO.set(previous);
        }
    }
}
