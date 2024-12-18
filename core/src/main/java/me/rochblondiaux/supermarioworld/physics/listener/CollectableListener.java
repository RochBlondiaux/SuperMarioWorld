package me.rochblondiaux.supermarioworld.physics.listener;

import java.util.UUID;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import lombok.RequiredArgsConstructor;
import me.rochblondiaux.supermarioworld.entity.Entity;
import me.rochblondiaux.supermarioworld.entity.collectable.Collectable;
import me.rochblondiaux.supermarioworld.entity.living.Player;
import me.rochblondiaux.supermarioworld.level.Level;

@RequiredArgsConstructor
public class CollectableListener implements ContactListener {

    private final Level level;

    @Override
    public void beginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();

        if (!(userDataA instanceof UUID) || !(userDataB instanceof UUID))
            return;

        Entity entityA = this.level.findById((UUID) userDataA).orElse(null);
        Entity entityB = this.level.findById((UUID) userDataB).orElse(null);
        if (entityA == null
            || entityB == null
            || !entityB.alive()
            || !entityA.alive()
            || (!(entityA instanceof Player) && !(entityB instanceof Player))
            || (!(entityA instanceof Collectable) && !(entityB instanceof Collectable)))
            return;

        Player player = entityA instanceof Player ? (Player) entityA : (Player) entityB;
        Collectable collectable = entityA instanceof Collectable ? (Collectable) entityA : (Collectable) entityB;

        collectable.collect(player);
        collectable.alive(false);

        contact.setEnabled(false);
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
